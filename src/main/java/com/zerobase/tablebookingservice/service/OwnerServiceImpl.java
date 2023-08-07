package com.zerobase.tablebookingservice.service;

import com.zerobase.tablebookingservice.exception.CustomException;
import com.zerobase.tablebookingservice.model.*;
import com.zerobase.tablebookingservice.model.constants.BookingState;
import com.zerobase.tablebookingservice.model.constants.UserType;
import com.zerobase.tablebookingservice.persist.BookingRepository;
import com.zerobase.tablebookingservice.persist.StoreRepository;
import com.zerobase.tablebookingservice.persist.UserRepository;
import com.zerobase.tablebookingservice.persist.entity.BookingEntity;
import com.zerobase.tablebookingservice.persist.entity.StoreEntity;
import com.zerobase.tablebookingservice.persist.entity.UserEntity;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static com.zerobase.tablebookingservice.model.constants.ErrorCode.*;

@Service
@RequiredArgsConstructor
@Slf4j
@Primary
public class OwnerServiceImpl implements OwnerService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final BookingRepository bookingRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return this.userRepository.findByEmail(username)
                .orElseThrow(() -> new CustomException(ACCOUNT_NOT_EXIST));
    }

    /**
     * 회원 가입
     */
    @Override
    @Transactional
    public User signup(EmailPassParam param) {
        validateSignup(param.getEmail());

        return User.fromEntity(
                userRepository.save(
                        UserEntity.builder()
                                .email(param.getEmail())
                                .password(encodePassword(param.getPassword()))
                                .role(UserType.ROLE_OWNER)
                                .activated(true)
                                .build())
        );
    }

    /**
     * 로그인
     */
    @Override
    @Transactional
    public User signin(EmailPassParam param) {
        UserEntity userEntity = userRepository.findByEmail(param.getEmail())
                .orElseThrow(() -> new CustomException(ACCOUNT_NOT_EXIST));

        validateSignin(userEntity, param.getPassword());

        return User.fromEntity(userEntity);
    }


    /**
     * 탈퇴
     */
    @Override
    @Transactional
    public void deleteOwnerAccount(UserEntity userEntity) {
        validateDeleteOwner(userEntity);
        userEntity.setActivated(false);

        userRepository.save(userEntity);
    }

    /**
     * 상점추가
     */
    @Override
    @Transactional
    public Store addStore(UserEntity userEntity, StoreParam param) {
        validateAddStore(param.getName(), param.getAddress());

        return Store.fromEntity(
                storeRepository.save(
                        StoreEntity.builder()
                                .name(param.getName())
                                .address(param.getAddress())
                                .description(param.getDescription())
                                .userEntity(userEntity)
                                .build())
        );
    }

    /**
     * 상점 정보 수정
     */
    @Override
    @Transactional
    public Store editStoreInfo(UserEntity userEntity, Long storeId, StoreParam param) {
        StoreEntity storeEntity = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(STORE_NOT_EXIST));

        validateStoreOwner(userEntity, storeEntity);

        storeEntity.setName(param.getName());
        storeEntity.setAddress(param.getAddress());
        storeEntity.setDescription(param.getDescription());


        return Store.fromEntity(storeRepository.save(storeEntity));
    }

    /**
     * 상점 삭제
     */
    @Override
    @Transactional
    public void deleteStore(UserEntity userEntity, Long storeId) {
        StoreEntity storeEntity = storeRepository.findById(storeId)
                .orElseThrow(() -> new CustomException(STORE_NOT_EXIST));

        validateStoreOwner(userEntity, storeEntity);

        storeRepository.delete(storeEntity);
    }

    /**
     * 예약 목록 확인
     */
    @Override
    public List<Booking> getBookings(UserEntity userEntity) {
        List<StoreEntity> stores = storeRepository.findByUserEntity(userEntity);
        return stores.stream()
                .flatMap(store -> bookingRepository
                        .findByStoreOrderByStoreDescBookingDateTime(store)
                        .stream())
                .map(Booking::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 예약 확정 및 거절
     */
    @Override
    @Transactional
    public Booking confirmBooking(UserEntity userEntity, Long reservationId, BookingState state) {
        BookingEntity bookingEntity = bookingRepository.findById(reservationId)
                .orElseThrow(() -> new CustomException(BOOKING_NOT_EXIST));
        validateBookingStoreOwner(userEntity, bookingEntity);
        bookingEntity.setBookingState(state);

        return Booking.fromEntity(bookingRepository.save(bookingEntity));
    }

    private void validateSignup(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new CustomException(EMAIL_ALREADY_REGISTERED);
        }
    }

    /**
     * 비밀번호 암호화
     */
    private String encodePassword(String password) {
        if (password == null || password.length() < 1) {
            throw new RuntimeException();
        }

        return passwordEncoder.encode(password);
    }

    private void validateSignin(UserEntity userEntity, String password) {
        if (!passwordEncoder.matches(password, userEntity.getPassword())) {
            throw new CustomException(PASSWORD_IS_INCORRECT);
        }
        if (!userEntity.isActivated()) {
            throw new CustomException(UNACTIVATED_ACCOUNT);
        }
    }

    private void validateDeleteOwner(UserEntity userEntity) {
        if (storeRepository.countByUserEntity(userEntity) > 0) {
            throw new CustomException(ACCOUNT_BOOKING_EXISTS);
        }
    }

    private void validateAddStore(String name, String address) {
        if (storeRepository.existsByNameAndAddress(name, address)) {
            throw new CustomException(STORE_ALREADY_ADDED);
        }
    }

    private void validateStoreOwner(UserEntity userEntity, StoreEntity storeEntity) {
        if (storeEntity.getUserEntity().getId() != userEntity.getId()) {
            throw new CustomException(STORE_OWNER_UNMATCH);
        }
    }

    private void validateBookingStoreOwner(UserEntity userEntity, BookingEntity bookingEntity) {
        if (bookingEntity.getStore().getUserEntity().getId() != userEntity.getId()) {
            throw new CustomException(BOOKING_STORE_OWNER_UNMATCH);
        }
    }
}
