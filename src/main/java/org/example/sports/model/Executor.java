package org.example.sports.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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

    @Column(columnDefinition = "integer default 0.0", nullable = false)
    private Double rating;

    @Column(name = "completed_orders", columnDefinition = "integer default 0", nullable = false)
    private Integer completedOrders;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @MapsId
    @JoinColumn(name = "username", referencedColumnName = "username")
    private User user;
}
