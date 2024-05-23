package com.elice.tripnote.uuid_hashtag.entity;

import com.elice.tripnote.hashtag.entity.Hashtag;
import com.elice.tripnote.integrated_route.entity.IntegratedRoute;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name="uuid_hashtag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class UUID_Hashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

//    @Column(name = "hashtag_id", nullable = false)
//    Long hashtag_id;

    @ManyToOne(optional = true)
    @JoinColumn(name = "hashtag_id", nullable = false)
    Hashtag hashtag;

    @ManyToOne
    @JoinColumn(name = "integerated_route_id", nullable = false)
    IntegratedRoute integratedRoute;
    @Builder
    public UUID_Hashtag(Hashtag hashtag, IntegratedRoute integratedRoute){
        this.hashtag=hashtag;
        this.integratedRoute=integratedRoute;
    }
}
