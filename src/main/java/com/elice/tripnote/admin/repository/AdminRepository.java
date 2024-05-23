package com.elice.tripnote.admin.repository;

import com.elice.tripnote.admin.entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AdminRepository extends JpaRepository<Admin, Long> {
}
