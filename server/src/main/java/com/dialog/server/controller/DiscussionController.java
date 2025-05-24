package com.dialog.server.controller;

import com.dialog.server.dto.request.DiscussionCreateRequest;
import com.dialog.server.dto.request.DiscussionCursorPageRequest;
import com.dialog.server.dto.request.DiscussionUpdateRequest;
import com.dialog.server.dto.response.DiscussionCreateResponse;
import com.dialog.server.dto.response.DiscussionCursorPageResponse;
import com.dialog.server.dto.response.DiscussionDetailResponse;
import com.dialog.server.exception.ApiSuccessResponse;
import com.dialog.server.service.DiscussionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.net.URI;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/discussions")
public class DiscussionController {

    private final DiscussionService discussionService;

    @PostMapping
    public ResponseEntity<ApiSuccessResponse<DiscussionCreateResponse>> postDiscussion(@RequestBody @Valid DiscussionCreateRequest request) {
        // todo 인증 구현 끝나면 인증 정보에서 유저 정보 추출해서 사용
        final long FAKE_USER_ID = 1L;
        DiscussionCreateResponse response = discussionService.createDiscussion(request, FAKE_USER_ID);
        return ResponseEntity.created(URI.create("/api/discussions/" + response.discussionId()))
                .body(new ApiSuccessResponse<>(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse<DiscussionDetailResponse>> getDiscussion(@PathVariable Long id) {
        DiscussionDetailResponse response = discussionService.getDiscussionById(id);
        return ResponseEntity.ok().body(new ApiSuccessResponse<>(response));
    }

    @GetMapping
    public ResponseEntity<ApiSuccessResponse<DiscussionCursorPageResponse<DiscussionDetailResponse>>> getDiscussionsWithCursor(
            @RequestParam(required = false) String cursor,
            @RequestParam int size,
            @RequestParam String direction
    ) {
        DiscussionCursorPageRequest request = new DiscussionCursorPageRequest(cursor, size, direction);
        DiscussionCursorPageResponse<DiscussionDetailResponse> pageDiscussions = discussionService.getDiscussionsWithDateCursor(request);
        return ResponseEntity.ok().body(new ApiSuccessResponse<>(pageDiscussions));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse<Void>> updateDiscussion(@PathVariable Long id, DiscussionUpdateRequest request) {
        discussionService.updateDiscussion(id, request);
        return ResponseEntity.ok().body(new ApiSuccessResponse<>(null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse<Void>> deleteDiscussion(@PathVariable Long id) {
        discussionService.deleteDiscussion(id);
        return ResponseEntity.ok().body(new ApiSuccessResponse<>(null));
    }
}
