package com.expenses.api.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class CategoryReorderRequest {

    private List<CategoryOrderItem> orders;

    @Getter
    @Setter
    @NoArgsConstructor
    public static class CategoryOrderItem {
        private Long id;
        private Integer displayOrder;
    }
}
