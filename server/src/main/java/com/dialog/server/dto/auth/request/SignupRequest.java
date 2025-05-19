package com.dialog.server.dto.auth.request;

public record SignupRequest(
        String oauthId,
        String nickname,
        String email,
        String phoneNumber,
        boolean emailNotification,
        boolean phoneNotification
) {
}
