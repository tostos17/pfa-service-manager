package com.pfa.app.controller;

import com.pfa.app.dto.request.MatchRequestDTO;
import com.pfa.app.dto.response.MatchResponseDTO;
import com.pfa.app.mapper.MatchMapper;
import com.pfa.app.model.Match;
import com.pfa.app.repository.MatchRepository;
import com.pfa.app.service.MatchService;
import com.pfa.app.service.StorageService;
import com.pfa.app.exception.ResourceNotFoundException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/matches")
@RequiredArgsConstructor
public class MatchController {

    private final MatchMapper matchMapper;
    private final MatchService matchService;
    private final StorageService storageService;

    @PostMapping("/{matchId}/media")
    public ResponseEntity<Match> uploadMatchMedia(
            @PathVariable Long matchId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("type") String mediaType // "photo" or "video"
    ) {
        Match match = matchService.getMatchById(matchId);

        // Upload to S3 under a specific sub-folder
        String s3Url = storageService.uploadFile(file, "matches/" + matchId);

        // Append url to target list collection
        if ("video".equalsIgnoreCase(mediaType)) {
            match.getVideoUrls().add(s3Url);
        } else {
            match.getPhotoUrls().add(s3Url);
        }

        matchService.createMatch(match);
        return ResponseEntity.ok(match);
    }

    @PostMapping
    public ResponseEntity<MatchResponseDTO> create(@Valid @RequestBody MatchRequestDTO request) {
        Match matchEntity = matchMapper.toEntity(request);
        Match savedMatch = matchService.createMatch(matchEntity);
        return ResponseEntity.ok(matchMapper.toResponseDTO(savedMatch));
    }
}