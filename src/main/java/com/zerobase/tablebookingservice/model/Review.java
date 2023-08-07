package com.zerobase.tablebookingservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerobase.tablebookingservice.persist.entity.BookingEntity;
import lombok.*;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Review {
    private String storeName;
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime bookingDateTime;
    private int stars;
    private String review;

    public static Review fromEntity(BookingEntity bookingEntity) {
        return Review.builder()
                .storeName(bookingEntity.getStore().getName())
                .bookingDateTime(bookingEntity.getBookingDateTime())
                .stars(bookingEntity.getStars())
                .review(bookingEntity.getReview())
                .build();
    }
}
