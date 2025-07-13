import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.thirdsprint.blog.model.Post;
import org.thirdsprint.blog.repository.PostRepository;
import org.thirdsprint.blog.service.PostServiceImpl;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import org.mockito.Mockito;
import static org.mockito.Mockito.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;

public class PostServiceImplTest {

    private PostServiceImpl postServiceImpl;
    private PostRepository postRepository;

    @BeforeEach
    public void setUp() {
        // Создаем мок репозитория
        postRepository = Mockito.mock(PostRepository.class);

        List<Post> postList = Arrays.asList(new Post(), new Post());
        Page<Post> postPage = new PageImpl<>(postList);

        // Настраиваем поведение мока: метод findAll() возвращает список постов
        when(postRepository.findAll(any(Pageable.class))).thenReturn(postPage);

        // Создаем сервис вручную, передавая мок репозитория
        postServiceImpl = new PostServiceImpl(postRepository);
    }

    @Test
    public void testFindAllReturnsData() {
        Pageable pageable = PageRequest.of(0, 10); // первая страница, 10 элементов

        Page<Post> postsPage = postServiceImpl.findAll(pageable);

        assertNotNull(postsPage);
        assertFalse(postsPage.isEmpty());
        assertEquals(2, postsPage.getContent().size());
    }
}
