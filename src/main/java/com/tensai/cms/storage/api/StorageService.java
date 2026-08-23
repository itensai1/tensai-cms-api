package com.tensai.cms.storage.api;

import com.tensai.cms.storage.internal.web.dto.FileDownloadInfo;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface StorageService {
    String store(MultipartFile file) throws IOException;

    FileDownloadInfo download(String uniqueKey);
}
