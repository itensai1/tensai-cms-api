package com.tensai.cms.storage.internal.web.dto;

import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;

public record FileDownloadInfo(
        Resource resource,
        MediaType contentType
) {
}