package com.elice.tripnote.domain.member.repository;

import com.elice.tripnote.domain.member.entity.Member;
import com.elice.tripnote.domain.member.entity.MemberResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long>, MemberCustomRepository {

    Boolean existsByEmail(String email);

    Boolean existsByNickname(String nickname);

    Optional<Member> findByEmail(String email);
    Optional<Member> findByOauthId(Long oauthId);
    Optional<Member> findByOauthIdOrEmail(Long oauthId, String email);

    @Query("SELECT m.id FROM Member m WHERE m.email = :email")
    Optional<Long> findIdByEmail(@Param("email") String email);

    @Modifying
    @Query("UPDATE Member m SET m.password = :newPassword WHERE m.email = :email")
    void updatePassword(@Param("email") String email, @Param("newPassword") String newPassword);

    @Query("SELECT m.password FROM Member m WHERE m.email = :email")
    String findPasswordByEmail(@Param("email") String email);

    Member getByEmail(String email);

    @Query("SELECT m.oauthId FROM Member m WHERE m.email = :email")
    Optional<Long> findOauthIdByEmail(@Param("email") String email);
}
