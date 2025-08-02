import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;
import ru.practicum.sprint4.spring.boot.blog.model.Post;
import ru.practicum.sprint4.spring.boot.blog.model.Tag;
import ru.practicum.sprint4.spring.boot.blog.repository.PostRepository;
import ru.practicum.sprint4.spring.boot.blog.service.PostServiceImpl;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

@SpringBootTest
public class PostServiceImplTest {

    @Autowired
    private PostServiceImpl postServiceImpl;

    @MockBean
    private PostRepository postRepository;

    @BeforeEach
    public void setUp() {

        TestUtils.setField(postServiceImpl, "uploadDir", "target/test-uploads");
        TestUtils.setField(postServiceImpl, "uploadUrl", "/uploads/");

        List<Post> postList = Arrays.asList(new Post(), new Post());
        Page<Post> postPage = new PageImpl<>(postList);

        when(postRepository.findAll(any(Pageable.class))).thenReturn(postPage);
    }

    @Test
    public void testFindAllReturnsData_shouldReturnPageWithPosts() {
        Pageable pageable = PageRequest.of(0, 10);

        Page<Post> postsPage = postServiceImpl.findAll(pageable);

        assertNotNull(postsPage);
        assertFalse(postsPage.isEmpty());
        assertEquals(2, postsPage.getContent().size());
    }

    //region Создание поста
    @Test
    public void createPost_shouldSavePostWithTagsAndImage() throws IOException {
        MultipartFile image = mock(MultipartFile.class);
        when(image.isEmpty()).thenReturn(false);
        when(image.getOriginalFilename()).thenReturn("image.jpg");
        when(image.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[]{1, 2, 3}));

        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> {
            Post p = invocation.getArgument(0);
            if (p.getId() == null) {
                p.setId(1L);
            }
            return p;
        });

        String tagsText = "tag1, tag2";
        Post result = postServiceImpl.createPost("Заголовок", image, tagsText, "Текст поста");

        assertNotNull(result);
        assertEquals("Заголовок", result.getTitle());
        assertEquals("Текст поста", result.getText());

        List<Tag> tags = result.getTags();
        assertEquals(2, tags.size());
        assertTrue(tags.stream().anyMatch(t -> t.getName().equals("tag1")));
        assertTrue(tags.stream().anyMatch(t -> t.getName().equals("tag2")));
        assertTrue(tags.stream().allMatch(t -> t.getPost() == result));

        assertNotNull(result.getImagePath());
        assertTrue(result.getImagePath().startsWith("/uploads/1."),
                "Image path should start with /images/1. or /uploads/1., but was: " + result.getImagePath());
    }
    //endregion

    //region Редактирование поста
    @Test
    public void updatePost_shouldUpdateFieldsAndTags() throws IOException {
        Long postId = 1L;

        Post existingPost = new Post();
        existingPost.setId(postId);
        existingPost.setTitle("Old title");
        existingPost.setText("Old text");

        when(postRepository.findById(postId)).thenReturn(Optional.of(existingPost));
        when(postRepository.save(any(Post.class))).thenAnswer(invocation -> invocation.getArgument(0));

        MultipartFile image = mock(MultipartFile.class);
        when(image.isEmpty()).thenReturn(false);
        when(image.getOriginalFilename()).thenReturn("newimage.JPG");
        when(image.getInputStream()).thenReturn(new ByteArrayInputStream(new byte[]{1, 2, 3}));

        String newTitle = "New title";
        String newText = "New text";
        String newTags = "tag1, tag2";

        Post updatedPost = postServiceImpl.updatePost(postId, newTitle, image, newTags, newText);

        assertEquals(newTitle, updatedPost.getTitle());
        assertEquals(newText, updatedPost.getText());

        List<Tag> tags = updatedPost.getTags();
        assertEquals(2, tags.size());
        assertTrue(tags.stream().anyMatch(t -> t.getName().equals("tag1")));
        assertTrue(tags.stream().anyMatch(t -> t.getName().equals("tag2")));
        assertTrue(tags.stream().allMatch(t -> t.getPost() == updatedPost));

        assertNotNull(updatedPost.getImagePath());
        assertTrue(updatedPost.getImagePath().startsWith("/uploads/1."),"Image path should start with /images/1. or /uploads/1., but was: " + updatedPost.getImagePath());

        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, times(1)).save(existingPost);
    }
    //endregion

    //region Удаление поста
    @Test
    public void deletePost_shouldDeleteSuccessfully() {
        Long postId = 1L;
        Post post = new Post();
        post.setId(postId);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        doNothing().when(postRepository).delete(post);

        assertDoesNotThrow(() -> postServiceImpl.deletePost(postId));

        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, times(1)).delete(post);
    }
    //endregion

    //region Счетчик лайков
    @Test
    public void updatePostLike_shouldIncreaseLikesCount() {
        Long postId = 1L;
        Post post = new Post();
        post.setId(postId);
        post.setLikesCount(2L);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postRepository.save(post)).thenReturn(post);

        assertDoesNotThrow(() -> postServiceImpl.updatePostLike(postId, true));

        assertEquals(3, post.getLikesCount());
        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, times(1)).save(post);
    }

    @Test
    public void updatePostLike_shouldDecreaseLikesCount() {
        Long postId = 1L;
        Post post = new Post();
        post.setId(postId);
        post.setLikesCount(2L);

        when(postRepository.findById(postId)).thenReturn(Optional.of(post));
        when(postRepository.save(post)).thenReturn(post);

        assertDoesNotThrow(() -> postServiceImpl.updatePostLike(postId, false));

        assertEquals(1, post.getLikesCount());
        verify(postRepository, times(1)).findById(postId);
        verify(postRepository, times(1)).save(post);
    }
    //endregion
}