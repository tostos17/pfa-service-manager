package com.pfa.app.service.impl;

import com.pfa.app.dto.response.ApiResponse;
import com.pfa.app.dto.response.PlayerDataResponse;
import com.pfa.app.model.Player;
import com.pfa.app.repository.PlayerRepository;
import com.pfa.app.service.FileService;
import com.pfa.app.service.PlayerDataService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class PlayerDataServiceImpl implements PlayerDataService {

    private final PlayerRepository playerRepository;
    private final FileService fileService;

    // Custom runtime exception handler to bridge with our Global Exception Handler seamlessly
    @Override
    @Transactional
    public String updatePlayerPassport(String playerId, MultipartFile passportPhoto) {
        // 1. Locate the player profile by business UUID string
        Player player = playerRepository.findByPlayerId(playerId)
                .orElseThrow(() -> new IllegalArgumentException("Update failed: Player profile not found with ID: " + playerId));

        // 2. Stream the new photo binary content straight to AWS S3
        // This overwrites or adds a new file under the matching unique player bucket subfolder prefix
        String newPassportUrl = fileService.uploadPlayerPassport(passportPhoto, player.getPlayerId());

        // 3. Persist the absolute address pointer inside PostgreSQL
        player.setPassportUrl(newPassportUrl);
        playerRepository.save(player);

        return newPassportUrl;
    }

    @Override
    public ApiResponse<Page<PlayerDataResponse>> getPlayers(Pageable pageable) {
        Page<Player> playersPage = playerRepository.findAll(pageable);

        return ApiResponse.ok(
                playersPage.map(player -> new PlayerDataResponse(
                        player.getPlayerId(),
                        player.getFirstName(),
                        player.getMiddleName(),
                        player.getLastName(),
                        player.getEmail(),
                        player.getPhone(),
                        player.getDateOfBirth(),
                        player.getParent() != null ? player.getParent().getFirstName() + " " + player.getParent().getLastName() : null,
                        player.isHealthy(),
                        player.getHealthConcernDescription(),
                        player.getPassportUrl()
                ))
        );
    }
}