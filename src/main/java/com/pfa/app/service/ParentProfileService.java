package com.pfa.app.service;

import com.pfa.app.dto.response.PlayerDataResponse;

import java.util.List;

public interface ParentProfileService {
    List<PlayerDataResponse> getChildrenRoster(String parentUsername);
}
