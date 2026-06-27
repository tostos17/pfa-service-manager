package com.pfa.app.service.impl;

import com.pfa.app.dto.request.*;
import com.pfa.app.dto.response.PlayerProfileResponse;
import com.pfa.app.exception.ApiException;
import com.pfa.app.model.Player;
import com.pfa.app.model.PlayerProfile;
import com.pfa.app.repository.PlayerProfileRepository;
import com.pfa.app.repository.PlayerProfileSpecification;
import com.pfa.app.repository.PlayerRepository;
import com.pfa.app.service.PlayerProfileService;
import com.pfa.app.utils.AgeCalculator;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class PlayerProfileServiceImpl implements PlayerProfileService {

    private final PlayerProfileRepository profileRepository;
    private final PlayerRepository playerRepository; // Assuming this manages your refactored entities

    @Override
    @Transactional
    public PlayerProfileResponse createOrUpdateProfile(String playerId, PlayerProfileRequest request) {
        // Find core PlayerData first using public business UUID
        Player playerData = playerRepository.findByPlayerId(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Player not found with ID: " + playerId));

        // Look up profile, or create a brand new one if it doesn't exist yet
        PlayerProfile profile = profileRepository.findByPlayerPlayerId(playerId)
                .orElse(new PlayerProfile());

        profile.setPlayer(playerData);
        profile.setHeightCm(request.getHeightCm());
        profile.setWeightKg(request.getWeightKg());
        profile.setDominantFoot(request.getDominantFoot().toUpperCase());
        profile.setPosition(request.getPosition());
        profile.setPreferredJerseyNumber(request.getPreferredJerseyNumber());
        profile.setBiography(request.getBiography());

        PlayerProfile savedProfile = profileRepository.save(profile);
        return mapToResponse(savedProfile);
    }

    @Override
    @Transactional(readOnly = true)
    public PlayerProfileResponse getProfileByPlayerId(String playerId) {
        PlayerProfile profile = profileRepository.findByPlayerPlayerId(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Metrics profile not initialized yet for player ID: " + playerId));
        return mapToResponse(profile);
    }

    @Override
    public String updateSquadNumber(HttpServletRequest httpServletRequest, UpdateSquadNumberRequest request) {
        PlayerProfile playerProfile = profileRepository.findByPlayerPlayerId(request.getPlayerId()).orElseThrow(() -> new ApiException("Player not found", HttpStatus.NOT_FOUND.value()));
        playerProfile.setPreferredJerseyNumber(request.getNumber());
        profileRepository.save(playerProfile);
        return "Squad number updated";
    }

    @Override
    public String updatePlayerHeight(HttpServletRequest httpServletRequest, UpdatePlayerWeightHeightRequest request) {
        PlayerProfile playerProfile = profileRepository.findByPlayerPlayerId(request.getPlayerId()).orElseThrow(() -> new ApiException("Player not found", HttpStatus.NOT_FOUND.value()));
        playerProfile.setHeightCm(request.getMeasurement());
        profileRepository.save(playerProfile);
        return "Height updated Successfully";
    }

    @Override
    public String updatePlayerWeight(HttpServletRequest httpServletRequest, UpdatePlayerWeightHeightRequest request) {
        PlayerProfile playerProfile = profileRepository.findByPlayerPlayerId(request.getPlayerId()).orElseThrow(() -> new ApiException("Player not found", HttpStatus.NOT_FOUND.value()));
        playerProfile.setWeightKg(request.getMeasurement());
        profileRepository.save(playerProfile);
        return "Height updated Successfully";
    }

    @Override
    public String updatePlayerPosition(HttpServletRequest httpServletRequest, UpdatePlayerPsitionRequest request) {
        PlayerProfile playerProfile = profileRepository.findByPlayerPlayerId(request.getPlayerId()).orElseThrow(() -> new ApiException("Player not found", HttpStatus.NOT_FOUND.value()));
        playerProfile.setPosition(request.getPosition().name());
        profileRepository.save(playerProfile);
        return "Position updated Successfully";
    }

    @Override
    public String updateDominantFoot(HttpServletRequest httpServletRequest, UpdatePlayerDominantFootRequest request) {
        PlayerProfile playerProfile = profileRepository.findByPlayerPlayerId(request.getPlayerId()).orElseThrow(() -> new ApiException("Player not found", HttpStatus.NOT_FOUND.value()));
        playerProfile.setDominantFoot(request.getDominantFoot().name());
        profileRepository.save(playerProfile);
        return "Dominant foot updated Successfully";
    }

    @Override
    public String updatePlayerCategory(HttpServletRequest httpServletRequest, UpdatePlayerCategoryRequest request) {
        PlayerProfile playerProfile = profileRepository.findByPlayerPlayerId(request.getPlayerId()).orElseThrow(() -> new ApiException("Player not found", HttpStatus.NOT_FOUND.value()));
        playerProfile.setCategory(request.getCategory().name());
        profileRepository.save(playerProfile);
        return "Player category updated Successfully";
    }

    @Override
    public Page<PlayerProfileResponse> getAllPlayerProfiles(String search, Boolean healthy, Pageable pageable) {
        // Combine dynamic specifications safely
        Specification<PlayerProfile> spec = Specification
                .where(PlayerProfileSpecification.searchByCategory(search));

        Page<PlayerProfile> playersPage = profileRepository.findAll(spec, pageable);

        // Map database entities to professional frontend ready DTOs
        return playersPage.map(profile -> PlayerProfileResponse.builder()
                .playerId(profile.getPlayer().getPlayerId())
                .playerName(profile.getPlayer().getFirstName() + " " + profile.getPlayer().getMiddleName().charAt(0) + ". " + profile.getPlayer().getLastName())
                .category(profile.getCategory())
                .parentPhone(profile.getPlayer().getParent().getPhone())
                .age(AgeCalculator.getAgeInYearsAndDays(profile.getPlayer().getDateOfBirth()))
                .heightCm(profile.getHeightCm())
                .weightKg(profile.getWeightKg())
                .dominantFoot(profile.getDominantFoot())
                .position(profile.getPosition())
                .preferredJerseyNumber(profile.getPreferredJerseyNumber())
                .biography(profile.getBiography())
                .photo(profile.getPlayer().getPassportUrl())
                .build());
    }

    private PlayerProfileResponse mapToResponse(PlayerProfile profile) {
        Player data = profile.getPlayer();
        return PlayerProfileResponse.builder()
                .playerId(data.getPlayerId())
                .playerName(data.getFirstName() + " " + data.getMiddleName().charAt(0) + ". " + data.getLastName())
                .heightCm(profile.getHeightCm())
                .weightKg(profile.getWeightKg())
                .dominantFoot(profile.getDominantFoot())
                .position(profile.getPosition())
                .preferredJerseyNumber(profile.getPreferredJerseyNumber())
                .biography(profile.getBiography())
                .photo(data.getPassportUrl())
                .age(AgeCalculator.getAgeInYearsAndDays(data.getDateOfBirth()))
                .parentPhone(data.getParent().getPhone())
                .category(profile.getCategory())
                .build();
    }
}