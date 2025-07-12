import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.thirdsprint.blog.model.Post;
import org.thirdsprint.blog.repository.PostRepository;
import org.thirdsprint.blog.service.PostService;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


public class PostServiceTest {

    private PostService postService;
    private PostRepository postRepository;

    @BeforeEach
    public void setUp() {
        // Создаем мок репозитория
        postRepository = Mockito.mock(PostRepository.class);

        // Настраиваем поведение мока: метод findAll() возвращает список постов
        when(postRepository.findAll()).thenReturn(Arrays.asList(new Post(), new Post()));

        // Создаем сервис вручную, передавая мок репозитория
        postService = new PostService(postRepository);
    }

    @Test
    public void testFindAllReturnsData() {
        List<Post> posts = postService.findAll();

        assertNotNull(posts);
        assertFalse(posts.isEmpty());
    }
}
