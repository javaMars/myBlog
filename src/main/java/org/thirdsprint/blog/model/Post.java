package org.thirdsprint.blog.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import org.thirdsprint.blog.post.strategy.FullTextStrategy;
import org.thirdsprint.blog.post.strategy.LinesPreviewStrategy;
import org.thirdsprint.blog.post.strategy.TextDisplayStrategy;

import java.util.*;

@Setter
@Getter
@Entity
@Table(name = "posts")
public class Post {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;

    @Lob
    @Column
    private String text;
    @Column(name = "likes_count")
    private Long likesCount = 0L;;
    @Column(name = "image_path")
    private String imagePath = "";

    @Transient
    public String getImagePath() {
        if (Objects.equals(imagePath, "") && id != null) {
            return "/images/" + id + ".jpg";
        }
        return imagePath;
    }

    public String getTextPreview() {
        return new LinesPreviewStrategy(3).getText(this);
    }

    public List<String> getTextParts() {
        String fullText = new FullTextStrategy().getText(this);

        if (fullText == null) {
            return Collections.emptyList();
        }
        return Arrays.asList(fullText.split("\\r?\\n"));
    }

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Comment> comments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    private List<Tag> tags = new ArrayList<>();
}
