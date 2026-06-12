package com.pfa.app.service.impl;

import com.pfa.app.service.FileService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.GetUrlRequest;
import software.amazon.awssdk.services.s3.model.PutObjectRequest;

import java.io.IOException;
import java.util.Arrays;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileServiceImpl implements FileService {

    private final S3Client s3Client;

    @Value("${aws.s3.bucket-name}")
    private String bucketName;

    @Override
    public String uploadPlayerPassport(MultipartFile file, String uniquePlayerId) {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Cannot upload an empty file");
        }

        // 1. Content validation safety guard check
        String contentType = file.getContentType();
        if (contentType == null || !isSupportedImageType(contentType)) {
            throw new IllegalArgumentException("Unsupported file format. Only JPEG, JPG, and PNG are allowed.");
        }

        // 2. Clean up file extension safely
        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename());
        String extension = "";
        int dotIndex = originalFilename.lastIndexOf('.');
        if (dotIndex >= 0) {
            extension = originalFilename.substring(dotIndex);
        }

        // 3. Construct unique file object layout key
        // Example structure: passports/4f39b1a2-89cb-4c11-a5d6-88019ab23f31_passport.jpg
        String objectKey = "passports/" + uniquePlayerId + "_passport" + extension;

        try {
            // 4. Build put metadata specifications
            PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                    .bucket(bucketName)
                    .key(objectKey)
                    .contentType(contentType)
                    .build();

            // 5. stream raw multi-part contents directly to AWS
            s3Client.putObject(putObjectRequest,
                    RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

            // 6. Fetch target public access address pointer string
            return s3Client.utilities()
                    .getUrl(GetUrlRequest.builder().bucket(bucketName).key(objectKey).build())
                    .toExternalForm();

        } catch (IOException e) {
            throw new RuntimeException("Failed to read multi-part file bytes stream context", e);
        } catch (Exception e) {
            throw new RuntimeException("AWS S3 Connection network transaction failed", e);
        }
    }

    private boolean isSupportedImageType(String contentType) {
        return Arrays.asList("image/jpeg", "image/jpg", "image/png").contains(contentType.toLowerCase());
    }
}