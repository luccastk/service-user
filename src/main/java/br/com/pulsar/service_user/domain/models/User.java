package br.com.pulsar.service_user.domain.models;

import br.com.pulsar.service_user.domain.models.enums.RoleName;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "T_USERS")
@Data
public class User {

    @Id
    @Column(name = "PK_USER")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;
    @Column(name = "CD_USER")
    private UUID id = UUID.randomUUID();
    @Column(name = "NM_USER")
    private String name;
    @Column(name = "EM_USER")
    private String email;
    @Column(name = "PW_USER")
    private String password;
    @Column(name = "RL_USER")
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "T_USERS_ROLES",
            joinColumns = @JoinColumn(name = "USER_ID"),
            inverseJoinColumns = @JoinColumn(name = "ROLE_ID")
    )
    private List<Role> roles;
}
