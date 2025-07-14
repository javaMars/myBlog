package org.thirdsprint.blog.service;

import org.thirdsprint.blog.model.Post;

public interface CommentService {

    void addComment(Long postId, String text);

    void updateComment(Long commentId, String text);

    void deleteComment(Long commentId);
}
