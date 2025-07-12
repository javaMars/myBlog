-- Таблица постов
CREATE TABLE IF NOT EXISTS posts (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    text CLOB,
    likes_count INT DEFAULT 0
);

-- Таблица тегов (каждый тег связан с постом)
CREATE TABLE IF NOT EXISTS tags (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id BIGINT NOT NULL,
    name VARCHAR(100),
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE
);

-- Таблица комментариев
CREATE TABLE IF NOT EXISTS comments (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    post_id BIGINT NOT NULL,
    text VARCHAR(1000),
    FOREIGN KEY (post_id) REFERENCES posts(id) ON DELETE CASCADE
);