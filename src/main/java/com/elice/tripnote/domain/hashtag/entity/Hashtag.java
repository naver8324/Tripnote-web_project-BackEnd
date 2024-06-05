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
    private Long id;

    @Column(name = "name", nullable = false, unique = true)
    private String name;

    //0 - false
    //1 - true
    @Column(name = "is_city", columnDefinition = "TINYINT(1)")
    @ColumnDefault("false")
    private boolean isCity;

    @Column(name = "is_delete", columnDefinition = "TINYINT(1)")
    @ColumnDefault("false")
    private boolean isDelete;

    @Builder
    public Hashtag(String name, boolean isCity){
        this.name = name;
        this.isCity = isCity;
    }

    public void update(HashtagRequestDTO hashtagRequestDTO){
        this.name = hashtagRequestDTO.getName();
        this.isCity = hashtagRequestDTO.isCity();
    }

    //true -> false, false -> true
    //삭제된 해시태그를 복구할 수 있게
    public void delete(){
        this.isDelete = !this.isDelete;
    }

    public HashtagResponseDTO toResponseDTO() {
        return HashtagResponseDTO.builder()
                .id(id)
                .name(name)
                .isCity(isCity)
                .build();
    }

}
