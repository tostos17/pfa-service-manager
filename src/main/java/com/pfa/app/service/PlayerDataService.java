package com.pfa.app.service;

import org.springframework.web.multipart.MultipartFile;

public interface PlayerDataService {
    String updatePlayerPassport(String playerId, MultipartFile passportPhoto);
}