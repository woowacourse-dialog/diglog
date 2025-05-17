package com.dialog.server.controller;

import com.dialog.server.dto.request.DiscussionCreateRequest;
import com.dialog.server.dto.request.DiscussionUpdateRequest;
import com.dialog.server.dto.response.DiscussionCreateResponse;
import com.dialog.server.dto.response.DiscussionDetailResponse;
import com.dialog.server.dto.response.DiscussionPageResponse;
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

    // todo: ApiSuccessResponse 는 불필요한 포장이라고 생각합니다. 사용하지 않는 방향으로 컨벤션을 번경하는 것은 어떨까요?
    @PostMapping
    public ResponseEntity<ApiSuccessResponse<DiscussionCreateResponse>> postDiscussion(@RequestBody @Valid DiscussionCreateRequest request) {
        final long FAKE_USER_ID = 1L;
        DiscussionCreateResponse response = discussionService.createDiscussion(request, FAKE_USER_ID);
        return ResponseEntity.created(URI.create("/discussions/" + response.discussionId()))
                .body(new ApiSuccessResponse<>(response));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse<DiscussionDetailResponse>> getDiscussions(@PathVariable Long id) {
        DiscussionDetailResponse response = discussionService.getDiscussionById(id);
        return ResponseEntity.ok().body(new ApiSuccessResponse<>(response));
    }

    @GetMapping
    public ResponseEntity<ApiSuccessResponse<DiscussionPageResponse>> getPaging(@RequestParam Long cursorId, @RequestParam int size) {
        DiscussionPageResponse pageDiscussions = discussionService.getPageDiscussions(cursorId, size);
        return ResponseEntity.ok().body(new ApiSuccessResponse(pageDiscussions));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse> updateDiscussion(@PathVariable Long id, DiscussionUpdateRequest request) {
        discussionService.updateDiscussion(id, request);
        return ResponseEntity.ok().body(new ApiSuccessResponse(null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiSuccessResponse> deleteDiscussion(@PathVariable Long id) {
        discussionService.deleteDiscussion(id);
        return ResponseEntity.ok().body(new ApiSuccessResponse(null));
    }
}
