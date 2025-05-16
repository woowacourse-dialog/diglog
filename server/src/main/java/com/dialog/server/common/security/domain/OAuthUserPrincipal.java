package com.dialog.server.common.security.domain;

import com.dialog.server.domain.User;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public record OAuthUserPrincipal(User user, Map<String, Object> attributes) implements OAuth2User {
    @Override
    public Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (user.getNickname() == null) { // TODO: User에 권한 관련 정보를 저장할 필요가 있음
            return List.of(new SimpleGrantedAuthority("TEMP"));
        }
        return List.of(new SimpleGrantedAuthority("ROLE_USER"));
    }

    @Override
    public String getName() {
        return user.getOauthId();
    }
}
