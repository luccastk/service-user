package br.com.pulsar.service_user.domain.models;

import jakarta.persistence.*;
import lombok.Data;

import java.util.UUID;

@Entity
@Table(name = "T_EMPLOYEES")
@Data
public class Employee {

    @Id
    @Column(name = "PK_EMPLOYEE")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long pk;
    @Column(name = "CD_EMPLOYEE")
    private UUID id = UUID.randomUUID();
    @Column(name = "PS_EMPLOYEE")
    private String position;
    @Column(name = "DP_EMPLOYEE")
    private String departament;

    @OneToOne
    @MapsId
    @JoinColumn(name = "PK_EMPLOYEE")
    private User user;
}
