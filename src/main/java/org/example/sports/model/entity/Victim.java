package org.example.sports.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "victims")
public class Victim {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String firstName;

    @Column(nullable = false)
    private String lastName;

    @Column
    private String workplace;

    @Column
    private String position;

    @Column
    private String residence;

    @Column
    private String phone;

    @Column(columnDefinition = "TEXT")
    private String description;
}
