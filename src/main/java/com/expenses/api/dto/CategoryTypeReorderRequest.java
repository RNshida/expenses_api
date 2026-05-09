package com.expenses.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CategoryTypeReorderRequest {

    private List<CategoryTypeOrderItem> orders;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class CategoryTypeOrderItem {
        private Long id;
        private Integer displayOrder;
    }
}
