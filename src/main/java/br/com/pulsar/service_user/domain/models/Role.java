package br.com.pulsar.service_user.domain.models;

import br.com.pulsar.service_user.domain.models.enums.RoleName;
import jakarta.persistence.*;
import lombok.Data;

@Entity
@Table(name = "T_ROLES")
@Data
public class Role {

    @Id
    @Column(name = "PK_ROLE")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "NM_ROLE")
    @Enumerated(EnumType.STRING)
    private RoleName name;
}
