package com.dialog.server.dto.response;

import com.dialog.server.domain.Category;
import com.dialog.server.domain.Discussion;
import com.fasterxml.jackson.annotation.JsonFormat;

import java.time.LocalDateTime;

public record DiscussionSlotResponse(
        Long id,
        String title,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime startAt,
        @JsonFormat(pattern = "yyyy-MM-dd HH:mm")
        LocalDateTime endAt,
        String place,
        Category category,
        int participantCount,
        int maxParticipantCount,
        LocalDateTime createdAt,
        LocalDateTime modifiedAt,
        int viewCount
) {
    public static DiscussionSlotResponse from(Discussion discussion) {
        return new DiscussionSlotResponse(
                discussion.getId(),
                discussion.getTitle(),
                discussion.getStartAt(),
                discussion.getEndAt(),
                discussion.getPlace(),
                discussion.getCategory(),
                discussion.getParticipantCount(),
                discussion.getMaxParticipantCount(),
                discussion.getCreatedAt(),
                discussion.getModifiedAt(),
                discussion.getViewCount()
        );
    }
}
