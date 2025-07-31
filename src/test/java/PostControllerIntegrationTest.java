import jakarta.persistence.Column;
import jakarta.persistence.Lob;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import ru.practicum.sprint4.spring.boot.blog.config.AppConfig;
import ru.practicum.sprint4.spring.boot.blog.config.TestJpaConfig;
import ru.practicum.sprint4.spring.boot.blog.config.WebConfig;
import ru.practicum.sprint4.spring.boot.blog.repository.PostRepository;
import ru.practicum.sprint4.spring.boot.blog.service.CommentService;
import ru.practicum.sprint4.spring.boot.blog.service.PostService;
import org.springframework.mock.web.MockMultipartFile;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(SpringExtension.class)
@WebAppConfiguration
@ContextConfiguration(classes = {TestJpaConfig.class, WebConfig.class, AppConfig.class})
public class PostControllerIntegrationTest {
    @Autowired
    private WebApplicationContext webApplicationContext;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Autowired
    private PostRepository postRepository;

    @Autowired
    private PostService postService;

    @Autowired
    private CommentService commentService;

    private MockMvc mockMvc;

    @BeforeEach
    void setup() {

        mockMvc = MockMvcBuilders.webAppContextSetup(webApplicationContext).build();

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
                .andExpect(model().attributeExists("posts"))
                .andExpect(model().attributeExists("paging"))
                .andExpect(model().attributeExists("search"))
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
