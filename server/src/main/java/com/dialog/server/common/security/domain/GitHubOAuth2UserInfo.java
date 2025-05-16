package com.dialog.server.common.security.domain;

import java.util.Map;

public class GitHubOAuth2UserInfo {

    public static final String ID_KEY = "id";

    private final Map<String, Object> attributes;

    public GitHubOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getOAuthUserId() {
        return attributes.get(ID_KEY).toString(); // TODO: toString()이 아닌 (String)을 사용하도록 바꿔보기
    }

    public String getEmail() {
        return (String) attributes.get("email");
    }
}
