package com.elice.tripnote.hashtag.entity;

import com.elice.tripnote.uuid_hashtag.entity.UUID_Hashtag;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "hashtag")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Hashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    Long id;

    @Column(name = "name", nullable = false)
    String name;

    @Column(name = "is_city", nullable = false)
    @ColumnDefault("N")
    String is_city;

    @OneToMany(fetch = FetchType.LAZY, mappedBy = "hashtag")
    @JsonIgnore
    List<UUID_Hashtag> uuid_hashtags = new ArrayList<>();
    @Builder
    public Hashtag(String name,List<UUID_Hashtag> uuid_hashtags){
        this.name = name;
        this.uuid_hashtags=uuid_hashtags;
    }
    @Builder
    public Hashtag(String name, String is_city,List<UUID_Hashtag> uuid_hashtags){
        this.name = name;
        this.is_city=is_city;
        this.uuid_hashtags=uuid_hashtags;
    }

}
