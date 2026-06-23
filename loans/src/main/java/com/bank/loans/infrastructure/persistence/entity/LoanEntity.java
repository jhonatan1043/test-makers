package com.bank.loans.infrastructure.persistence.entity;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import com.bank.loans.domain.enums.LoanStatus;
import jakarta.persistence.*;
import lombok.*;


@Entity
@Table(name="loans")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoanEntity {

    @Id
    @GeneratedValue(
        strategy = GenerationType.IDENTITY
    )
    private Long id;

    @Column(
        nullable = false
    )
    private BigDecimal amount;


    @Column(
        nullable = false
    )
    private Integer termMonths;

    @Enumerated(EnumType.STRING)
    private LoanStatus status;

    @ManyToOne(
        fetch = FetchType.LAZY
    )

    @JoinColumn(
        name="user_id"
    )
    private UserEntity user;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @PrePersist
    public void prePersist(){

        this.createdAt =
            LocalDateTime.now();
        this.updatedAt =
            LocalDateTime.now();

        if(status == null){
            this.status =
                LoanStatus.PENDING;
        }
    }

    @PreUpdate
    public void preUpdate(){
        this.updatedAt =
            LocalDateTime.now();

    }


}