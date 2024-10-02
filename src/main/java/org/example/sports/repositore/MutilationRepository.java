package org.example.sports.repositore;

import org.example.sports.model.entity.Mutilation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MutilationRepository extends JpaRepository<Mutilation, Long> {
}
