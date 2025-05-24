package com.dialog.server.service;

import com.dialog.server.domain.Role;
import com.dialog.server.domain.User;
import com.dialog.server.dto.auth.request.SignupRequest;
import com.dialog.server.exception.DialogException;
import com.dialog.server.exception.ErrorCode;
import com.dialog.server.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Transactional(readOnly = true)
@Service
public class AuthService {

    private final UserRepository userRepository;

    @Transactional
    public Long registerUser(SignupRequest signupRequest) {
        User user = userRepository.findUserByOauthId(signupRequest.oauthId())
                .orElseThrow(() -> new DialogException(ErrorCode.UNEXPECTED_ERROR)); // TODO : 적합한 예외 코드 생성
        if (signupRequest.email() != null && userRepository.existsUserByEmail(signupRequest.email())) {
            throw new DialogException(ErrorCode.UNEXPECTED_ERROR); // TODO : 적합한 예외 코드 생성
        }
        if (user.isRegistered()) {
            throw new DialogException(ErrorCode.UNEXPECTED_ERROR); // TODO : 적합한 예외 코드 생성
        }

        user.register(
                signupRequest.nickname(),
                signupRequest.email(),
                signupRequest.phoneNumber(),
                signupRequest.emailNotification(),
                signupRequest.phoneNotification(),
                Role.USER
        );
        return user.getId();
    }

    public String getTempUserEmail(String oauthId) {
        User user = userRepository.findUserByOauthId(oauthId)
                .orElseThrow(() -> new DialogException(ErrorCode.UNEXPECTED_ERROR));
        return user.getEmail();
    }

    public Authentication authenticate(String oauthId) {
        User user = userRepository.findUserByOauthId(oauthId)
                .orElseThrow(() -> new DialogException(ErrorCode.UNEXPECTED_ERROR));
        if (!user.isRegistered()) {
            throw new DialogException(ErrorCode.UNEXPECTED_ERROR); // TODO: 적합한 예외 코드 작성
        }

        return new UsernamePasswordAuthenticationToken(
                oauthId,
                null, // OAuth 로그인만 허용하므로 비밀번호 없음
                user.getRole().toAuthorities()
        );
    }
}
