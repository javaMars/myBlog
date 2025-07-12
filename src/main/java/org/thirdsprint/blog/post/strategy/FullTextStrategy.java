package org.thirdsprint.blog.post.strategy;

import org.thirdsprint.blog.model.Post;

public class FullTextStrategy implements TextDisplayStrategy {
    @Override
    public String getText(Post post) {
        return post.getText();
    }
}
