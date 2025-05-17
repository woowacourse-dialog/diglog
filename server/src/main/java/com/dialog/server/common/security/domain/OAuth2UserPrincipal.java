package com.dialog.server.common.security.domain;

import com.dialog.server.domain.User;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

public record OAuth2UserPrincipal(User user, Map<String, Object> attributes) implements OAuth2User {
    @Override
    public Map<String, Object> getAttributes() {
        return Collections.unmodifiableMap(attributes);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        if (!user.isRegistered()) {
            return List.of(new SimpleGrantedAuthority("TEMP"));
        }
        return List.of(new SimpleGrantedAuthority("ROLE_USER")); // TODO: 권한 설정 후 실제 권한을 담는 로직으로 대체
    }

    @Override
    public String getName() {
        return user.getOauthId();
    }
}
