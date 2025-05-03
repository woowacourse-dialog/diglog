package com.dialog.server.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import java.time.LocalDateTime;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "discussions")
@Entity
public class Discussion extends BaseEntity {

    @Column(name = "discussion_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String title;

    private String content;

    private LocalDateTime startAt;

    private LocalDateTime endAt;

    private String place;

    private int viewCount;

    private int participantCount;

    private int maxParticipantCount;

    private Category category;

    private String summary;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

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
                      User author) {
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
    }
}
