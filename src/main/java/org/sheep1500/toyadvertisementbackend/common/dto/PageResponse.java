package org.sheep1500.toyadvertisementbackend.common.dto;

import lombok.Getter;
import org.springframework.data.domain.Page;

import java.util.List;


@Getter
public class PageResponse<T> {
    private List<T> content;
    private int pageNumber;
    private int pageSize;
    private int numberOfElements;
    private int totalPages;
    private long totalElements;
    private boolean last;
    private boolean first;

    public PageResponse(Page<T> page) {
        this.content = page.getContent();
        this.pageNumber = page.getNumber();
        this.pageSize = page.getSize();
        this.numberOfElements = page.getNumberOfElements();
        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
        this.last = page.isLast();
        this.first = page.isFirst();
    }
}
