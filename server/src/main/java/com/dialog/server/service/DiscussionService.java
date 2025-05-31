package com.dialog.server.service;

import com.dialog.server.domain.Discussion;
import com.dialog.server.domain.User;
import com.dialog.server.dto.request.DiscussionCreateRequest;
import com.dialog.server.dto.request.DiscussionCursorPageRequest;
import com.dialog.server.dto.request.DiscussionUpdateRequest;
import com.dialog.server.dto.request.SearchType;
import com.dialog.server.dto.response.DiscussionCreateResponse;
import com.dialog.server.dto.response.DiscussionCursorPageResponse;
import com.dialog.server.dto.response.DiscussionDetailResponse;
import com.dialog.server.dto.response.DiscussionSlotResponse;
import com.dialog.server.exception.DialogException;
import com.dialog.server.exception.ErrorCode;
import com.dialog.server.repository.DiscussionRepository;
import com.dialog.server.repository.UserRepository;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Slf4j
@RequiredArgsConstructor
public class DiscussionService {

    private static final String CURSOR_PART_DELIMITER = "_";
    private static final int CURSOR_TIME_INDEX = 0;
    private static final int CURSOR_ID_INDEX = 1;
    private static final int MAX_PAGE_SIZE = 50;
    private static final String NEXT_PAGE_CONDITION = "next";

    private final DiscussionRepository discussionRepository;
    private final UserRepository userRepository;

    @Transactional
    public DiscussionCreateResponse createDiscussion(DiscussionCreateRequest request, Long authorId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new DialogException(ErrorCode.NOT_FOUND_USER));
        Discussion discussion = request.toDiscussion(author);
        try {
            Discussion savedDiscussion = discussionRepository.save(discussion);
            return DiscussionCreateResponse.from(savedDiscussion);
        } catch (IllegalArgumentException ex) {
            throw new DialogException(ErrorCode.CREATE_DISCUSSION_FAILED);
        }
    }

    @Transactional
    public void updateDiscussion(Long discussionId,DiscussionUpdateRequest request) {
        Discussion savedDiscussion = discussionRepository.findById(discussionId)
                .orElseThrow(() -> new DialogException(ErrorCode.NOT_FOUND_DISCUSSION));
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
                .orElseThrow(() -> new DialogException(ErrorCode.NOT_FOUND_DISCUSSION));
        return DiscussionDetailResponse.from(savedDiscussion);
    }

    @Transactional
    public void deleteDiscussion(Long discussionId) {
        Discussion deleteDiscussion = discussionRepository.findById(discussionId)
                .orElseThrow(() -> new DialogException(ErrorCode.NOT_FOUND_DISCUSSION));
        if (deleteDiscussion.canNotDelete()) {
            throw new DialogException(ErrorCode.CANNOT_DELETE_DISCUSSION);
        }
        deleteDiscussion.delete();
    }

    @Transactional(readOnly = true)
    public DiscussionCursorPageResponse<DiscussionDetailResponse> getDiscussionsWithDateCursor(DiscussionCursorPageRequest request) {
        int pageSize = request.size();
        List<Discussion> discussions;

        if (request.cursor() == null || request.cursor().isEmpty()) {
            discussions = discussionRepository.findFirstPageDiscussionsByDate(PageRequest.of(0, pageSize + 1));
        } else {
            String[] cursorParts = request.cursor().split(CURSOR_PART_DELIMITER);
            LocalDateTime cursorTime = LocalDateTime.parse(cursorParts[CURSOR_TIME_INDEX]);
            Long cursorId = Long.valueOf(cursorParts[CURSOR_ID_INDEX]);

            if (NEXT_PAGE_CONDITION.equals(request.direction())) {
                discussions = discussionRepository.findDiscussionsBeforeDateCursor(cursorTime, cursorId, PageRequest.of(0, pageSize + 1));
            } else {
                discussions = discussionRepository.findDiscussionsAfterDateCursor(cursorTime, cursorId, PageRequest.of(0, pageSize + 1));
                Collections.reverse(discussions);
            }
        }

        return buildDateCursorResponse(discussions, pageSize, request.cursor());
    }

    @Transactional(readOnly = true)
    public DiscussionCursorPageResponse<DiscussionSlotResponse> searchDiscussion(SearchType searchType,
                                                                                 String query,
                                                                                 String cursor,
                                                                                 int size) {
        if (size > MAX_PAGE_SIZE) {
            throw new DialogException(ErrorCode.PAGE_SIZE_TOO_LARGE);
        }

        List<Discussion> discussions;
        switch (searchType) {
            case TITLE_OR_CONTENT -> discussions = searchDiscussionByTitleOrContent(query, cursor, size);
            case AUTHOR_NICKNAME -> discussions = searchDiscussionByAuthorNickname(query, cursor, size);
            default -> throw new DialogException(ErrorCode.INVALID_SEARCH_TYPE);
        }
        return buildDateCursorResponseV2(discussions, size, cursor);
    }

    private List<Discussion> searchDiscussionByTitleOrContent(String query,
                                                              String cursor,
                                                              int size) {
        List<Discussion> discussions;
        if (cursor == null || cursor.isEmpty()) {
            discussions = discussionRepository.findByTitleOrContentContainingPageable(query, PageRequest.of(0, size + 1));
        } else {
            String[] cursorParts = cursor.split(CURSOR_PART_DELIMITER);
            LocalDateTime cursorTime = LocalDateTime.parse(cursorParts[CURSOR_TIME_INDEX]);
            Long cursorId = Long.valueOf(cursorParts[CURSOR_ID_INDEX]);

            discussions = discussionRepository.findByTitleOrContentContainingBeforeDateCursor(query, cursorTime, cursorId, size + 1);
        }
        return discussions;
    }

    private List<Discussion> searchDiscussionByAuthorNickname(String query, String cursor, int size) {
        List<Discussion> discussions;
        if (cursor == null || cursor.isEmpty()) {
            discussions = discussionRepository.findByAuthorNicknameContainingPageable(query, PageRequest.of(0, size + 1));
        } else {
            String[] cursorParts = cursor.split(CURSOR_PART_DELIMITER);
            LocalDateTime cursorTime = LocalDateTime.parse(cursorParts[CURSOR_TIME_INDEX]);
            Long cursorId = Long.valueOf(cursorParts[CURSOR_ID_INDEX]);

            discussions = discussionRepository.findByAuthorNicknameContainingBeforeDateCursor(query, cursorTime, cursorId, size + 1);
        }
        return discussions;
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
                nextCursor = lastDiscussion.getCreatedAt().toString() + CURSOR_PART_DELIMITER + lastDiscussion.getId();
            }
            if (hasPrev) {
                Discussion firstDiscussion = content.getFirst();
                prevCursor = firstDiscussion.getCreatedAt().toString() + CURSOR_PART_DELIMITER + firstDiscussion.getId();
            }
        }
        List<DiscussionDetailResponse> responses = content.stream().map(DiscussionDetailResponse::from).toList();
        return new DiscussionCursorPageResponse<>(responses, nextCursor, prevCursor, hasNext, hasPrev, pageSize);
    }

    private DiscussionCursorPageResponse<DiscussionSlotResponse> buildDateCursorResponseV2(List<Discussion> discussions, int pageSize, String currentCursor) {
        boolean hasNext = discussions.size() > pageSize;

        List<Discussion> content = hasNext ? discussions.subList(0, pageSize) : discussions;

        String nextCursor = null;

        if (!content.isEmpty() && hasNext) {
            Discussion lastDiscussion = discussions.getLast();
            nextCursor = lastDiscussion.getCreatedAt().toString() + CURSOR_PART_DELIMITER + lastDiscussion.getId();
        }
        List<DiscussionSlotResponse> responses = content.stream().map(DiscussionSlotResponse::from).toList();
        return new DiscussionCursorPageResponse<>(responses, nextCursor, null, hasNext, false, pageSize);
    }
}
