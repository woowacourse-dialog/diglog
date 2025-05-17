package com.dialog.server.repository;

import com.dialog.server.domain.Discussion;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DiscussionRepository extends JpaRepository<Discussion, Long> {
    List<Discussion> findTopNByOrderByCreatedAtAsc(long size);

    List<Discussion> findTopNByIdLessThanOrderByCreatedAtAsc(Long Id, int size);
}
