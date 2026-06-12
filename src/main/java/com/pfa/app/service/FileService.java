package com.pfa.app.service;

import org.springframework.web.multipart.MultipartFile;

public interface FileService {
    /**
     * Uploads a file to AWS S3 and returns the public/presigned access URL string.
     */
    String uploadPlayerPassport(MultipartFile file, String username);
}