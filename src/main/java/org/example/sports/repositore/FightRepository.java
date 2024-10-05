package org.example.sports.repositore;

import org.example.sports.model.Fight;
import org.example.sports.model.enums.FightStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FightRepository extends JpaRepository<Fight, Long> {
    Page<Fight> findByExecutor_Username(String executorId, Pageable pageable);
    long countByExecutorUsernameAndStatus(String executorUsername, FightStatus status);

    long countByExecutorUsername(String executorUsername);
}
