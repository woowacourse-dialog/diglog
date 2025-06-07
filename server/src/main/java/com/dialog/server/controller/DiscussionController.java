package com.dialog.server.controller;

import com.dialog.server.dto.request.DiscussionCreateRequest;
import com.dialog.server.dto.request.DiscussionCursorPageRequest;
import com.dialog.server.dto.request.DiscussionUpdateRequest;
import com.dialog.server.dto.request.SearchType;
import com.dialog.server.dto.response.DiscussionCreateResponse;
import com.dialog.server.dto.response.DiscussionCursorPageResponse;
import com.dialog.server.dto.response.DiscussionDetailResponse;
import com.dialog.server.dto.response.DiscussionSlotResponse;
import com.dialog.server.exception.ApiSuccessResponse;
import com.dialog.server.service.DiscussionService;
import jakarta.validation.Valid;
import java.net.URI;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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
    public ResponseEntity<ApiSuccessResponse<DiscussionCursorPageResponse<DiscussionSlotResponse>>> getDiscussionsWithCursor(
            @RequestParam(name = "name", required = false) String cursor,
            @RequestParam(name = "size") int size
    ) {
        DiscussionCursorPageRequest request = new DiscussionCursorPageRequest(cursor, size);
        DiscussionCursorPageResponse<DiscussionSlotResponse> pageDiscussions = discussionService.getDiscussionsPage(request);
        return ResponseEntity.ok().body(new ApiSuccessResponse<>(pageDiscussions));
    }

    @GetMapping("/search")
    public ResponseEntity<ApiSuccessResponse<DiscussionCursorPageResponse<DiscussionSlotResponse>>> searchDiscussions(
            @RequestParam int searchBy,
            @RequestParam String query,
            @RequestParam(required = false) String cursor,
            @RequestParam(required = false, defaultValue = "10") int size
    ) {
        final DiscussionCursorPageResponse<DiscussionSlotResponse> searched = discussionService.searchDiscussion(
                SearchType.fromValue(searchBy), query, cursor, size
        );
        return ResponseEntity.ok().body(new ApiSuccessResponse<>(searched));
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
