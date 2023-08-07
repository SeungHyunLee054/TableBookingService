package com.zerobase.tablebookingservice.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.zerobase.tablebookingservice.model.constants.BookingState;
import com.zerobase.tablebookingservice.persist.entity.BookingEntity;
import lombok.*;

import java.time.LocalDateTime;

import static com.fasterxml.jackson.annotation.JsonFormat.Shape.STRING;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Booking {
    private String storeName;
    @JsonFormat(shape = STRING, pattern = "yyyy-MM-dd HH:mm", timezone = "Asia/Seoul")
    private LocalDateTime bookingDateTime;
    private BookingState bookingState;

    public static Booking fromEntity(BookingEntity bookingEntity){
        return Booking.builder()
                .storeName(bookingEntity.getStore().getName())
                .bookingDateTime(bookingEntity.getBookingDateTime())
                .bookingState(bookingEntity.getBookingState())
                .build();
    }
}
