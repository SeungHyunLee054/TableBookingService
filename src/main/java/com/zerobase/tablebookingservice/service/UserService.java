package com.zerobase.tablebookingservice.service;

import com.zerobase.tablebookingservice.model.*;
import com.zerobase.tablebookingservice.persist.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface UserService extends UserDetailsService {
    @Override
    UserDetails loadUserByUsername(String email) throws UsernameNotFoundException;

    User signup(EmailPassParam param);

    User signin(EmailPassParam emailPassEnter);

    void deleteUser(UserEntity userEntity);

    List<Store> searchStore(String keyword);

    Page<Store> getStoreListByName(int page);

    Page<Store> getStoreListByStars(int page);

    Booking bookingStore(UserEntity userEntity, long storeId, BookingParam bookingParam);

    Booking searchBooking(UserEntity userEntity, long bookingId);

    Booking cancelBooking(UserEntity userEntity, long bookingId);

    Review writeReview(UserEntity userEntity, long bookingId, ReviewParam reviewParam);
}
