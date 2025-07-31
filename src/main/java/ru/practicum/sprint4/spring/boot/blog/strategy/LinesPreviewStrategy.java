package ru.practicum.sprint4.spring.boot.blog.strategy;

import ru.practicum.sprint4.spring.boot.blog.model.Post;

import java.util.Arrays;

public class LinesPreviewStrategy implements TextDisplayStrategy{
    private final int numberOfLines;

    public LinesPreviewStrategy(int numberOfLines) {
        if (numberOfLines <= 0) {
            throw new IllegalArgumentException("Количество строк должно быть больше нуля");
        }
        this.numberOfLines = numberOfLines;
    }

    @Override
    public String getText(Post post) {
        if (post.getText() == null || post.getText().isEmpty()) {
            return "";
        }
        String[] lines = post.getText().split("\\r?\\n");
        int count = Math.min(numberOfLines, lines.length);
        return String.join("\n", Arrays.copyOfRange(lines, 0, count));
    }
}
