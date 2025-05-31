package com.dialog.server.repository;

import com.dialog.server.domain.Discussion;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;

public interface DiscussionCustomRepository {

    List<Discussion> findByTitleOrContentContainingPageable(String keyword, Pageable pageable);

    List<Discussion> findByTitleOrContentContainingBeforeDateCursor(String keyword, LocalDateTime cursor, Long id, int limit);

    List<Discussion> findByAuthorNicknameContainingPageable(String nickname, Pageable pageable);

    List<Discussion> findByAuthorNicknameContainingBeforeDateCursor(String nickname, LocalDateTime cursor, Long id, int limit);
}
