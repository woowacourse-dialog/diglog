package com.dialog.server.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
@Entity
public class User extends BaseEntity {

    @Column(name = "user_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;

    private String nickname;

    private String email;

    private String phoneNumber;

    private boolean emailNotification;

    private boolean phoneNotification;

    private boolean isDeleted;

    @Builder
    private User(String nickname,
                 String email,
                 String phoneNumber,
                 boolean emailNotification,
                 boolean phoneNotification) {
        this.nickname = nickname;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.emailNotification = emailNotification;
        this.phoneNotification = phoneNotification;
    }
}
