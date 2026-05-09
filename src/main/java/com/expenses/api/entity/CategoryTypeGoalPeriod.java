package com.expenses.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "category_type_goal_periods")
@Getter
@Setter
@NoArgsConstructor
public class CategoryTypeGoalPeriod {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_type_id", nullable = false)
    private CategoryType categoryType;

    @Column(name = "start_year_month", nullable = true, length = 7)
    private String startYearMonth;

    @Column(name = "end_year_month", nullable = true, length = 7)
    private String endYearMonth;

    @Column(name = "target_amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal targetAmount;

    public CategoryTypeGoalPeriod(AppUser user, CategoryType categoryType,
                                   String startYearMonth, String endYearMonth, BigDecimal targetAmount) {
        this.user = user;
        this.categoryType = categoryType;
        this.startYearMonth = startYearMonth;
        this.endYearMonth = endYearMonth;
        this.targetAmount = targetAmount;
    }
}
