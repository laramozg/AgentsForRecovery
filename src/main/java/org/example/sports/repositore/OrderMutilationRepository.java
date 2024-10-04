package org.example.sports.repositore;

import org.example.sports.model.OrderMutilation;
import org.example.sports.model.OrderMutilationId;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OrderMutilationRepository extends JpaRepository<OrderMutilation, OrderMutilationId> {
    List<OrderMutilation> findByOrder_Id(Long orderId);
}
