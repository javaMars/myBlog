package org.thirdsprint.blog.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.thirdsprint.blog.model.Comment;
import org.thirdsprint.blog.model.Post;
import org.thirdsprint.blog.repository.CommentRepository;
import org.thirdsprint.blog.repository.PostRepository;

@Service
@Transactional
public class CommentServiceImpl implements CommentService{
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;

    public CommentServiceImpl(CommentRepository commentRepository, PostRepository postRepository) {
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
    }

    public void addComment(Long postId, String text){
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Пост с id:" + postId + " не найден"));

        Comment comment = new Comment();
        comment.setPost(post);
        comment.setText(text);
        commentRepository.save(comment);
    }

    public void updateComment(Long commentId, String text){
        commentRepository.updateTextById(commentId, text);
    }

    public void deleteComment(Long commentId){
        commentRepository.deleteById(commentId);
    }
}
