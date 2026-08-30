package com.tensai.cms.shared.dto;


import lombok.Builder;

import java.util.List;

@Builder
public record PageDto<T>(
        List<T> content,
        int currentPage,
        int totalPages,
        long totalItems,
        boolean hasNext,
        boolean hasPrevious

) {
}
