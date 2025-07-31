package ru.practicum.sprint4.spring.boot.blog.service;

public interface CommentService {

    void addComment(Long postId, String text);

    void updateComment(Long commentId, String text);

    void deleteComment(Long commentId);
}
