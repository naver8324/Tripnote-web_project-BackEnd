package com.elice.tripnote.domain.spot.repository;

import com.elice.tripnote.domain.spot.constant.Region;
import com.elice.tripnote.domain.spot.entity.Spot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface SpotRepository extends JpaRepository<Spot, Long>, CustomSpotRepository  {
    List<Spot> findAll();

    Optional<Spot> findById(Long id);

    Optional<Spot> findByLocation(String location);
    List<Spot> findByRegion(Region region); // 변경

    Page<Spot> findByRegion(Region region, Pageable pageable);

    Page<Spot> findByLocation(String location, Pageable pageable);

    @Modifying
    @Query("DELETE FROM Spot s WHERE s.location = :location")
    Spot deleteByLocation(String location);


    @Query("SELECT s FROM Spot s WHERE s.region = :region AND s.location = :location")
    Spot findByRegionAndLocation(@Param("region") Region region, @Param("location") String location); // 변경
}