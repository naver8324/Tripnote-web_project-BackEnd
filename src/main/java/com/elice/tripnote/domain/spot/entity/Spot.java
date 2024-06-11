package com.elice.tripnote.domain.spot.entity;

import com.elice.tripnote.domain.spot.constant.Region;
import jakarta.persistence.*;
import lombok.*;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "spot", uniqueConstraints = {@UniqueConstraint(columnNames = {"region", "location"})})
public class Spot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false)
    private String location;

    @Column(columnDefinition = "varchar(512)",nullable = true)
    private String imageUrl;

    @Enumerated(EnumType.STRING) // Enum 타입으로 지정
    @Column(nullable = false)
    private Region region; // 변경: String -> Region 열거형으로 타입 변경

    @Column(nullable = false)
    private String address;

    @Column(nullable = false)
    private double lat;

    @Column(nullable = false)
    private double lng;


    @Builder
    public Spot(String location, String imageUrl, Region region, String address, double lat, double lng){
        this.location=location;
        this.imageUrl=imageUrl;
        this.region=region;
        this.address=address;
        this.lat=lat;
        this.lng=lng;
    }
}
