package com.expenses.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(
    name = "category_goals",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "category_id"})
)
@Getter
@Setter
@NoArgsConstructor
public class CategoryGoal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "target_amount", precision = 15, scale = 2)
    private BigDecimal targetAmount;

    public CategoryGoal(AppUser user, Category category, BigDecimal targetAmount) {
        this.user = user;
        this.category = category;
        this.targetAmount = targetAmount;
    }
}
