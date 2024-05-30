package com.elice.tripnote.domain.spot.repository;

import com.elice.tripnote.domain.spot.entity.Spot;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


public interface SpotRepository extends JpaRepository<Spot, Long>, CustomSpotRepository  {
    List<Spot> findAll();

    Optional<Spot> findById(Long id);

    Optional<Spot> findByLocation(String location);
    List<Spot> findByRegion(String region);

    Page<Spot> findByRegion(String region, Pageable pageable);

}
