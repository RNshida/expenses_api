package com.expenses.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(
    name = "furusato_configs",
    uniqueConstraints = @UniqueConstraint(columnNames = {"user_id", "fiscal_year"})
)
@Getter
@Setter
@NoArgsConstructor
public class FurusatoConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Column(name = "fiscal_year", nullable = false)
    private Integer fiscalYear;

    @Column(name = "annual_income")
    private Integer annualIncome;

    @Column(name = "limit_amount", precision = 15, scale = 2)
    private BigDecimal limitAmount;

    public FurusatoConfig(AppUser user, Integer fiscalYear) {
        this.user = user;
        this.fiscalYear = fiscalYear;
    }
}
