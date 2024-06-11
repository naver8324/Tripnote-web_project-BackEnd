package com.elice.tripnote.domain.post.repository;


import com.elice.tripnote.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long>, CustomPostRepository {
    Post findByRouteId(Long routeId);

}
