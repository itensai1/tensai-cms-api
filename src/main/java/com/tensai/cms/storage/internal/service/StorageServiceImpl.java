package com.tensai.cms.storage.internal.service;

import com.tensai.cms.shared.exception.CustomException;
import com.tensai.cms.storage.api.StorageService;
import com.tensai.cms.storage.internal.config.StorageProperties;
import com.tensai.cms.storage.internal.web.dto.FileDownloadInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.core.ResponseInputStream;
import software.amazon.awssdk.core.sync.RequestBody;
import software.amazon.awssdk.services.s3.S3Client;
import software.amazon.awssdk.services.s3.model.*;

import java.io.IOException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageServiceImpl implements StorageService {
    private final StorageProperties properties;
    private final S3Client s3Client;

    @Override
    public String store(MultipartFile file) throws IOException {

        String originalFilename = file.getOriginalFilename();

        String extension = (originalFilename != null && originalFilename.contains(".")) ?
                originalFilename.substring(originalFilename.lastIndexOf(".")) : "";

        String uniqueKey = UUID.randomUUID() + extension;

        // Upload to MinIO/S3
        PutObjectRequest putObjectRequest = PutObjectRequest.builder()
                .bucket(properties.bucket())
                .key(uniqueKey)
                .contentType(file.getContentType())
                .build();

        s3Client.putObject(putObjectRequest,
                RequestBody.fromInputStream(file.getInputStream(), file.getSize()));

        return uniqueKey;
    }

    @Override
    public FileDownloadInfo download(String uniqueKey) {

        try {
            GetObjectRequest getObjectRequest = GetObjectRequest.builder()
                    .bucket(properties.bucket())
                    .key(uniqueKey)
                    .build();

            ResponseInputStream<GetObjectResponse> s3Object = s3Client.getObject(getObjectRequest);
            MediaType mediaType = MediaType.parseMediaType(s3Object.response().contentType());
            return new FileDownloadInfo(new InputStreamResource(s3Object), mediaType);

        } catch (NoSuchKeyException e) {
            throw new CustomException(404, e.getMessage());
        }
    }
}
