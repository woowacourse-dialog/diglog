package com.dialog.server.dto.response;

import java.util.List;

public record DiscussionCursorPageResponse<T>(
        List<T> content,
        String nextCursor,
        String prevCursor,
        boolean hasNext,
        boolean hasPrev,
        int size
) {
}
