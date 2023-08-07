package com.zerobase.tablebookingservice.service;

import com.zerobase.tablebookingservice.model.*;
import com.zerobase.tablebookingservice.model.constants.BookingState;
import com.zerobase.tablebookingservice.persist.entity.UserEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.List;

public interface OwnerService extends UserDetailsService {
    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException;

    User signup(EmailPassParam param);

    User signin(EmailPassParam param);

    void deleteOwnerAccount(UserEntity userEntity);

    Store addStore(UserEntity userEntity, StoreParam param);

    Store editStoreInfo(UserEntity userEntity, Long storeId, StoreParam param);

    void deleteStore(UserEntity userEntity, Long storeId);

    List<Booking> getBookings(UserEntity userEntity);

    Booking confirmBooking(UserEntity userEntity, Long reservationId, BookingState state);
}
