package com.zerobase.tablebookingservice.service;

import com.zerobase.tablebookingservice.exception.CustomException;
import com.zerobase.tablebookingservice.model.Arrive;
import com.zerobase.tablebookingservice.model.ArriveParam;
import com.zerobase.tablebookingservice.model.constants.BookingState;
import com.zerobase.tablebookingservice.model.constants.ErrorCode;
import com.zerobase.tablebookingservice.persist.BookingRepository;
import com.zerobase.tablebookingservice.persist.StoreRepository;
import com.zerobase.tablebookingservice.persist.entity.BookingEntity;
import com.zerobase.tablebookingservice.persist.entity.StoreEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

import static com.zerobase.tablebookingservice.model.constants.ErrorCode.BOOKING_NOT_EXIST;
import static com.zerobase.tablebookingservice.model.constants.ErrorCode.LATE_ARRIVE;

@Service
@RequiredArgsConstructor
public class KioskServiceImpl implements KioskService {
    private final StoreRepository storeRepository;
    private final BookingRepository bookingRepository;

    /**
     * 방문 확인
     */
    @Override
    @Transactional
    public Arrive confirmArrive(ArriveParam param) {
        StoreEntity storeEntity = storeRepository.findById(param.getStoreId())
                .orElseThrow(()->new CustomException(ErrorCode.STORE_NOT_EXIST));
        BookingEntity bookingEntity = bookingRepository
                .findByStoreAndBookingDateTimeAndUserContact(storeEntity,
                        param.getReserveDateTime(), param.getUserContact())
                .orElseThrow(() -> new CustomException(BOOKING_NOT_EXIST));

        validateConfirmArrive(bookingEntity.getBookingState(),
                bookingEntity.getBookingDateTime());

        bookingEntity.setVisitedYn(true);
        return Arrive.fromEntity(bookingRepository.save(bookingEntity));
    }

    /**
     * 유효성 검사
     * 예약 확인, 10분전 도착 확인
     */
    private void validateConfirmArrive(BookingState state, LocalDateTime bookingDateTime) {
        if (state != BookingState.ACCEPTED) {
            throw new CustomException(BOOKING_NOT_EXIST);
        }
        if (LocalDateTime.now().isAfter(bookingDateTime.minusMinutes(10))) {
            throw new CustomException(LATE_ARRIVE);
        }
    }

}
