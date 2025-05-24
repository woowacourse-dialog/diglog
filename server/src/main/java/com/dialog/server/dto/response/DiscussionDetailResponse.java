package com.dialog.server.dto.response;

import com.dialog.server.domain.Category;
import com.dialog.server.domain.Discussion;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record DiscussionDetailResponse(
        Long id,
        String title,
        String content,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime startAt,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime endAt,
        String place,
        Category category,
        int participantCount,
        int maxParticipantCount,
        String summary,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        int viewCount
) {
    public static DiscussionDetailResponse from(Discussion discussion) {
        return new DiscussionDetailResponse(
                discussion.getId(),
                discussion.getTitle(),
                discussion.getContent(),
                discussion.getStartAt(),
                discussion.getEndAt(),
                discussion.getPlace(),
                discussion.getCategory(),
                discussion.getParticipantCount(),
                discussion.getMaxParticipantCount(),
                discussion.getSummary(),
                discussion.getCreatedAt(),
                discussion.getModifiedAt(),
                discussion.getViewCount()
        );
    }
}
