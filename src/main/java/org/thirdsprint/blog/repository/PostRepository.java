package org.thirdsprint.blog.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.lang.NonNull;
import org.thirdsprint.blog.model.Post;

public interface PostRepository extends PagingAndSortingRepository<Post, Long> {

    @NonNull
    Page<Post> findAll(@NonNull Pageable pageable);
}
