package com.expenses.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(
    name = "category_type_goals",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "category_type_id"})
)
@Getter
@Setter
@NoArgsConstructor
public class CategoryTypeGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_type_id", nullable = false)
    private CategoryType categoryType;

    @Column(name = "target_amount", precision = 15, scale = 2)
    private BigDecimal targetAmount;

    public CategoryTypeGoal(AppUser user, CategoryType categoryType, BigDecimal targetAmount) {
        this.user = user;
        this.categoryType = categoryType;
        this.targetAmount = targetAmount;
    }
}
