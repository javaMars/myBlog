package org.thirdsprint.blog.controller;

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
import org.thirdsprint.blog.dto.PagingWrapper;
import org.thirdsprint.blog.model.Post;
import org.thirdsprint.blog.service.PostService;

import java.io.IOException;

@Controller
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
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
        model.addAttribute("search", search);

        return "posts";
    }

    @GetMapping("/posts/add")
    public String newPostForm(Model model) {
        model.addAttribute("post", null);
        return "add-post";
    }

    @GetMapping("/posts/{id}")
    public String editPostForm(@PathVariable Long id, Model model) {
        Post post = postService.findPostById(id)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id: " + id));
        model.addAttribute("post", post);
        return "post";
    }

    @PostMapping("/posts")
    public String createPost(@RequestParam("title") String title,
                             @RequestParam("image") MultipartFile image,
                             @RequestParam("tags") String tags,
                             @RequestParam("text") String text) throws IOException {

        postService.createPost(title, image, tags, text);
        return "redirect:/posts";
    }

    @PostMapping("/posts/{id}")
    public String updatePost(@PathVariable Long id,
                             @RequestParam("title") String title,
                             @RequestParam("image") MultipartFile image,
                             @RequestParam("tags") String tags,
                             @RequestParam("text") String text) throws IOException {
        postService.updatePost(id, title, image, tags, text);
        return "redirect:/posts";
    }
}
