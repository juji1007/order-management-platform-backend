package com.nineteen.omp.user.repository;

import com.nineteen.omp.user.domain.User;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
  
  boolean existsByUsername(String username);

  Optional<User> findByUsername(String username);
}
