package ru.practicum.sprint4.spring.boot.blog.strategy;

import ru.practicum.sprint4.spring.boot.blog.model.Post;

public interface TextDisplayStrategy {
    String getText(Post post);
}
