package com.elice.tripnote.domain.admin.entity;


import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "admin")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
@Builder
public class Admin {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "login_id", length = 40, nullable = false, unique = true)
    private String loginId;

    @Column(nullable = false, length = 90)
    private String password;

}
