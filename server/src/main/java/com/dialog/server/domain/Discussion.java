package com.dialog.server.domain;

import com.dialog.server.dto.request.DiscussionUpdateRequest;
import com.dialog.server.exception.DialogException;
import com.dialog.server.exception.ErrorCode;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "discussions")
@Entity
public class Discussion extends BaseEntity {

    @Column(name = "discussion_id", nullable = false)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    @Column(nullable = false)
    private String title;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private LocalDateTime startAt;

    @Column(nullable = false)
    private LocalDateTime endAt;

    @Column(nullable = false)
    private String place;

    @Column(nullable = false)
    private int viewCount;

    @Column(nullable = false)
    private int participantCount;

    @Column(nullable = false)
    private int maxParticipantCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Category category;

    private String summary;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(nullable = false)
    private boolean isDeleted;

    @Builder
    public Discussion(String title,
                      String content,
                      LocalDateTime startAt,
                      LocalDateTime endAt,
                      String place,
                      int viewCount,
                      int participantCount,
                      int maxParticipantCount,
                      Category category,
                      String summary,
                      User author,
                      boolean isDeleted) {
        validate(startAt);
        this.title = title;
        this.content = content;
        this.startAt = startAt;
        this.endAt = endAt;
        this.place = place;
        this.viewCount = viewCount;
        this.participantCount = participantCount;
        this.maxParticipantCount = maxParticipantCount;
        this.category = category;
        this.summary = summary;
        this.author = author;
        this.isDeleted = isDeleted;
    }

    public void validate(LocalDateTime time) {
        if (time.isBefore(LocalDateTime.now())) {
            throw new DialogException(ErrorCode.UNEXPECTED_ERROR);
        }
    }

    public void update(DiscussionUpdateRequest request) {
        this.title = request.title();
        this.content = request.content();
        this.startAt = request.startAt();
        this.endAt = request.endAt();
        this.place = request.place();
        this.maxParticipantCount = request.maxParticipantCount();
        this.category = request.category();
        this.summary = request.summary();
    }

    public void delete() {
        isDeleted = true;
    }
}
