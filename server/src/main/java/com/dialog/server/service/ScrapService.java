package com.dialog.server.service;

import com.dialog.server.domain.Discussion;
import com.dialog.server.domain.Scrap;
import com.dialog.server.domain.User;
import com.dialog.server.exception.DialogException;
import com.dialog.server.exception.ErrorCode;
import com.dialog.server.repository.DiscussionRepository;
import com.dialog.server.repository.ScrapRepository;
import com.dialog.server.repository.UserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ScrapService {

    private final ScrapRepository scrapRepository;
    private final UserRepository userRepository;
    private final DiscussionRepository discussionRepository;

    @Transactional
    public void create(Long userId, Long discussionId) {
        User user = getUserById(userId);
        Discussion discussion = getDiscussionById(discussionId);
        if (isScraped(user, discussion)) {
            throw new DialogException(ErrorCode.ALREADY_SCRAPPED);
        }
        Scrap scrap = Scrap.builder()
                .user(user)
                .discussion(discussion)
                .build();
        scrapRepository.save(scrap);
    }

    @Transactional
    public void delete(Long userId, Long discussionId) {
        User user = getUserById(userId);
        Discussion discussion = getDiscussionById(discussionId);
        if (!isScraped(user, discussion)) {
            throw new DialogException(ErrorCode.NOT_SCRAPPED_YET);
        }
        scrapRepository.deleteByUserAndDiscussion(user, discussion);
    }

    private User getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(userId + "에 해당하는 user를 찾을 수 없습니다."));
    }

    private Discussion getDiscussionById(Long discussionId) {
        return discussionRepository.findById(discussionId)
                .orElseThrow(() -> new IllegalArgumentException(discussionId + "에 해당하는 discussion을 찾을 수 없습니다."));
    }

    private boolean isScraped(User user, Discussion discussion) {
        return scrapRepository.existsByUserAndDiscussion(user, discussion);
    }
}
