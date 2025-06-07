package com.dialog.server.repository;

import com.dialog.server.domain.Discussion;
import com.dialog.server.domain.QDiscussion;
import com.dialog.server.domain.QUser;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

@RequiredArgsConstructor
@Repository
public class DiscussionCustomRepositoryImpl implements DiscussionCustomRepository {

    private final JPAQueryFactory queryFactory;
    private final QDiscussion discussion = QDiscussion.discussion;
    private final QUser user = QUser.user;

    @Override
    public List<Discussion> findByTitleOrContentContainingPageable(String keyword, Pageable pageable) {
        return queryFactory.selectFrom(discussion)
                .innerJoin(discussion.author, user)
                .fetchJoin()
                .where(
                        titleOrContentContains(keyword)
                )
                .orderBy(discussion.createdAt.desc(), discussion.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<Discussion> findByTitleOrContentContainingBeforeDateCursor(String keyword,
                                                                           LocalDateTime cursor,
                                                                           Long cursorId,
                                                                           int limit) {
        return queryFactory.selectFrom(discussion)
                .innerJoin(discussion.author, user)
                .fetchJoin()
                .where(
                        titleOrContentContains(keyword),
                        cursorBefore(cursor, cursorId)
                )
                .orderBy(discussion.createdAt.desc(), discussion.id.desc())
                .limit(limit)
                .fetch();
    }

    private BooleanExpression titleOrContentContains(String keyword) {
        if (!hasText(keyword)) {
            return null;
        }
        final String trimmed = keyword.trim();
        return discussion.title.containsIgnoreCase(trimmed)
                .or(discussion.content.containsIgnoreCase(trimmed));
    }

    @Override
    public List<Discussion> findByAuthorNicknameContainingPageable(String nickname, Pageable pageable) {
        return queryFactory.selectFrom(discussion)
                .innerJoin(discussion.author, user)
                .fetchJoin()
                .where(
                        nicknameContains(nickname)
                )
                .orderBy(discussion.createdAt.desc(), discussion.id.desc())
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();
    }

    @Override
    public List<Discussion> findByAuthorNicknameContainingBeforeDateCursor(String nickname,
                                                                           LocalDateTime cursor,
                                                                           Long cursorId,
                                                                           int limit) {
        return queryFactory.selectFrom(discussion)
                .innerJoin(discussion.author, user)
                .fetchJoin()
                .where(
                        nicknameContains(nickname),
                        cursorBefore(cursor, cursorId)
                )
                .orderBy(discussion.createdAt.desc(), discussion.id.desc())
                .limit(limit)
                .fetch();
    }

    private BooleanExpression nicknameContains(String nickname) {
        return nickname != null ? user.nickname.containsIgnoreCase(nickname) : null;
    }

    private boolean hasText(String text) {
        return text != null && !text.isBlank();
    }

    private BooleanExpression cursorBefore(LocalDateTime cursor, Long cursorId) {
        if (cursor == null || cursorId == null) {
            return null;
        }

        return discussion.createdAt.loe(cursor)
                .or(discussion.createdAt.eq(cursor).and(discussion.id.gt(cursorId)));
    }
}
