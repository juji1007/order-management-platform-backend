package com.nineteen.omp.ai.repository;

import com.nineteen.omp.ai.domain.AiRequestHistory;
import java.util.UUID;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AiRequestHistoryRepository extends JpaRepository<AiRequestHistory, UUID> {

}