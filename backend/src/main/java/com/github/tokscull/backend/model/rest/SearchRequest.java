package com.github.tokscull.backend.model.rest;

import com.github.tokscull.backend.model.search.Filter;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class SearchRequest {
    private Integer page;
    private Integer size;
    private List<Filter> filters;

    public List<Filter> getFilters() {
        if (Objects.isNull(this.filters)) return new ArrayList<>();
        return this.filters;
    }
}
