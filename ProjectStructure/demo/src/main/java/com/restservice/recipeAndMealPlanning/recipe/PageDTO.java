package com.restservice.recipeAndMealPlanning.recipe;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.Objects;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PageDTO {
    private Integer pageNo = 0;
    private Integer pageSize = 10;
    private Sort.Direction sortDirection = Sort.Direction.ASC;
    private String sortByField = "recipeId";

    protected Pageable getPageable(PageDTO pageDTO) {
        Integer pageNo = Objects.isNull(pageDTO.getPageNo()) || pageDTO.getPageNo() < 0 ? this.pageNo : pageDTO.getPageNo();
        Integer pageSize = Objects.isNull(pageDTO.getPageSize()) || pageDTO.getPageSize() < 1 ? this.pageSize : pageDTO.getPageSize();
        Sort.Direction sortDirection = Objects.isNull(pageDTO.getSortDirection()) ? this.sortDirection : pageDTO.getSortDirection();
        String sortByField = Objects.isNull(pageDTO.getSortByField()) /*|| !Recipe.isValidFieldName(pageDTO.sortByField*/ ? this.sortByField : pageDTO.getSortByField();

        return PageRequest.of(pageNo, pageSize, sortDirection, sortByField);
    }
}
