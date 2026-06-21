package com.pfa.app.service.impl;

import com.pfa.app.dto.request.PlayerProfileRequest;
import com.pfa.app.dto.response.PlayerProfileResponse;
import com.pfa.app.model.Player;
import com.pfa.app.model.PlayerProfile;
import com.pfa.app.repository.PlayerProfileRepository;
import com.pfa.app.repository.PlayerRepository;
import com.pfa.app.service.PlayerProfileService;
import lombok.RequiredArgsConstructor;
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

    private PlayerProfileResponse mapToResponse(PlayerProfile profile) {
        Player data = profile.getPlayer();
        return PlayerProfileResponse.builder()
                .playerId(data.getPlayerId())
                .playerName(data.getFirstName() + " " + data.getLastName())
                .heightCm(profile.getHeightCm())
                .weightKg(profile.getWeightKg())
                .dominantFoot(profile.getDominantFoot())
                .position(profile.getPosition())
                .preferredJerseyNumber(profile.getPreferredJerseyNumber())
                .biography(profile.getBiography())
                .build();
    }
}