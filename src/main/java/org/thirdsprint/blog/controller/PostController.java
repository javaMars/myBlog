package org.thirdsprint.blog.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.thirdsprint.blog.dto.PagingWrapper;
import org.thirdsprint.blog.model.Post;
import org.thirdsprint.blog.service.PostService;

@Controller
public class PostController {
    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @GetMapping("/posts")
    public String getPosts(@RequestParam(name = "pageNumber", defaultValue = "0") int pageNumber,
                           @RequestParam(name = "pageSize", defaultValue = "2") int pageSize,
                           @RequestParam(name = "search", required = false) String search,
                           Model model) {

        Pageable pageable = PageRequest.of(pageNumber, pageSize);
        Page<Post> postsPage;

        postsPage = postService.findAll(pageable);

        model.addAttribute("posts", postsPage.getContent());
        model.addAttribute("paging", new PagingWrapper<>(postsPage));
        model.addAttribute("search", search);

        return "posts";
    }
}
