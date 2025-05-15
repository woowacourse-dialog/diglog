package com.dialog.server.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import com.dialog.server.domain.Category;
import com.dialog.server.domain.Discussion;
import com.dialog.server.domain.Like;
import com.dialog.server.domain.User;
import com.dialog.server.repository.DiscussionRepository;
import com.dialog.server.repository.LikeRepository;
import com.dialog.server.repository.UserRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
class LikeServiceTest {

    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DiscussionRepository discussionRepository;

    private LikeService likeService;

    @BeforeEach
    void setUp() {
        likeService = new LikeService(likeRepository, userRepository, discussionRepository);
    }

    @Test
    void 사용자는_토론에_좋아요를_할_수_있다() {
        //given
        User user = createUser();
        Discussion discussion = createDiscussion(user);

        //when
        likeService.create(user.getId(), discussion.getId());

        //then
        Like createdLike = Like.builder()
                .id(1L)
                .discussion(discussion)
                .user(user)
                .build();
        assertThat(likeRepository.findById(1L))
                .isPresent()
                .hasValue(createdLike);
    }

    @Test
    void 좋아요를_할때_사용자가_토론에_이미_좋아요를_했다면_예외가_발생한다() {
        //given
        User user = createUser();
        Discussion discussion = createDiscussion(user);
        createLike(user, discussion);

        //when
        //then
        assertThatThrownBy(() -> likeService.create(user.getId(), discussion.getId()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("해당 토론에는 이미 좋아요한 상태입니다.");
    }

    @Test
    void 사용자는_토론에_대해_좋아요를_취소할_수_있다() {
        //given
        User user = createUser();
        Discussion discussion = createDiscussion(user);
        createLike(user, discussion);

        //when
        likeService.delete(user.getId(), discussion.getId());

        //then
        assertThat(likeRepository.findById(1L))
                .isNotPresent();
    }


    private User createUser() {
        User user = User.builder()
                .email("email")
                .nickname("test")
                .emailNotification(false)
                .phoneNotification(false)
                .phoneNumber("111-111-1111")
                .build();
        return userRepository.save(user);
    }

    private Discussion createDiscussion(User user) {
        Discussion discussion = Discussion.builder()
                .author(user)
                .category(Category.ANDROID)
                .content("content")
                .startAt(LocalDateTime.of(2025, 5, 15, 10, 1))
                .endAt(LocalDateTime.of(2025, 5, 15, 11, 1))
                .title("title")
                .maxParticipantCount(3)
                .participantCount(3)
                .place("place")
                .viewCount(3)
                .build();
        return discussionRepository.save(discussion);
    }

    private Like createLike(User user, Discussion discussion) {
        Like like = Like.builder()
                .user(user)
                .discussion(discussion)
                .build();
        return likeRepository.save(like);
    }
}