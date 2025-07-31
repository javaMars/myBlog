package ru.practicum.sprint4.spring.boot.blog.strategy;

import ru.practicum.sprint4.spring.boot.blog.model.Post;

public class FullTextStrategy implements TextDisplayStrategy {
    @Override
    public String getText(Post post) {
        return post.getText();
    }
}
