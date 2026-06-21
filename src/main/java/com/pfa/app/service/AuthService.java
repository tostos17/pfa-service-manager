package com.pfa.app.service;

import com.pfa.app.dto.request.*;
import com.pfa.app.dto.response.AuthResponse;
import com.pfa.app.exception.ApiException;
import com.pfa.app.model.*;
import com.pfa.app.repository.ParentRepository;
import com.pfa.app.repository.RoleRepository;
import com.pfa.app.repository.UserRepository;
import com.pfa.app.security.JwtService;
import com.pfa.app.service.impl.AdminIdentifierService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ParentRepository parentRepository;
    private final JwtService jwtService;
    private final FileService fileService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final AdminIdentifierService adminIdentifierService;

    @Transactional
    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username is already taken");
        }

        Set<Role> assignedRoles = new HashSet<>();
        for (String roleName : request.getRoles()) {
            // Standardize format to look up "ROLE_ADMIN", "ROLE_COACH" etc.
            String formattedName = roleName.toUpperCase().startsWith("ROLE_")
                    ? roleName.toUpperCase()
                    : "ROLE_" + roleName.toUpperCase();

            Role role = roleRepository.findByName(formattedName)
                    .orElseThrow(() -> new IllegalArgumentException("Role not found in system: " + roleName));
            log.info("Role found: {}", roleName);
            assignedRoles.add(role);
        }

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(assignedRoles)
                .enabled(true)
                .build();

        userRepository.save(user);

        // Load UserDetails to create token
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        String jwtToken = jwtService.generateToken(userDetails);

        return AuthResponse.builder()
                .token(jwtToken)
                .username(user.getUsername())
                .build();
    }

    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String jwtToken = jwtService.generateToken(userDetails);

        return AuthResponse.builder()
                .token(jwtToken)
                .username(userDetails.getUsername())
                .build();
    }

    @Transactional
    public AuthResponse registerCoach(CoachRegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username is already taken");
        }

        Role coachRole = roleRepository.findByName("ROLE_COACH")
                .orElseThrow(() -> new IllegalArgumentException("Coach role not initialized"));

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(coachRole))
                .build();

        Coach coach = Coach.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .specialization(request.getSpecialization())
                .licenseLevel(request.getLicenseLevel())
                .user(user)
                .build();

        user.setCoach(coach);
        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        return AuthResponse.builder().token(jwtService.generateToken(userDetails)).username(user.getUsername()).build();
    }

    @Transactional
    public AuthResponse registerPlayer(PlayerRegisterRequest request, MultipartFile passportPhoto) {
        validateHealthEntries(request.getHealthy(), request.getHealthConcernDescription());
        // 1. Fail-fast validation checks
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username is already taken");
        }

        Role playerRole = roleRepository.findByName("ROLE_PLAYER")
                .orElseThrow(() -> new IllegalArgumentException("Player role not initialized"));

        Parent linkedParent = null;
        if (request.getParentId() != null) {
            linkedParent = parentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Parent profile not found"));
        }

        // 2. Generate unique identifiers safely
        String uniquePlayerId = UUID.randomUUID().toString();
        String uploadedUrl = null;
        if (passportPhoto != null && !passportPhoto.isEmpty()) {
            uploadedUrl = fileService.uploadPlayerPassport(passportPhoto, uniquePlayerId);
        }

        // 3. Build authorization user core
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(playerRole))
                .build();

        // 4. Build PlayerData core
        Player player = Player.builder()
                .playerId(uniquePlayerId)
                .firstName(request.getFirstName())
                .middleName(request.getMiddleName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .phone(request.getPhone())
                .dateOfBirth(request.getDateOfBirth())
                .address(request.getAddress())
                .stateOfOrigin(request.getStateOfOrigin())
                .country(request.getCountry())
                .healthy(request.getHealthy().equalsIgnoreCase("true"))
                .healthConcernDescription(request.getHealthConcernDescription())
                .passportUrl(uploadedUrl)
                .parent(linkedParent)
                .user(user)
                .registrationDate(LocalDate.now(ZoneId.of("Africa/Lagos")))
                .build();

        // REQUIREMENT 1: Instantiate blank/default performance profile layer
        PlayerProfile initialProfile = PlayerProfile.builder()
                .heightCm(0.0)
                .weightKg(0.0)
                .dominantFoot("PENDING")
                .position("UNASSIGNED")
                .player(player)
                .build();

        player.setPlayerProfile(initialProfile);

        // REQUIREMENT 2: Generate unique name-reflective Account string & Account entry
        // Format Example: PFA-SMITH-J-2026-A1B2
        String shortRandom = UUID.randomUUID().toString().substring(0, 4).toUpperCase();
        String firstInitial = player.getFirstName().substring(0, 1).toUpperCase();
        String cleanedLastName = player.getLastName().replaceAll("\\s+", "").toUpperCase();
        int currentYear = java.time.Year.now().getValue();

        String personalizedAccountId = String.format("PFA-%s-%s-%d-%s", cleanedLastName, firstInitial, currentYear, shortRandom);

        Account initialAccount = Account.builder()
                .accountId(personalizedAccountId)
                .status(true)
                .currentPending(BigDecimal.ZERO)
                .outstandings(BigDecimal.ZERO)
                .player(player)
                .build();

        player.setAccount(initialAccount);

        // 5. Save the root entity. CascadeType.ALL propagates and saves the Profile and Account
        user.setPlayer(player);
        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        return AuthResponse.builder()
                .token(jwtService.generateToken(userDetails))
                .username(user.getUsername())
                .build();
    }

    private void validateHealthEntries(String healthy, String healthConcernDescription) {
        if(healthy.equalsIgnoreCase("false") && (healthConcernDescription == null || healthConcernDescription.isEmpty())) {
            throw new ApiException("Kindly describe the player's health concerns", 400);
        }
    }

    @Transactional
    public AuthResponse registerAdmin(AdminRegisterRequest request) {
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username is already taken");
        }

        Role adminRole = roleRepository.findByName("ROLE_ADMIN")
                .orElseThrow(() -> new IllegalArgumentException("Admin role not initialized"));

        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(adminRole))
                .build();

        Admin admin = Admin.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .employeeId(adminIdentifierService.generateAdminEmployeeId())
                .user(user)
                .build();

        user.setAdmin(admin);
        userRepository.save(user);

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        return AuthResponse.builder().token(jwtService.generateToken(userDetails)).username(user.getUsername()).build();
    }

    @Transactional
    public AuthResponse registerParent(ParentRegisterRequest request) {
        // 1. Check if user already exists
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username is already taken");
        }

        // 2. Resolve the ROLE_PARENT meta record
        Role parentRole = roleRepository.findByName("ROLE_PARENT")
                .orElseThrow(() -> new IllegalArgumentException("Parent role structure not initialized in database"));

        // 3. Construct core security User context
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(parentRole))
                .build();

        // 4. Construct satellite profile context and bind bidirectional relations
        Parent parent = Parent.builder()
                .title(request.getTitle())
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .phone(request.getPhone())
                .email(request.getEmail())
                .address(request.getAddress())
                .user(user) // Maps parent -> user
                .build();

        user.setParent(parent); // Maps user -> parent

        // 5. Persist the tree; CascadeType.ALL will automatically generate the row in 'parents' table
        userRepository.save(user);

        // 6. Return standard JWT response context immediately
        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        return AuthResponse.builder()
                .token(jwtService.generateToken(userDetails))
                .username(user.getUsername())
                .build();
    }
}