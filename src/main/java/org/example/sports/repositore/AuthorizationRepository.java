package org.example.sports.repositore;

import org.example.sports.model.AuthorizationData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorizationRepository extends JpaRepository<AuthorizationData, String> {
}
