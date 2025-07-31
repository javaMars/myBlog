package ru.practicum.sprint4.spring.boot.blog.repository;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.sprint4.spring.boot.blog.model.Comment;

public interface CommentRepository extends Repository<Comment, Long> {
    void save(Comment comment);

    void deleteById(Long commentId);

    @Modifying
    @Transactional
    @Query("UPDATE Comment c SET c.text = :text WHERE c.id = :commentId")
    void updateTextById(Long commentId, String text);
}
