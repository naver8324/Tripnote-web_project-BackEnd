package com.elice.tripnote.domain.link.uuidhashtag.repository;

import com.elice.tripnote.domain.link.uuidhashtag.entity.UUIDHashtag;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UUIDHashtagRepository extends JpaRepository<UUIDHashtag, Long> {
    List<UUIDHashtag> findByIntegratedRoute_IntegratedRoutes(String integratedRoutes);
}
