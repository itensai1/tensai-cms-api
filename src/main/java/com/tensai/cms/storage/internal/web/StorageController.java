package com.tensai.cms.storage.internal.web;

import com.tensai.cms.storage.api.StorageService;
import com.tensai.cms.storage.internal.web.dto.FileDownloadInfo;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/file")
@RequiredArgsConstructor
public class StorageController {
    private final StorageService storageService;

    @GetMapping("/download/{id}")
    public ResponseEntity<Resource> download(@PathVariable String id) {
        FileDownloadInfo downloadedFile = storageService.download(id);

        return ResponseEntity.ok()
                .contentType(downloadedFile.contentType())
                .body(downloadedFile.resource());
    }
}
