package org.thirdsprint.blog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.NonNull;
import org.thirdsprint.blog.model.Post;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface PostRepository extends PagingAndSortingRepository<Post, Long> {

    @NonNull
    Page<Post> findAll(@NonNull Pageable pageable);

    @Query("SELECT DISTINCT p FROM Post p JOIN p.tags t WHERE t.name LIKE %:tag%")
    Page<Post> findByTagNameContaining(@Param("tag") String tag, Pageable pageable);

    Optional<Post> findById(Long id);

    Post save(Post post);
}
