package org.thirdsprint.blog.post.strategy;

import org.thirdsprint.blog.model.Post;

public interface TextDisplayStrategy {
    String getText(Post post);
}
