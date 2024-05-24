package com.elice.tripnote.domain.hashtag.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.ColumnDefault;

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

    @Column(name = "is_city")
    String isCity;

    @PrePersist
    private void prePersist() {
        this.isCity = this.isCity == null ? "N" : this.isCity;
    }

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
