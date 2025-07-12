package org.thirdsprint.blog.service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.thirdsprint.blog.post.strategy.TextDisplayStrategy;
import org.thirdsprint.blog.repository.PostRepository;
import org.thirdsprint.blog.model.Post;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAll(pageable);
    }
}
