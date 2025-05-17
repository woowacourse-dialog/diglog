package com.dialog.server.service;

import com.dialog.server.domain.Discussion;
import com.dialog.server.domain.User;
import com.dialog.server.dto.request.DiscussionCreateRequest;
import com.dialog.server.dto.request.DiscussionUpdateRequest;
import com.dialog.server.dto.response.DiscussionCreateResponse;
import com.dialog.server.dto.response.DiscussionDetailResponse;
import com.dialog.server.dto.response.DiscussionPageResponse;
import com.dialog.server.exception.DialogException;
import com.dialog.server.exception.ErrorCode;
import com.dialog.server.repository.DiscussionRepository;
import com.dialog.server.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class DiscussionService {
    private final DiscussionRepository discussionRepository;
    private final UserRepository userRepository;

    public DiscussionService(DiscussionRepository discussionRepository, UserRepository userRepository) {
        this.discussionRepository = discussionRepository;
        this.userRepository = userRepository;
    }

    public DiscussionCreateResponse createDiscussion(DiscussionCreateRequest request, Long authorId) {
        User author = userRepository.findById(authorId)
                .orElseThrow(() -> new DialogException(ErrorCode.UNEXPECTED_ERROR));
        Discussion discussion = request.toDiscussion(author);
        Discussion savedDiscussion = discussionRepository.save(discussion);
        return DiscussionCreateResponse.from(savedDiscussion);
    }

    public void updateDiscussion(Long discussionId,DiscussionUpdateRequest request) {
        Discussion savedDiscussion = discussionRepository.findById(discussionId)
                .orElseThrow(() -> new DialogException(ErrorCode.UNEXPECTED_ERROR));
        // todo : 업데이트 시 request에 너무 종속되는 문제가 발생 -> 중간 DTO 생성할 지 Check
        savedDiscussion.update(request);
    }

    public DiscussionDetailResponse getDiscussionById(Long discussionId) {
        Discussion savedDiscussion = discussionRepository.findById(discussionId)
                .orElseThrow(() -> new DialogException(ErrorCode.UNEXPECTED_ERROR));
        return DiscussionDetailResponse.from(savedDiscussion);
    }

    public void deleteDiscussion(Long discussionId) {
        Discussion deleteDiscussion = discussionRepository.findById(discussionId)
                .orElseThrow(() -> new DialogException(ErrorCode.UNEXPECTED_ERROR));
        // todo: 참가자가 존재하는 경우 예외 발생
        if (deleteDiscussion.getParticipantCount() > 1) {
            throw new DialogException(ErrorCode.UNEXPECTED_ERROR);
        };
        deleteDiscussion.delete();
    }

    public DiscussionPageResponse getPageDiscussions(Long cursorId, int size) {
        List<Discussion> discussions;
        if (cursorId == null) {
            discussions = discussionRepository.findTopNByOrderByCreatedAtAsc(size);
        } else {
            discussions = discussionRepository.findTopNByIdLessThanOrderByCreatedAtAsc(cursorId, size);
        }
        boolean hasNext = (discussions.size() == size);
        Long nextCursor;
        if (hasNext) {
            nextCursor = discussions.getLast().getId();
        } else {
            nextCursor = null;
        }
        List<DiscussionDetailResponse> pageDiscussions = discussions.stream().map(DiscussionDetailResponse::from).toList();
        return DiscussionPageResponse.of(pageDiscussions, nextCursor, hasNext);
    }


}
