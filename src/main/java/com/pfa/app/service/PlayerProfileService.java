package com.pfa.app.service;


import com.pfa.app.dto.request.PlayerProfileRequest;
import com.pfa.app.dto.response.PlayerProfileResponse;

public interface PlayerProfileService {
    PlayerProfileResponse createOrUpdateProfile(String playerId, PlayerProfileRequest request);
    PlayerProfileResponse getProfileByPlayerId(String playerId);
}