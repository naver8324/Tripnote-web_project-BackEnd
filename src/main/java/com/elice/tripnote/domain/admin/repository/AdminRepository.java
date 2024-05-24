package com.elice.tripnote.domain.admin.repository;

import com.elice.tripnote.domain.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
