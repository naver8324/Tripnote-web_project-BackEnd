package com.elice.tripnote.domain.post.repository;


import com.elice.tripnote.domain.post.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface PostRepository extends JpaRepository<Post, Long>, CustomPostRepository {
    Optional<Post> findByRouteId(Long routeId);

}
