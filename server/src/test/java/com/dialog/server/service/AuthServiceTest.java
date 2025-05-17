package com.dialog.server.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

import com.dialog.server.domain.User;
import com.dialog.server.dto.SignupRequest;
import com.dialog.server.exception.DialogException;
import com.dialog.server.repository.UserRepository;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private AuthService authService;

    private User registeredUser;
    private User unRegisteredUser;
    private final String testOauthId = "oauth123";
    private final String testEmail = "test@example.com";
    private final String testNickname = "testUser";
    private final String testPhoneNumber = "010-1234-5678";

    @BeforeEach
    void setUp() {
        registeredUser = User.builder()
                .oauthId(testOauthId)
                .nickname(testNickname)
                .email(testEmail)
                .phoneNumber(testPhoneNumber)
                .build();

        unRegisteredUser = User.builder()
                .oauthId(testOauthId)
                .email(testEmail)
                .build();
    }

    @Test
    @DisplayName("사용자 회원가입 성공")
    void registerUserTest() {
        // given
        SignupRequest signupRequest = new SignupRequest(
                testOauthId,
                testNickname,
                testEmail,
                testPhoneNumber,
                false,
                false
        );
        when(userRepository.findUserByOauthId(testOauthId)).thenReturn(Optional.of(unRegisteredUser));
        when(userRepository.existsUserByEmail(testEmail)).thenReturn(false);

        // when
        authService.registerUser(signupRequest);

        // then
        assertThat(unRegisteredUser.isRegistered()).isTrue();
    }

    @Test
    @DisplayName("이미 회원가입한 사용자라면 예외가 발생한다.")
    void alreadyRegisteredUserTest() {
        // given
        String unRegisteredEmail = "new-email@example.com";
        SignupRequest signupRequest = new SignupRequest(
                testOauthId,
                testNickname,
                unRegisteredEmail,
                testPhoneNumber,
                false,
                false
        );
        when(userRepository.findUserByOauthId(testOauthId)).thenReturn(Optional.of(registeredUser));
        when(userRepository.existsUserByEmail(unRegisteredEmail)).thenReturn(false);

        // when, then
        assertThatThrownBy(() -> authService.registerUser(signupRequest))
                .isInstanceOf(DialogException.class);
    }

    @Test
    @DisplayName("이미 존재하는 이메일이라면 예외가 발생한다.")
    void existEmailRegisterTest() {
        // given
        SignupRequest signupRequest = new SignupRequest(
                testOauthId,
                testNickname,
                testEmail,
                testPhoneNumber,
                false,
                false
        );
        when(userRepository.findUserByOauthId(testOauthId)).thenReturn(Optional.of(unRegisteredUser));
        when(userRepository.existsUserByEmail(testEmail)).thenReturn(true);

        // when, then
        assertThatThrownBy(() -> authService.registerUser(signupRequest))
                .isInstanceOf(DialogException.class);
    }

    @Test
    @DisplayName("깃허브 로그인을 하지 않고 회원가입 시도 시 예외가 발생한다")
    void notOAuthUserTest() {
        // given
        SignupRequest signupRequest = new SignupRequest(
                testOauthId,
                testNickname,
                testEmail,
                testPhoneNumber,
                false,
                false
        );
        when(userRepository.findUserByOauthId(testOauthId)).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> authService.registerUser(signupRequest))
                .isInstanceOf(DialogException.class);
    }

    @Test
    @DisplayName("임시 저장된 사용자의 정보를 반환한다.")
    void getTempUserEmailTest() {
        // given
        when(userRepository.findUserByOauthId(testOauthId)).thenReturn(Optional.of(registeredUser));

        // when
        String email = authService.getTempUserEmail(testOauthId);

        // then
        assertThat(email).isEqualTo(testEmail);
    }

    @Test
    @DisplayName("임시 저장되지 않은 사용자의 정보를 요청할 시 예외 발생")
    void noTempUserTest() {
        // given
        when(userRepository.findUserByOauthId(testOauthId)).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> authService.getTempUserEmail(testOauthId))
                .isInstanceOf(DialogException.class);
    }

    @Test
    @DisplayName("등록된 사용자의 인증이 성공적으로 이루어진다")
    void authenticateSuccessTest() {
        // given
        when(userRepository.findUserByOauthId(testOauthId)).thenReturn(Optional.of(registeredUser));

        // when
        Authentication authentication = authService.authenticate(testOauthId);

        // then
        assertThat(authentication.isAuthenticated()).isTrue();
        assertThat(authentication.getPrincipal()).isEqualTo(testOauthId);
        assertThat(authentication.getCredentials()).isNull();

        assertThat(authentication.getAuthorities()).hasSize(1);
    }

    @Test
    @DisplayName("존재하지 않는 OAuth ID로 인증 시 예외가 발생한다")
    void authenticateWithNonExistingUserTest() {
        // given
        when(userRepository.findUserByOauthId(testOauthId)).thenReturn(Optional.empty());

        // when, then
        assertThatThrownBy(() -> authService.authenticate(testOauthId))
                .isInstanceOf(DialogException.class);
    }

    @Test
    @DisplayName("등록되지 않은 사용자 인증 시 예외가 발생한다")
    void authenticateUnregisteredUserTest() {
        // given
        when(userRepository.findUserByOauthId(testOauthId)).thenReturn(Optional.of(unRegisteredUser));

        // when, then
        assertThatThrownBy(() -> authService.authenticate(testOauthId))
                .isInstanceOf(DialogException.class);
    }
}
