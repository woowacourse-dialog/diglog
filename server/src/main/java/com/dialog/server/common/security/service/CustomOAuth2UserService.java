package com.dialog.server.common.security.service;

import com.dialog.server.common.security.domain.GitHubOAuth2UserInfo;
import com.dialog.server.common.security.domain.OAuthUserPrincipal;
import com.dialog.server.domain.User;
import com.dialog.server.repository.UserRepository;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class CustomOAuth2UserService implements OAuth2UserService<OAuth2UserRequest, OAuth2User> {

    private final UserRepository userRepository;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        OAuth2UserService<OAuth2UserRequest, OAuth2User> defaultOAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = defaultOAuth2UserService.loadUser(userRequest);

        Map<String, Object> attributes = oAuth2User.getAttributes();
        GitHubOAuth2UserInfo userInfo = new GitHubOAuth2UserInfo(attributes);

        User user = userRepository.findUserByOauthId(userInfo.getOAuthUserId())
                .orElseGet(() -> saveTempUser(userInfo));

        return new OAuthUserPrincipal(user, attributes);
    }

    private User saveTempUser(GitHubOAuth2UserInfo oAuth2UserInfo) {
        User tempUser = User.builder()
                .oauthId(oAuth2UserInfo.getOAuthUserId())
                .email(oAuth2UserInfo.getEmail())
                .build();
        return userRepository.save(tempUser);
    }
}
