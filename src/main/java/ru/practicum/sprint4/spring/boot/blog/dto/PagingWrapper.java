package ru.practicum.sprint4.spring.boot.blog.dto;

import org.springframework.data.domain.Page;

public class PagingWrapper<T> {
    private final Page<T> page;

    public PagingWrapper(Page<T> page) {
        this.page = page;
    }

    public int pageSize() {
        return page.getSize();
    }

    public int pageCurrent() {
        return page.getNumber() + 1; // для отображения пользователю с 1
    }

    public int pageNumber() {
        return page.getNumber(); // для отображения пользователю с 1
    }

    public boolean hasNext() {
        return page.hasNext();
    }

    public boolean hasPrevious() {
        return page.hasPrevious();
    }

    public int totalPages() {
        return page.getTotalPages();
    }
}