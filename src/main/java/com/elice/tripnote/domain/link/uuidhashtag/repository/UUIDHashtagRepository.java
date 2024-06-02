package com.elice.tripnote.domain.link.uuidhashtag.repository;

import com.elice.tripnote.domain.link.uuidhashtag.entity.UUIDHashtag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UUIDHashtagRepository extends JpaRepository<UUIDHashtag, Long> {
    List<UUIDHashtag> findByIntegratedRoute_IntegratedRoutes(String integratedRoutes);


    @Query("SELECT uh.id FROM IntegratedRoute ir JOIN ir.uuidHashtags uh WHERE ir.id = :integratedRouteId")
    List<Long> findHashtagIdsByIntegratedRouteId(@Param("integratedRouteId") Long integratedRouteId);
}
