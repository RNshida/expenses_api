package com.expenses.api.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "furusato_contributions")
@Getter
@Setter
@NoArgsConstructor
public class FurusatoContribution {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "user_id", nullable = false)
    private AppUser user;

    @Column(name = "fiscal_year", nullable = false)
    private Integer fiscalYear;

    @Column(name = "municipality", nullable = false, length = 50)
    private String municipality;

    @Column(name = "product_name", length = 20)
    private String productName;

    @Column(name = "amount", nullable = false, precision = 15, scale = 2)
    private BigDecimal amount;

    @Column(name = "status", nullable = false, length = 30)
    private String status;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @PrePersist
    void prePersist() {
        this.createdAt = LocalDateTime.now();
    }

    public FurusatoContribution(AppUser user, Integer fiscalYear, String municipality,
                                 String productName, BigDecimal amount, String status) {
        this.user = user;
        this.fiscalYear = fiscalYear;
        this.municipality = municipality;
        this.productName = productName;
        this.amount = amount;
        this.status = status;
    }
}
