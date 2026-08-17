package com.library.kernel.web;

import java.util.List;

public record Page<T>(List<T> items, long totalElements, int totalPages, int pageNumber, int pageSize) {

    public static <T> Page<T> of(List<T> items, long totalElements, int pageNumber, int pageSize) {
        int totalPages = pageSize > 0 ? (int) Math.ceil((double) totalElements / pageSize) : 0;
        if (totalPages == 0 && !items.isEmpty()) {
            totalPages = 1;
        }
        return new Page<>(items, totalElements, totalPages, pageNumber, pageSize);
    }

    public boolean hasNext() {
        return pageNumber < totalPages - 1;
    }

    public boolean hasPrevious() {
        return pageNumber > 0;
    }

    public int getNextPage() {
        return hasNext() ? pageNumber + 1 : pageNumber;
    }

    public int getPreviousPage() {
        return hasPrevious() ? pageNumber - 1 : 0;
    }
}
