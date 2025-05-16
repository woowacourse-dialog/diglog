package com.dialog.server.service;

import com.dialog.server.domain.Discussion;
import com.dialog.server.domain.Like;
import com.dialog.server.domain.User;
import com.dialog.server.repository.DiscussionRepository;
import com.dialog.server.repository.LikeRepository;
import com.dialog.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final UserRepository userRepository;
    private final DiscussionRepository discussionRepository;

    @Transactional
    public void create(Long userId, Long discussionId) {
        User user = getUserById(userId);
        Discussion discussion = getDiscussionById(discussionId);

        if (likeRepository.existsByUserAndDiscussion(user, discussion)) {
            throw new IllegalArgumentException("해당 토론에는 이미 좋아요한 상태입니다.");
        }
        Like like = Like.builder()
                .user(user)
                .discussion(discussion)
                .build();
        likeRepository.save(like);
    }

    @Transactional
    public void delete(Long userId, Long discussionId) {
        likeRepository.deleteByUserIdAndDiscussionId(userId, discussionId);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(userId + "에 해당하는 user를 찾을 수 없습니다."));
    }

    private Discussion getDiscussionById(Long discussionId) {
        return discussionRepository.findById(discussionId)
                .orElseThrow(() -> new IllegalArgumentException(discussionId + "에 해당하는 discussion을 찾을 수 없습니다."));
    }
}
