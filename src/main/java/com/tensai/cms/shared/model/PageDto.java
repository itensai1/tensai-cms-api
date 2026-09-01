package com.tensai.cms.shared.model;


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
