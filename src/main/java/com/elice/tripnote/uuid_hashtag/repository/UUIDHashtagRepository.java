package com.elice.tripnote.uuid_hashtag.repository;

import com.elice.tripnote.hashtag.entity.Hashtag;
import com.elice.tripnote.uuid_hashtag.entity.UUID_Hashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UUIDHashtagRepository extends JpaRepository<UUID_Hashtag, Long> {
    List<Hashtag> findByIntegratedRoute(String integratedRoute);
}
