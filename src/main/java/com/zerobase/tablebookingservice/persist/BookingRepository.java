package com.zerobase.tablebookingservice.persist;

import com.zerobase.tablebookingservice.persist.entity.BookingEntity;
import com.zerobase.tablebookingservice.persist.entity.StoreEntity;
import com.zerobase.tablebookingservice.persist.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<BookingEntity, Long> {
    long countByUserEntity(UserEntity userEntity);

    long countByUserEntityAndStoreAndBookingDateTime(
            UserEntity userEntity, StoreEntity storeEntity, LocalDateTime time);

    List<BookingEntity> findByStoreOrderByStoreDescBookingDateTime(
            StoreEntity storeEntity);

    Optional<BookingEntity> findByStoreAndBookingDateTimeAndUserContact(
            StoreEntity storeEntity, LocalDateTime bookingDateTime,
            String userContact);
}
