package com.zerobase.tablebookingservice.persist.entity;

import com.zerobase.tablebookingservice.model.constants.BookingState;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity(name = "BOOKING")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EntityListeners(AuditingEntityListener.class)
public class BookingEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @ManyToOne
    private StoreEntity store;

    @ManyToOne
    private UserEntity userEntity;

    private LocalDateTime bookingDateTime;

    private String userContact;

    @Enumerated(EnumType.STRING)
    private BookingState bookingState;

    private boolean visitedYn;

    @Min(1)
    @Max(5)
    private int stars;
    private String review;

    @CreatedDate
    private LocalDateTime requestAt;
    @LastModifiedDate
    private LocalDateTime updateAt;
}
