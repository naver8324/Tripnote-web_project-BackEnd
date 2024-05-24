package com.elice.tripnote.domain.post.repository;


import com.elice.tripnote.domain.post.entity.Post;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {

    Page<Post> findByIsDeletedIsFalse(Pageable pageable);
    Page<Post> findByMember_IdAndIsDeletedIsFalse(Long memberId, Pageable pageable);

    @Query("SELECT p FROM Post p JOIN p.likePosts lp JOIN lp.member m WHERE m.id = :memberId AND p.isDeleted = false AND lp.createdAt IS NOT NULL")
    Page<Post> findNotDeletedPostsByMemberIdWithLikes(Long memberId, Pageable pageable);


}
