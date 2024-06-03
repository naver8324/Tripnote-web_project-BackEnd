package com.elice.tripnote.domain.spot.entity;

import com.elice.tripnote.domain.link.likeSpot.entity.LikeSpot;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "spot")
public class Spot {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(nullable = false)
    private String location; //주소명?

    @Column(nullable = false)
    private int likes;//누가 좋아요 눌렀는지에 대해 모르므로 무한대로 증가할 수가 있다. => 유지보수

    @Column(nullable = true)
    private String imageUrl;

    @Column(nullable = false)
    private String region; //지역

    @OneToMany(mappedBy = "spot")
    private List<LikeSpot> likespot = new ArrayList<>();

    @Builder
    public Spot(String location, int likes, String imageUrl, String region){
        this.location=location;
        this.likes=likes;
        this.imageUrl=imageUrl;
        this.region=region;
    }

    public void increaseLikes(){
        this.likes++;
    }

    public void decreaseLikes() {
        if (this.likes > 0) {
            this.likes--;
        }
    }

}
