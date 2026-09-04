package com.tensai.cms.storage.internal.web;

import com.tensai.cms.storage.api.StorageService;
import com.tensai.cms.storage.internal.web.dto.FileDownloadInfo;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@Tag(name = "Media Storage", description = "S3-compatible object storage and media file retrieval operations")
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
public class StorageController {

    private final StorageService storageService;

    @Operation(
            summary = "Download stored file",
            description = "Streams binary media files (images, audio, video, documents) stored in S3/MinIO by file ID or key."
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Binary media stream retrieved successfully",
                    content = @Content(
                            mediaType = MediaType.APPLICATION_OCTET_STREAM_VALUE,
                            schema = @Schema(type = "string", format = "binary")
                    )
            ),
            @ApiResponse(responseCode = "404", description = "Requested file object was not found")
    })
    @SecurityRequirements // Marks endpoint as public in Swagger UI
    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(
            @Parameter(description = "Unique file object key or ID", example = "a1b2c3d4-media.png", required = true)
            @PathVariable String id
    ) {
        FileDownloadInfo downloadedFile = storageService.download(id);

        return ResponseEntity.ok()
                .contentType(downloadedFile.contentType())
                .body(downloadedFile.resource());
    }
}
