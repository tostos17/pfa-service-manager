package com.pfa.app.service;


import com.pfa.app.dto.request.*;
import com.pfa.app.dto.response.PlayerProfileResponse;
import com.pfa.app.enums.Position;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.data.annotation.ReadOnlyProperty;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface PlayerProfileService {
    PlayerProfileResponse createOrUpdateProfile(String playerId, PlayerProfileRequest request);
    PlayerProfileResponse getProfileByPlayerId(String playerId);

    String updateSquadNumber(HttpServletRequest httpServletRequest, UpdateSquadNumberRequest request);

    String updatePlayerHeight(HttpServletRequest httpServletRequest, UpdatePlayerWeightHeightRequest request);

    String updatePlayerWeight(HttpServletRequest httpServletRequest, UpdatePlayerWeightHeightRequest request);

    String updatePlayerPosition(HttpServletRequest httpServletRequest, UpdatePlayerPsitionRequest request);

    String updateDominantFoot(HttpServletRequest httpServletRequest, UpdatePlayerDominantFootRequest request);

    String updatePlayerCategory(HttpServletRequest httpServletRequest, UpdatePlayerCategoryRequest request);

    Page<PlayerProfileResponse> getAllPlayerProfiles(String search, Boolean healthy, Pageable pageable);
}