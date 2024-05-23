package com.elice.tripnote.spot.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "spot")
public class Spot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "spot_id")
    private Long id;

    @Column(nullable = false)
    private String location; //주소명?

    @Column(nullable = false)
    private int like;

    @Column(nullable = true)
    private String imageUrl;

    @Column(nullable = false)
    private String region; //지역
}
