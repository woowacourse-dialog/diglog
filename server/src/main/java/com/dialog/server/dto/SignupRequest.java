package com.dialog.server.dto;

public record SignupRequest(
        String oauthId,
        String nickname,
        String email,
        String phoneNumber,
        boolean emailNotification,
        boolean phoneNotification
) {
}
