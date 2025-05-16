package com.dialog.server.repository;

import com.dialog.server.domain.Discussion;
import com.dialog.server.domain.Scrap;
import com.dialog.server.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ScrapRepository extends JpaRepository<Scrap, Long> {

    boolean existsByUserAndDiscussion(User user, Discussion discussion);

    void deleteByUserIdAndDiscussionId(Long userId, Long discussionId);
}
