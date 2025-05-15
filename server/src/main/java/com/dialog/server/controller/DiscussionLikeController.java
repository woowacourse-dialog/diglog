package com.dialog.server.controller;

import com.dialog.server.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/discussions/{discussionsId}/likes")
@RequiredArgsConstructor
public class DiscussionLikeController {

    private static final Long FAKE_USER_ID = 1L;

    private final LikeService likeService;

    @PostMapping
    public ResponseEntity<Void> likeDiscussion(@PathVariable("discussionsId") Long discussionsId) {
        likeService.create(FAKE_USER_ID, discussionsId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .build();
    }
}
