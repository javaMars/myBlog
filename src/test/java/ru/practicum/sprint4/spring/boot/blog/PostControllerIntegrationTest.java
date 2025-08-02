import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import ru.practicum.sprint4.spring.boot.blog.repository.PostRepository;
import ru.practicum.sprint4.spring.boot.blog.service.CommentService;
import ru.practicum.sprint4.spring.boot.blog.service.PostService;

@SpringBootTest
@AutoConfigureMockMvc
public class PostControllerIntegrationTest {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private MockMvc mockMvc;

    @BeforeEach
    void setup() {
        // Очистка и заполнение тестовых данных в базе
        jdbcTemplate.execute("DELETE FROM posts");
        jdbcTemplate.execute("DELETE FROM tags");
        jdbcTemplate.execute("INSERT INTO posts (id, title, text, likes_count) VALUES (1, 'Тестовый пост', 'Текст поста', 1)");
        jdbcTemplate.execute("INSERT INTO tags (id, post_id, name) VALUES (1, 1, 'tag1')");
    }

    @Test
    void getPosts_withoutSearch_shouldReturnPostsView() throws Exception {
        mockMvc.perform(get("/posts")
                        .param("pageNumber", "0")
                        .param("pageSize", "2"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts"))
                .andExpect(model().attributeExists("posts", "paging", "search"))
                .andExpect(model().attribute("search", ""));
    }

    @Test
    void getPosts_withSearch_shouldFilterPosts() throws Exception {
        mockMvc.perform(get("/posts")
                        .param("search", "tag1"))
                .andExpect(status().isOk())
                .andExpect(view().name("posts"))
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attribute("search", "tag1"));
    }

    @Test
    void viewPostForm_shouldReturnPostView() throws Exception {
        mockMvc.perform(get("/posts/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("post"))
                .andExpect(model().attributeExists("post"));
    }

    @Test
    void createPost_shouldRedirectToPosts() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile(
                "image",
                "test-image.jpg",
                "image/jpeg",
                "some-image-content".getBytes()
        );

        mockMvc.perform(multipart("/posts")
                        .file(imageFile)
                        .param("title", "Новый пост")
                        .param("tags", "tag1,tag2")
                        .param("text", "Текст поста"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));
    }

    @Test
    void updatePost_shouldRedirectToPost() throws Exception {
        MockMultipartFile imageFile = new MockMultipartFile(
                "image",
                "test-image.jpg",
                "image/jpeg",
                "some-image-content".getBytes()
        );

        mockMvc.perform(multipart("/posts/1")
                        .file(imageFile)
                        .param("title", "Обновлённый заголовок")
                        .param("tags", "tag1,tag3")
                        .param("text", "Обновлённый текст"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));
    }

    @Test
    void deletePost_shouldRedirectToPosts() throws Exception {
        mockMvc.perform(post("/posts/1/delete"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts"));
    }

    @Test
    void likePost_shouldRedirectToPost() throws Exception {
        mockMvc.perform(post("/posts/1/like")
                        .param("like", "true"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));
    }

    @Test
    void addComment_shouldRedirectToPost() throws Exception {
        mockMvc.perform(post("/posts/1/comments")
                        .param("text", "Комментарий"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/posts/1"));
    }
}
