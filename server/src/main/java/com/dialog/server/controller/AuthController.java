package com.dialog.server.controller;

import static org.springframework.security.web.context.HttpSessionSecurityContextRepository.SPRING_SECURITY_CONTEXT_KEY;

import com.dialog.server.dto.auth.request.SignInRequest;
import com.dialog.server.dto.auth.request.SignupRequest;
import com.dialog.server.dto.auth.response.SignupResponse;
import com.dialog.server.dto.auth.response.TempUserInfoResponse;
import com.dialog.server.exception.ApiSuccessResponse;
import com.dialog.server.service.AuthService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequestMapping("/api")
@RestController
public class AuthController {

    public static final String SESSION_PARAM = "JSESSIONID";
    public static final String OAUTH_ID_PARAM = "oid";

    private final AuthService authService;

    public AuthController(AuthService authService) {
        this.authService = authService;
    }

    @PostMapping("/signup")
    public ApiSuccessResponse<SignupResponse> signup(@RequestBody SignupRequest signupRequest) {
        Long userId = authService.registerUser(signupRequest);
        return new ApiSuccessResponse<>(new SignupResponse(userId));
    }

    @GetMapping("/signup/check")
    public ApiSuccessResponse<TempUserInfoResponse> checkTempUser(@RequestParam(value = OAUTH_ID_PARAM) String oauthId) {
        String email = authService.getTempUserEmail(oauthId);
        return new ApiSuccessResponse<>(new TempUserInfoResponse(email));
    }

    @PostMapping("/signin")
    public ApiSuccessResponse<Void> signIn(@RequestBody SignInRequest signInRequest, HttpServletRequest request) {
        Authentication authentication = authService.authenticate(signInRequest.oid());

        SecurityContextHolder.getContext().setAuthentication(authentication);

        HttpSession session = request.getSession(true);
        session.setAttribute(SPRING_SECURITY_CONTEXT_KEY, SecurityContextHolder.getContext());
        return new ApiSuccessResponse<>(null);
    }

    @DeleteMapping("/logout")
    public ApiSuccessResponse<Void> logout(@CookieValue(name = SESSION_PARAM, required = false) String sessionId,
                                           HttpServletRequest request,
                                           HttpServletResponse response) {
        SecurityContextHolder.clearContext();

        HttpSession session = request.getSession(false);
        if (session != null) {
            session.invalidate();
        }

        if (sessionId != null) {
            Cookie cookie = new Cookie(SESSION_PARAM, null);
            cookie.setPath("/");
            cookie.setMaxAge(0);
            cookie.setHttpOnly(true);
            response.addCookie(cookie);
        }
        return new ApiSuccessResponse<>(null);
    }
}
