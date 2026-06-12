package com.pfa.app.service.impl;

import com.pfa.app.dto.response.PlayerDataResponse;
import com.pfa.app.model.Player;
import com.pfa.app.repository.PlayerRepository;
import com.pfa.app.service.ParentProfileService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParentProfileServiceImpl implements ParentProfileService {

    private final PlayerRepository playerRepository;

    @Override
    @Transactional(readOnly = true)
    public List<PlayerDataResponse> getChildrenRoster(String parentUsername) {
        List<Player> children = playerRepository.findByParentUserUsername(parentUsername);

        return children.stream()
                .map(player -> PlayerDataResponse.builder()
                        .playerId(player.getPlayerId())
                        .firstName(player.getFirstName())
                        .middleName(player.getMiddleName())
                        .lastName(player.getLastName())
                        .email(player.getEmail())
                        .phone(player.getPhone())
                        .dateOfBirth(player.getDateOfBirth())
                        .healthy(player.isHealthy())
                        .healthConcernDescription(player.getHealthConcernDescription())
                        .passportUrl(player.getPassportUrl())
                        .build())
                .collect(Collectors.toList());
    }
}