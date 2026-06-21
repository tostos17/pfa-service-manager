package com.pfa.app.service;

import com.pfa.app.dto.response.ApiResponse;
import com.pfa.app.dto.response.ParentResponseDto;
import com.pfa.app.dto.response.PlayerDataResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ParentProfileService {
    List<PlayerDataResponse> getChildrenRoster(String parentUsername);

    ApiResponse<Page<ParentResponseDto>> getParents(Pageable pageable);
}
