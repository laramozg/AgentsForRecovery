package org.example.sports.repositore;

import org.example.sports.model.Mutilation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Set;

@Repository
public interface MutilationRepository extends JpaRepository<Mutilation, Long> {
    List<Mutilation> findAllByIdIn(List<Long> ids);
}
