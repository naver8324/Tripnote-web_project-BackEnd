package com.elice.tripnote.domain.member.repository;

import com.elice.tripnote.domain.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    Boolean existsByEmail(String email);
    Optional<Member> findByEmail(String email);
}
