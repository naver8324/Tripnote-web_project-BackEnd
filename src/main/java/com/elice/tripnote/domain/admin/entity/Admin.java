package com.elice.tripnote.domain.admin.entity;


import jakarta.persistence.*;

@Entity
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String login_id;

    @Column(nullable = false)
    private String password;

}
