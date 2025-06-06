package com.dialog.server.repository;

import com.dialog.server.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByOauthId(String oauthId);

    boolean existsUserByEmail(String email);
}
