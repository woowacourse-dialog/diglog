package com.dialog.server.service;

import com.dialog.server.domain.Category;
import com.dialog.server.domain.Discussion;
import com.dialog.server.domain.Scrap;
import com.dialog.server.domain.User;
import com.dialog.server.repository.DiscussionRepository;
import com.dialog.server.repository.ScrapRepository;
import com.dialog.server.repository.UserRepository;
import java.time.LocalDateTime;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
class ScrapServiceTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private DiscussionRepository discussionRepository;

    @Autowired
    private ScrapRepository scrapRepository;

    private ScrapService scrapService;

    @BeforeEach
    void setUp() {
        scrapService = new ScrapService(scrapRepository, userRepository, discussionRepository);
    }

    @Test
    void 사용자는_토론에_북마크를_할_수_있다() {
        //given
        User user = createUser();
        Discussion discussion = createDiscussion(user);

        //when
        scrapService.create(user.getId(), discussion.getId());

        //then
        Scrap expectedScrap = Scrap.builder()
                .id(1L)
                .user(user)
                .discussion(discussion)
                .build();
        Assertions.assertThat(scrapRepository.findById(1L))
                .hasValue(expectedScrap);
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
}
