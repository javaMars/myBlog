package org.thirdsprint.blog.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thirdsprint.blog.model.Tag;
import org.thirdsprint.blog.repository.PostRepository;
import org.thirdsprint.blog.model.Post;

import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Transactional
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    @Value("${app.upload.dir}")
    private String uploadDir;

    @Value("${app.upload.url}")
    private String uploadUrl;

    public PostServiceImpl(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Page<Post> findAll(Pageable pageable) {
        return postRepository.findAll(pageable);
    }

    public Page<Post> findByTag(String tag, Pageable pageable) {
        return postRepository.findByTagNameContaining(tag, pageable);
    }

    public Optional<Post> findPostById(Long id) {
        return postRepository.findById(id);
    }

    @Override
    public Post createPost(String title, MultipartFile image, String tagsText, String text) throws IOException {
        Post post = new Post();
        post.setTitle(title);
        post.setText(text);

        List<Tag> tags = Arrays.stream(tagsText.split(","))
                .map(String::trim)
                .filter(s -> !s.isEmpty())
                .map(name -> {
                    Tag tag = new Tag();
                    tag.setName(name);
                    tag.setPost(post);
                    return tag;
                })
                .toList();

        post.getTags().addAll(tags);

        Post savedPost = postRepository.save(post);

        if (image != null && !image.isEmpty()) {
            String originalFilename = image.getOriginalFilename();
            String extension = "";

            if (originalFilename != null && originalFilename.contains(".")) {
                extension = originalFilename.substring(originalFilename.lastIndexOf(".") + 1).toLowerCase();
            }

            if (!(extension.equals("jpg") || extension.equals("jpeg"))) {
                throw new IllegalArgumentException("Только файлы формата JPG/JPEG разрешены");
            }

            String filename = savedPost.getId() + "." + extension;
            saveImageFile(filename, image);
            savedPost.setImagePath(uploadUrl + filename);

            savedPost = postRepository.save(post);
        }

        return savedPost;
    }

    @Override
    public Post updatePost(Long id, String title, MultipartFile image, String tagsText, String text) throws IOException, IllegalArgumentException {
        Optional<Post> optionalPost = postRepository.findById(id);
        Post post = optionalPost.orElseThrow(() -> new IllegalArgumentException("Пост с id:" + id + " не найден"));

        post.setTitle(title);
        post.setText(text);

        if (image != null && !image.isEmpty()) {

            String originalFilename = image.getOriginalFilename();
            String extension = originalFilename.substring(originalFilename.lastIndexOf(".")).toLowerCase();

            extension = extension.startsWith(".") ? extension.substring(1) : extension;
            if (!(extension.equalsIgnoreCase("jpg") || extension.equalsIgnoreCase("jpeg"))) {
                throw new IllegalArgumentException("Только файлы формата JPG/JPEG разрешены");
            }

            String filename = post.getId() + extension;
            saveImageFile(filename, image);
            post.setImagePath(uploadUrl + filename);
        }

        // Обработка тегов
        List<Tag> tags;

        if (tagsText == null || tagsText.trim().isEmpty()) {
            tags = new ArrayList<>();  // Пустой список тегов
        } else {
            tags = Arrays.stream(tagsText.split(","))
                    .map(String::trim)
                    .filter(s -> !s.isEmpty())
                    .map(name -> {
                        Tag tag = new Tag();
                        tag.setName(name);
                        tag.setPost(post);
                        return tag;
                    })
                    .toList();
        }

        // Очищаем старые теги и добавляем новые
        post.getTags().clear();
        post.getTags().addAll(tags);

        return postRepository.save(post);
    }

    public void deletePost(Long id) {
        Optional<Post> optionalPost = postRepository.findById(id);
        Post post = optionalPost.orElseThrow(() -> new IllegalArgumentException("Пост с id:" + id + " не найден"));
        postRepository.delete(post);
    }

    private void saveImageFile(String filename, MultipartFile image) throws IOException {
        Path uploadPath = Paths.get(uploadDir);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }
        Files.copy(image.getInputStream(), uploadPath.resolve(filename), StandardCopyOption.REPLACE_EXISTING);
    }

    public void updatePostLike(Long id, boolean like) throws IllegalArgumentException {
        Optional<Post> optionalPost = postRepository.findById(id);
        Post post = optionalPost.orElseThrow(() -> new IllegalArgumentException("Пост с id:" + id + " не найден"));

        if (like) {
            post.setLikesCount(post.getLikesCount() + 1);
        } else {
            post.setLikesCount(post.getLikesCount() - 1);
        }

        postRepository.save(post);
    }
}
