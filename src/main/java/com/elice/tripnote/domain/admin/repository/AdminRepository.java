package com.elice.tripnote.domain.admin.repository;

import com.elice.tripnote.domain.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    Optional<Admin> findByLoginId(String email);
}
