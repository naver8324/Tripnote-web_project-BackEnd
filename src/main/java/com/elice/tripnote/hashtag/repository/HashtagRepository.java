package com.elice.tripnote.hashtag.repository;

import com.elice.tripnote.hashtag.entity.Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface HashtagRepository extends JpaRepository<Hashtag, Long> {
}
