package com.dialog.server.dto.response;

import java.util.List;

public record DiscussionPageResponse(
        List<DiscussionDetailResponse> discussions,
        Long cursorId,
        boolean hasNext
) {
    public static DiscussionPageResponse of(List<DiscussionDetailResponse> discussions, Long cursorId, boolean hasNext) {
        return new DiscussionPageResponse(discussions, cursorId, hasNext);
    }
}
