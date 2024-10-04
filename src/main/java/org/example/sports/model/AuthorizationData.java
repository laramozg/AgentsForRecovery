package org.example.sports.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.example.sports.model.enums.Role;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "auth_data")
public class AuthorizationData {
    @Id
    private String username;
    private String password;

    @PrimaryKeyJoinColumn(name = "username", referencedColumnName = "username")
    @OneToOne(fetch = FetchType.LAZY)
    private User user;

}
