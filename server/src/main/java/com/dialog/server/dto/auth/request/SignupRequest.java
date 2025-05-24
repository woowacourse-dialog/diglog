package com.dialog.server.dto.auth.request;

import jakarta.validation.constraints.NotBlank;

public record SignupRequest(
        @NotBlank String nickname,
        @NotBlank String email,
        @NotBlank String phoneNumber,
        boolean emailNotification,
        boolean phoneNotification
) {
}
