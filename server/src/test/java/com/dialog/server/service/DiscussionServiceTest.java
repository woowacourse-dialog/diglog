package com.dialog.server.service;

import com.dialog.server.domain.Category;
import com.dialog.server.domain.Discussion;
import com.dialog.server.domain.User;
import com.dialog.server.dto.request.DiscussionCreateRequest;
import com.dialog.server.dto.request.DiscussionCursorPageRequest;
import com.dialog.server.dto.request.DiscussionUpdateRequest;
import com.dialog.server.dto.response.DiscussionCreateResponse;
import com.dialog.server.dto.response.DiscussionCursorPageResponse;
import com.dialog.server.dto.response.DiscussionDetailResponse;
import com.dialog.server.repository.DiscussionRepository;
import com.dialog.server.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import static org.assertj.core.api.Assertions.assertThat;

@Transactional
@ActiveProfiles("test")
@SpringBootTest
class DiscussionServiceTest {
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private DiscussionRepository discussionRepository;
    @Autowired
    private DiscussionService discussionService;

    @Test
    void 토론_게시글을_저장할_수_있다() {
        // given & when
        User savedUser = userRepository.save(createUser());
        DiscussionCreateResponse response = saveDiscussion(savedUser);
        // then
        assertThat(discussionRepository.findById(response.discussionId()).isPresent());
    }

    @Test
    void 토론_게시글을_단일_조회할_수_있다() {
        // given & when
        User savedUser = userRepository.save(createUser());
        DiscussionCreateResponse response = saveDiscussion(savedUser);
        // then
        assertThat(discussionRepository.findById(response.discussionId()).isPresent());
    }

    @Test
    void 토론_게시글을_삭제할_수_있다() {
        // given
        User savedUser = userRepository.save(createUser());
        DiscussionCreateResponse response = saveDiscussion(savedUser);
        // when
        discussionService.deleteDiscussion(response.discussionId());
        Discussion discussion = discussionRepository.findById(1L).orElseThrow();
        // then
        assertThat(discussion.getDeletedAt()).isNotNull();
    }

    @Test
    void 토론_게시글을_수정할_수_있다() {
        // given
        User savedUser = userRepository.save(createUser());
        DiscussionCreateResponse response = saveDiscussion(savedUser);
        DiscussionUpdateRequest request = new DiscussionUpdateRequest(
                "modified title",
                "test content",
                LocalDateTime.now(),
                LocalDateTime.now().plusMinutes(30),
                "test place",
                6,
                Category.BACKEND,
                "test summary"
        );
        // when
        discussionService.updateDiscussion(response.discussionId(), request);
        DiscussionDetailResponse modifiedDiscussion = discussionService.getDiscussionById(response.discussionId());
        // then
        assertThat(modifiedDiscussion.title()).isEqualTo(request.title());
    }

    @Test
    void 페이지에_맞는_토론_게시글_목록을_가져올_수_있다() {
        // given
        User user = userRepository.save(createUser());
        int totalCount = 20;
        int pageSize = 5;

        // 여러 토론 게시글 생성 (시간 간격을 두고)
        for (int i = 0; i < totalCount; i++) {
            DiscussionCreateRequest request = createDiscussionRequest(
                    "테스트 제목 " + (i + 1),
                    "테스트 내용입니다",
                    LocalDateTime.now().plusHours(1),
                    LocalDateTime.now().plusHours(2),
                    "테스트 장소",
                    5,
                    Category.BACKEND,
                    "테스트 요약"
            );

            DiscussionCreateResponse response = discussionService.createDiscussion(request, user.getId());

            // 생성 시간에 차이를 두기 위해 약간의 지연 추가
            try {
                Thread.sleep(10);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }

        // when
        // 첫 번째 페이지 조회 (cursor 없음)
        DiscussionCursorPageRequest firstPageRequest = new DiscussionCursorPageRequest(null, pageSize, "next");
        DiscussionCursorPageResponse<DiscussionDetailResponse> firstPage =
                discussionService.getDiscussionsWithDateCursor(firstPageRequest);

        // 다음 페이지 조회 (첫 페이지의 nextCursor 사용)
        DiscussionCursorPageRequest secondPageRequest =
                new DiscussionCursorPageRequest(firstPage.nextCursor(), pageSize, "next");
        DiscussionCursorPageResponse<DiscussionDetailResponse> secondPage =
                discussionService.getDiscussionsWithDateCursor(secondPageRequest);

        // 이전 페이지로 돌아가기 (두 번째 페이지의 prevCursor 사용)
        DiscussionCursorPageRequest backToFirstPageRequest =
                new DiscussionCursorPageRequest(secondPage.prevCursor(), pageSize, "prev");
        DiscussionCursorPageResponse<DiscussionDetailResponse> backToFirstPage =
                discussionService.getDiscussionsWithDateCursor(backToFirstPageRequest);

        // then
        // 첫 번째 페이지 검증
        assertThat(firstPage.content()).hasSize(pageSize);
        assertThat(firstPage.hasNext()).isTrue();
        assertThat(firstPage.hasPrev()).isFalse();
        assertThat(firstPage.nextCursor()).isNotNull();
        assertThat(firstPage.prevCursor()).isNull();

        // 두 번째 페이지 검증
        assertThat(secondPage.content()).hasSize(pageSize);
        assertThat(secondPage.hasNext()).isTrue();
        assertThat(secondPage.hasPrev()).isTrue();
        assertThat(secondPage.nextCursor()).isNotNull();
        assertThat(secondPage.prevCursor()).isNotNull();

        // 첫 번째 페이지로 돌아가기 검증
        assertThat(backToFirstPage.content()).hasSize(pageSize);
        assertThat(backToFirstPage.content().get(0).title()).isEqualTo(firstPage.content().get(0).title());

        // 중복 없이 순서대로 정렬되었는지 확인
        List<Long> firstPageIds = firstPage.content().stream()
                .map(DiscussionDetailResponse::id)
                .collect(Collectors.toList());
        List<Long> secondPageIds = secondPage.content().stream()
                .map(DiscussionDetailResponse::id)
                .collect(Collectors.toList());

        // 두 페이지 간에 중복이 없는지 확인
        boolean hasOverlap = firstPageIds.stream().anyMatch(secondPageIds::contains);
        assertThat(hasOverlap).isFalse();

        // 정렬 순서 확인 (최신순)
        assertThat(firstPageIds.get(0)).isGreaterThan(firstPageIds.get(firstPageIds.size() - 1));
        assertThat(secondPageIds.get(0)).isGreaterThan(secondPageIds.get(secondPageIds.size() - 1));

        // 첫 페이지의 마지막 ID가 두 번째 페이지의 첫 ID보다 큰지 확인
        assertThat(firstPageIds.get(firstPageIds.size() - 1))
                .isGreaterThan(secondPageIds.get(0));

        // 마지막 페이지 테스트
        String lastCursor = secondPage.nextCursor();
        int remainingCount = totalCount - (2 * pageSize);

        for (int i = 0; i < (remainingCount / pageSize); i++) {
            DiscussionCursorPageRequest nextPageRequest =
                    new DiscussionCursorPageRequest(lastCursor, pageSize, "next");
            DiscussionCursorPageResponse<DiscussionDetailResponse> nextPage =
                    discussionService.getDiscussionsWithDateCursor(nextPageRequest);

            assertThat(nextPage.content()).hasSize(
                    i == (remainingCount / pageSize) - 1 && remainingCount % pageSize != 0 ?
                            remainingCount % pageSize : pageSize);

            if (i < (remainingCount / pageSize) - 1 || remainingCount % pageSize != 0) {
                assertThat(nextPage.hasNext()).isTrue();
                lastCursor = nextPage.nextCursor();
            } else {
                assertThat(nextPage.hasNext()).isFalse();
            }
        }
    }

    private DiscussionCreateRequest createDiscussionRequest(
            String title,
            String content,
            LocalDateTime startAt,
            LocalDateTime endAt,
            String place,
            int maxParticipantCount,
            Category category,
            String summary) {
        return new DiscussionCreateRequest(
                title,
                content,
                startAt,
                endAt,
                place,
                maxParticipantCount,
                category,
                summary
        );
    }

    private List<DiscussionCreateRequest> createDiscussionsRequestWithParameters(
            int amount,
            String titlePrefix,
            String content,
            LocalDateTime startTime,
            LocalDateTime endTime,
            String place,
            int capacity,
            Category category,
            String summary
    ) {
        return IntStream.range(0, amount)
                .mapToObj(i -> new DiscussionCreateRequest(
                        titlePrefix + (i + 1),
                        content,
                        startTime,
                        endTime,
                        place,
                        capacity,
                        category,
                        summary
                ))
                .toList();
    }

    private User createUser() {
        return User.builder()
                .nickname("test")
                .phoneNumber("010-3275-1107")
                .emailNotification(true)
                .phoneNotification(false)
                .build();
    }

    private DiscussionCreateResponse saveDiscussion(User savedUser) {
        List<DiscussionCreateRequest> request = createDiscussionsRequestWithParameters(
                1,
                "modified title",
                "test content",
                LocalDateTime.now().plusMinutes(15),
                LocalDateTime.now().plusMinutes(30),
                "test place",
                6,
                Category.BACKEND,
                "test summary");
        return discussionService.createDiscussion(request.getFirst(), savedUser.getId());
    }
}
