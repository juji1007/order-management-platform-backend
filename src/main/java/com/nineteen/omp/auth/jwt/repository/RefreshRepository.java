package com.nineteen.omp.auth.jwt.repository;

import com.nineteen.omp.auth.jwt.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;


public interface RefreshRepository extends CrudRepository<RefreshToken, Long> {

}
