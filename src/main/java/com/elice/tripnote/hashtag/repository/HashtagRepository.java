package com.elice.tripnote.hashtag.repository;

import com.elice.tripnote.hashtag.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface HashtagRepository extends JpaRepository<Hashtag, Long> {

    @Query("SELECT h FROM Hashtag h WHERE h.isCity = 'Y'")
    List<Hashtag> findHashtagsByIsCity();

    @Query("SELECT h FROM Hashtag h WHERE h.isCity = 'N'")
    List<Hashtag> findHashtagsByIsNotCity();

}
