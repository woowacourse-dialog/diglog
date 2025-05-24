package com.dialog.server.controller.handler;

import static com.dialog.server.controller.AuthController.OAUTH_ID_PARAM;

import com.dialog.server.dto.security.OAuth2UserPrincipal;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationSuccessHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class OAuth2SuccessHandler extends SimpleUrlAuthenticationSuccessHandler {

    private static final String SIGNUP_PATH = "/signup";
    private static final String LOGIN_PATH = "/signin";

    @Override
    public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
                                        Authentication authentication) throws IOException {
        OAuth2UserPrincipal principal = (OAuth2UserPrincipal) authentication.getPrincipal();

        String redirectPath = principal.user().isRegistered() ? SIGNUP_PATH : LOGIN_PATH;
        String redirectUri = UriComponentsBuilder.fromUriString(redirectPath)
                .queryParam(OAUTH_ID_PARAM, principal.getName())
                .build()
                .toUriString();

        getRedirectStrategy().sendRedirect(request, response, redirectUri);
    }
}
