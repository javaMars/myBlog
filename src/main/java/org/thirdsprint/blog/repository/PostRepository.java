package org.thirdsprint.blog.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.thirdsprint.blog.model.Post;

public interface PostRepository extends JpaRepository<Post, Long> {

}
