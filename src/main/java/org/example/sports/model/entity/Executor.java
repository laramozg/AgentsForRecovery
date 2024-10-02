package org.example.sports.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "executors")
public class Executor {
    @Id
    private String username;

    @Column(name = "passport_series_number", nullable = false)
    private String passportSeriesNumber;

    @Column(nullable = false)
    private Double weight;

    @Column(nullable = false)
    private Double height;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private Double rating;

    @Column(name = "completed_orders", columnDefinition = "integer default 0", nullable = false)
    private Integer completedOrders;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;
}
