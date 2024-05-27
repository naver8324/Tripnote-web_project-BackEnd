package com.elice.tripnote.domain.admin.entity;


import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "admin")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String login_id;

    @Column(nullable = false)
    private String password;

}
