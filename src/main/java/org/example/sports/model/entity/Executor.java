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
    private double weight;

    @Column(nullable = false)
    private double height;

    @Column(columnDefinition = "integer default 0", nullable = false)
    private double rating;

    @Column(name = "completed_orders", columnDefinition = "integer default 0", nullable = false)
    private int completedOrders;

    @OneToOne
    @JoinColumn(name = "username", referencedColumnName = "username")
    @OnDelete(action = OnDeleteAction.CASCADE)
    private User user;
}
