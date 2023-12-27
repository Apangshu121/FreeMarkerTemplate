package com.example.demo.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "users")
@ToString
public class User{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @Column(name = "userId")
    private String userId;

    private String password;

    private boolean approved;

    private String name;

    @Enumerated(EnumType.STRING)
    private Role role;

    @Column(name = "offlinePaymentsEnabled")
    private boolean offlinePaymentsEnabled;

    @Column(name = "gpsLocationX")
    private double gpsLocationX;

    @Column(name = "gpsLocationY")
    private double gpsLocationY;

}
