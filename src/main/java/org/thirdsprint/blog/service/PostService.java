package org.thirdsprint.blog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import org.thirdsprint.blog.model.Post;

import java.io.IOException;
import java.util.Optional;

public interface PostService {
    Page<Post> findByTag(String trim, Pageable pageable);

    Page<Post> findAll(Pageable pageable);

    Optional<Post> findPostById(Long id);

    Post createPost(String title, MultipartFile image, String tags, String text) throws IOException;

    Post updatePost(Long id, String title, MultipartFile image, String tags, String text) throws IOException, IllegalArgumentException;

    void updatePostLike(Long id, boolean like) throws IllegalArgumentException;

    void deletePost(Long id);
}
