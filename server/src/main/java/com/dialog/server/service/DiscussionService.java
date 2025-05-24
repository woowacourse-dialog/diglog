package com.dialog.server.service;

import com.dialog.server.domain.Discussion;
import com.dialog.server.domain.User;
import com.dialog.server.dto.request.DiscussionCreateRequest;
import com.dialog.server.dto.request.DiscussionCursorPageRequest;
import com.dialog.server.dto.request.DiscussionUpdateRequest;
import com.dialog.server.dto.response.DiscussionCreateResponse;
import com.dialog.server.dto.response.DiscussionCursorPageResponse;
import com.dialog.server.dto.response.DiscussionDetailResponse;
import com.dialog.server.exception.DialogException;
import com.dialog.server.exception.ErrorCode;
import com.dialog.server.repository.DiscussionRepository;
import com.dialog.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class DiscussionService {

    private final DiscussionRepository discussionRepository;
    private final UserRepository userRepository;

    @Transactional
    public DiscussionCreateResponse createDiscussion(DiscussionCreateRequest request, Long authorId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new DialogException(ErrorCode.UNEXPECTED_ERROR));
        Discussion discussion = request.toDiscussion(author);
        Discussion savedDiscussion = discussionRepository.save(discussion);
        return DiscussionCreateResponse.from(savedDiscussion);
    }

    @Transactional
    public void updateDiscussion(Long discussionId,DiscussionUpdateRequest request) {
        Discussion savedDiscussion = discussionRepository.findById(discussionId)
                .orElseThrow(() -> new DialogException(ErrorCode.UNEXPECTED_ERROR));
        savedDiscussion.update(
                request.title(),
                request.content(),
                request.startAt(),
                request.endAt(),
                request.place(),
                request.maxParticipantCount(),
                request.category(),
                request.summary()
        );
    }

    @Transactional(readOnly = true)
    public DiscussionDetailResponse getDiscussionById(Long discussionId) {
        Discussion savedDiscussion = discussionRepository.findById(discussionId)
                .orElseThrow(() -> new DialogException(ErrorCode.UNEXPECTED_ERROR));
        return DiscussionDetailResponse.from(savedDiscussion);
    }

    @Transactional
    public void deleteDiscussion(Long discussionId) {
        Discussion deleteDiscussion = discussionRepository.findById(discussionId)
                .orElseThrow(() -> new DialogException(ErrorCode.UNEXPECTED_ERROR));
        // todo: 참가자가 존재하는 경우 예외 발생
        if (deleteDiscussion.getParticipantCount() > 1) {
            throw new DialogException(ErrorCode.UNEXPECTED_ERROR);
        };
        deleteDiscussion.delete();
    }

    @Transactional(readOnly = true)
    public DiscussionCursorPageResponse<DiscussionDetailResponse> getDiscussionsWithDateCursor(DiscussionCursorPageRequest request) {
        int pageSize = request.size();
        List<Discussion> discussions;

        if (request.cursor() == null || request.cursor().isEmpty()) {
            discussions = discussionRepository.findFirstPageDiscussionsByDate(PageRequest.of(0, pageSize + 1));
        } else {
            String[] cursorParts = request.cursor().split("_");
            LocalDateTime cursorTime = LocalDateTime.parse(cursorParts[0]);
            Long cursorId = Long.valueOf(cursorParts[1]);

            if ("next".equals(request.direction())) {
                discussions = discussionRepository.findDiscussionsBeforeDateCursor(cursorTime, cursorId, PageRequest.of(0, pageSize + 1));
            } else {
                discussions = discussionRepository.findDiscussionsAfterDateCursor(cursorTime, cursorId, PageRequest.of(0, pageSize + 1));
                Collections.reverse(discussions);
            }
        }

        return buildDateCursorResponse(discussions, pageSize, request.cursor());
    }

    private DiscussionCursorPageResponse<DiscussionDetailResponse> buildDateCursorResponse(List<Discussion> discussions, int pageSize, String currentCursor) {
        boolean hasNext = discussions.size() > pageSize;
        boolean hasPrev = currentCursor != null && !currentCursor.isEmpty();

        List<Discussion> content = hasNext ? discussions.subList(0, pageSize) : discussions;

        String nextCursor = null;
        String prevCursor = null;

        if (!content.isEmpty()) {
            if (hasNext) {
                Discussion lastDiscussion = content.getLast();
                nextCursor = lastDiscussion.getCreatedAt().toString() + "_" + lastDiscussion.getId();
            }
            if (hasPrev) {
                Discussion firstDiscussion = content.getFirst();
                prevCursor = firstDiscussion.getCreatedAt().toString() + "_" + firstDiscussion.getId();
            }
        }
        List<DiscussionDetailResponse> responses = content.stream().map(DiscussionDetailResponse::from).toList();
        return new DiscussionCursorPageResponse<>(responses, nextCursor, prevCursor, hasNext, hasPrev, pageSize);
    }
}
