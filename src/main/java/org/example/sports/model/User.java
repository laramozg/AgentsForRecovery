package org.example.sports.model;

import jakarta.persistence.*;
import lombok.*;
import org.example.sports.model.enums.Role;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "users")
public class User {

    @Id
    private String username;
    @Column(nullable = false)
    private String nick;
    @Column(nullable = false)
    private String telegram;
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Role role;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private AuthorizationData authorizationData;
}
