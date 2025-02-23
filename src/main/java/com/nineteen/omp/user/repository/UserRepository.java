package com.nineteen.omp.user.repository;

import com.nineteen.omp.user.domain.User;
import java.util.Optional;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  boolean existsByUsername(String username);

  Page<User> findAllByNicknameContainsIgnoreCase(String nickname, Pageable pageable);

  Optional<User> findByUsername(String username);
}
