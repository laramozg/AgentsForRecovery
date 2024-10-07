package org.example.sports.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "mutilations")
public class Mutilation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String type;

    @Column(nullable = false)
    private Integer price;

    @ManyToMany(mappedBy = "mutilations", fetch = FetchType.LAZY)
    private Set<Order> orders = new HashSet<>();
}
