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

    @Column(name = "name", nullable = false, unique = true)
    String name;

    @Column(name = "is_city", nullable = false)
    @ColumnDefault("N")
    String isCity;

//    @OneToMany(fetch = FetchType.LAZY, mappedBy = "hashtag")
//    @JsonIgnore
//    List<UUID_Hashtag> uuidHashtags = new ArrayList<>();

    @Builder
    public Hashtag(String name, String isCity){
        this.name = name;
        this.isCity = isCity;
    }

//    @Builder
//    public Hashtag(String name,List<UUID_Hashtag> uuidHashtags){
//        this.name = name;
//        this.uuidHashtags = uuidHashtags;
//    }

//    @Builder
//    public Hashtag(String name, String isCity,List<UUID_Hashtag> uuidHashtags){
//        this.name = name;
//        this.isCity = isCity;
//        this.uuidHashtags = uuidHashtags;
//    }

    public void update(HashtagRequestDTO hashtagRequestDTO){
        this.name = hashtagRequestDTO.getName();
        this.isCity = hashtagRequestDTO.getIsCity();
    }

    public HashtagResponseDTO toResponseDTO() {
        return HashtagResponseDTO.builder()
                .id(id)
                .name(name)
                .isCity(isCity)
                .build();
    }

}
