package org.example.sports.repositore;

import org.example.sports.model.entity.Executor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ExecutorRepository extends JpaRepository<Executor, String> {
}
