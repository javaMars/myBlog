package org.thirdsprint.blog.service;

import org.springframework.stereotype.Service;
import org.thirdsprint.blog.repository.PostRepository;
import org.thirdsprint.blog.model.Post;

import java.util.List;

@Service
public class PostService {

    private final PostRepository postRepository;

    public PostService(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public List<Post> findAll() {
        return postRepository.findAll();
    }
}
