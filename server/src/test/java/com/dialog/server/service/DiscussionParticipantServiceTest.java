package com.dialog.server.service;

import static org.assertj.core.api.Assertions.tuple;
import static org.assertj.core.api.SoftAssertions.assertSoftly;

import com.dialog.server.domain.Category;
import com.dialog.server.domain.Discussion;
import com.dialog.server.domain.DiscussionParticipant;
import com.dialog.server.domain.User;
import com.dialog.server.repository.DiscussionParticipantRepository;
import com.dialog.server.repository.DiscussionRepository;
import com.dialog.server.repository.UserRepository;
import java.time.LocalDateTime;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
class DiscussionParticipantServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DiscussionRepository discussionRepository;

    @Autowired
    private DiscussionParticipantRepository discussionParticipantRepository;

    private DiscussionParticipantService discussionParticipantService;

    @BeforeEach
    void setUp() {
        discussionParticipantService = new DiscussionParticipantService(
                discussionParticipantRepository,
                userRepository,
                discussionRepository
        );
    }

    @Test
    void 사용자는_토론에_참여할_수_있다() {
        //given
        User user = createUser("email");
        Discussion discussion = createDiscussion(user, 3, 0);

        //when
        discussionParticipantService.participate(user.getId(), discussion.getId());

        //then
        assertSoftly(softly -> {
            softly.assertThat(discussionParticipantRepository.findAll())
                    .hasSize(1)
                    .extracting("participant", "discussion")
                    .containsExactly(tuple(user, discussion));
            softly.assertThat(discussion.getParticipantCount()).isEqualTo(1);
        });
    }

    private User createUser(String email) {
        User user = User.builder()
                .email(email)
                .nickname("test")
                .emailNotification(false)
                .phoneNotification(false)
                .phoneNumber("111-111-1111")
                .build();
        return userRepository.save(user);
    }

    private Discussion createDiscussion(User user, int maxParticipantCount, int participantCount) {
        Discussion discussion = Discussion.builder()
                .author(user)
                .category(Category.ANDROID)
                .content("content")
                .startAt(LocalDateTime.of(2025, 5, 15, 10, 1))
                .endAt(LocalDateTime.of(2025, 5, 15, 11, 1))
                .title("title")
                .maxParticipantCount(maxParticipantCount)
                .participantCount(participantCount)
                .place("place")
                .viewCount(3)
                .build();
        return discussionRepository.save(discussion);
    }

    private DiscussionParticipant createDiscussionParticipant(User participant, Discussion discussion) {
        DiscussionParticipant discussionParticipant = DiscussionParticipant.builder()
                .participant(participant)
                .discussion(discussion)
                .build();
        return discussionParticipantRepository.save(discussionParticipant);
    }
}
