package com.dialog.server.service;

import com.dialog.server.config.JpaConfig;
import com.dialog.server.domain.Role;
import com.dialog.server.domain.User;
import com.dialog.server.dto.auth.response.UserInfoResponse;
import com.dialog.server.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@Import(JpaConfig.class)
@ActiveProfiles("test")
@DataJpaTest
public class UserServiceTest {

    @Autowired
    private UserRepository userRepository;

    private UserService userService;

    @BeforeEach
    void setUp() {
        userService = new UserService(userRepository);
        userRepository.save(createUser());
    }

    @Test
    void 유저_정보를_생성한다() {
        //given
        UserInfoResponse userInfo = userService.getUserInfo("oauthId1");
        assertAll(
                () -> assertThat(userInfo.nickname()).isEqualTo("minggom"),
                () -> assertThat(userInfo.email()).isEqualTo("hippo@test.com"),
                () -> assertThat(userInfo.isNotificationEnabled()).isTrue()
        );
    }

    private User createUser() {
        return User.builder()
                .nickname("minggom")
                .email("hippo@test.com")
                .role(Role.USER)
                .emailNotification(true)
                .phoneNotification(true)
                .phoneNumber("01012345678")
                .oauthId("oauthId1")
                .build();
    }
}
