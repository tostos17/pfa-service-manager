package com.pfa.app.service;

import com.pfa.app.dto.response.ApiResponse;
import com.pfa.app.dto.response.PlayerDataResponse;
import com.pfa.app.model.Player;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface PlayerDataService {
    String updatePlayerPassport(String playerId, MultipartFile passportPhoto);

    ApiResponse<Page<PlayerDataResponse>> getPlayers(Pageable pageable);
}