package ru.practicum.sprint4.spring.boot.blog.controller;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.support.StandardServletMultipartResolver;
import ru.practicum.sprint4.spring.boot.blog.dto.PagingWrapper;
import ru.practicum.sprint4.spring.boot.blog.model.Post;
import ru.practicum.sprint4.spring.boot.blog.service.CommentService;
import ru.practicum.sprint4.spring.boot.blog.service.PostService;

import java.io.IOException;

@Controller
public class PostController {

    private final PostService postService;
    private final CommentService commentService;

    public PostController(PostService postService, CommentService commentService) {
        this.postService = postService;
        this.commentService = commentService;
    }

    @Bean
    public MultipartResolver multipartResolver() {
        return new StandardServletMultipartResolver();
    }

    @GetMapping("/posts")
    public String getPosts(@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                           @RequestParam(name = "pageSize", defaultValue = "2") int pageSize,
                           @RequestParam(name = "search", required = false) String search,
                           Model model) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Post> postsPage;

        if (search != null && !search.trim().isEmpty()) {
            postsPage = postService.findByTag(search.trim(), pageable);
        } else {
            postsPage = postService.findAll(pageable);
        }

        model.addAttribute("posts", postsPage.getContent());
        model.addAttribute("paging", new PagingWrapper<>(postsPage));
        model.addAttribute("search", search != null ? search : "");

        return "posts";
    }

    // Создание нового поста
    @PostMapping("/posts")
    public String createPost(@RequestParam("title") String title,
                             @RequestParam("image") MultipartFile image,
                             @RequestParam("tags") String tags,
                             @RequestParam("text") String text) throws IOException {

        postService.createPost(title, image, tags, text);
        return "redirect:/posts";
    }

    // Форма добавления нового поста
    @GetMapping("/posts/add")
    public String newPostForm(Model model) {
        model.addAttribute("post", null);
        return "add-post";
    }

    // Просмотр поста
    @GetMapping("/posts/{id}")
    public String viewPostForm(@PathVariable Long id, HttpServletRequest request, Model model) throws IllegalArgumentException {
        Post post = postService.findPostById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пост с id:" + id + " не найден"));
        model.addAttribute("post", post);
        model.addAttribute("contextPath", request.getContextPath());
        return "post";
    }

    // Форма для редактирования существующего поста
    @GetMapping("/posts/{id}/edit")
    public String editPostForm(@PathVariable Long id, Model model) {
        Post post = postService.findPostById(id)
                .orElseThrow(() -> new IllegalArgumentException("Пост с id:" + id + " не найден"));
        model.addAttribute("post", post);
        return "add-post"; // тот же шаблон, что и для добавления
    }

    // Обновление существующего поста
    @PostMapping("/posts/{id}")
    public String updatePost(@PathVariable("id") Long id,
                             @RequestParam("title") String title,
                             @RequestParam("image") MultipartFile image,
                             @RequestParam("tags") String tags,
                             @RequestParam("text") String text) throws IOException {
        postService.updatePost(id, title, image, tags, text);
        return "redirect:/posts/" + id;
    }

    // Удаление поста
    @PostMapping("/posts/{id}/delete")
    public String deletePost(@PathVariable("id") Long id) {
        postService.deletePost(id);
        return "redirect:/posts";
    }

    // Обработка лайка и дизлайка поста
    @PostMapping("/posts/{id}/like")
    public String likePost (
            @PathVariable Long id,
            @RequestParam("like") boolean likeValue) throws IllegalArgumentException {
        postService.updatePostLike(id, likeValue);

        return "redirect:/posts/" + id;
    }

    // Добавление комментария
    @PostMapping("/posts/{postId}/comments")
    public String addComment(@PathVariable Long postId,
                             @RequestParam("text") String text) {
        commentService.addComment(postId, text);
        return "redirect:/posts/" + postId;
    }

    // Редактирование комментария
    @PostMapping("/posts/{postId}/comments/{commentId}")
    public String updateComment(@PathVariable Long postId,
                                @PathVariable Long commentId,
                                @RequestParam("text") String text) {
        commentService.updateComment(commentId, text);
        return "redirect:/posts/" + postId;
    }

    // Удаление комментария
    @PostMapping("/posts/{postId}/comments/{commentId}/delete")
    public String deleteComment(@PathVariable Long postId,
                                @PathVariable Long commentId) {
        commentService.deleteComment(commentId);
        return "redirect:/posts/" + postId;
    }
}
