package com.nineteen.omp.user.repository;

import com.nineteen.omp.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {

  
  boolean existsByUsername(String username);


}
