package org.example.sports.model;

import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "order_mutilations")
@IdClass(OrderMutilationId.class)
public class OrderMutilation {

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", nullable = false)
    private Order order;

    @Id
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "mutilation_id", nullable = false)
    private Mutilation mutilation;
}
