package com.pfa.app.service;

import com.pfa.app.dto.ParentRegisterRequest;
import com.pfa.app.dto.request.*;
import com.pfa.app.dto.response.AuthResponse;
import com.pfa.app.model.*;
import com.pfa.app.repository.ParentRepository;
import com.pfa.app.repository.RoleRepository;
import com.pfa.app.repository.UserRepository;
import com.pfa.app.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final ParentRepository parentRepository;
    private final JwtService jwtService;
    private final FileService fileService;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;

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
        // 1. Fail-fast validations before touching S3 or generating IDs
        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            throw new IllegalArgumentException("Username is already taken");
        }

        Role playerRole = roleRepository.findByName("ROLE_PLAYER")
                .orElseThrow(() -> new IllegalArgumentException("Player role not initialized"));

        Parent linkedParent = null;
        if (request.getParentId() != null) {
            linkedParent = parentRepository.findById(request.getParentId())
                    .orElseThrow(() -> new IllegalArgumentException("Registration failed: Parent not found with ID " + request.getParentId()));
        }

        // 2. Generate the unique business ID upfront
        String uniquePlayerId = UUID.randomUUID().toString();

        // 3. Pass the uniquePlayerId to S3 instead of username.
        // If the DB save fails, you know exactly which ID to clean up in S3 if needed.
        String uploadedUrl = null;
        if (passportPhoto != null && !passportPhoto.isEmpty()) {
            uploadedUrl = fileService.uploadPlayerPassport(passportPhoto, uniquePlayerId);
        }

        // 4. Build Security User Context
        User user = User.builder()
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .roles(Set.of(playerRole))
                .build();

        // 5. Build Business Profile Context
        Player player = Player.builder()
                .playerId(uniquePlayerId) // Securely assigned
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
                .build();

        user.setPlayer(player);
        userRepository.save(user); // Triggers transactional database commit

        UserDetails userDetails = userDetailsService.loadUserByUsername(user.getUsername());
        return AuthResponse.builder()
                .token(jwtService.generateToken(userDetails))
                .username(user.getUsername())
                .build();
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
                .employeeId(request.getEmployeeId())
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