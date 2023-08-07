package com.zerobase.tablebookingservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerobase.tablebookingservice.persist.entity.BookingEntity;
import lombok.*;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Arrive {
    private String storeName;
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime reservationDateTime;
    private boolean visitedYn;

    public static Arrive fromEntity(BookingEntity bookingEntity) {
        return Arrive.builder()
                .storeName(bookingEntity.getStore().getName())
                .reservationDateTime(bookingEntity.getBookingDateTime())
                .visitedYn(bookingEntity.isVisitedYn())
                .build();
    }
}
