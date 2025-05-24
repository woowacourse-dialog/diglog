package com.dialog.server.dto.security;

import com.dialog.server.exception.DialogException;
import com.dialog.server.exception.ErrorCode;
import java.util.Map;

public class GitHubOAuth2UserInfo {

    public static final String ID_PARAM = "id";
    public static final String EMAIL_PARAM = "email";

    private final Map<String, Object> attributes;

    public GitHubOAuth2UserInfo(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public String getOAuthUserId() {
        Object id = attributes.get(ID_PARAM);
        if (id == null) {
            throw new DialogException(ErrorCode.GITHUB_USER_ID_MISSING);
        }
        if (id instanceof Number) {
            return String.valueOf(((Number) id).longValue());
        }
        return id.toString();
    }

    public String getEmail() {
        Object email = attributes.get(EMAIL_PARAM);
        return email == null ? null : email.toString();
    }
}
