package com.dialog.server.repository;

import com.dialog.server.domain.DiscussionParticipant;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DiscussionParticipantRepository extends JpaRepository<DiscussionParticipant, Long> {
}
