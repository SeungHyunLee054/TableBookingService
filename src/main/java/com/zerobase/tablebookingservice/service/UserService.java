package com.zerobase.tablebookingservice.service;

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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {
    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;
    private final StoreRepository storeRepository;
    private final BookingRepository bookingRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        return this.userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException());
    }

    /**
     * 회원가입
     */
    public User signup(EmailPassParam emailPassEnter) {
        log.info("회원가입 유효성 검사");
        validateSignUpUser(emailPassEnter.getEmail());

        return User.fromEntity(
                userRepository.save(
                        UserEntity.builder()
                                .email(emailPassEnter.getEmail())
                                .password(emailPassEnter.getPassword())
                                .role(UserType.ROLE_USER)
                                .activated(true)
                                .build()
                )
        );
    }

    /**
     * 로그인
     */
    public User signin(EmailPassParam emailPassEnter) {
        UserEntity userEntity = userRepository
                .findByEmail(emailPassEnter.getEmail())
                .orElseThrow(() -> new RuntimeException());

        log.info("로그인 유효성 검사");
        validateSignInUser(emailPassEnter, userEntity);

        return User.fromEntity(userEntity);
    }

    /**
     * 탈퇴
     */
    public void deleteUser(UserEntity userEntity) {
        log.info("탈퇴 유효성 검사");
        validateDeleteUser(userEntity);
        userEntity.setActivated(false);
        userRepository.save(userEntity);
    }

    /**
     * 상점 검색
     */
    public List<Store> searchStore(String keyword) {
        List<StoreEntity> stores = storeRepository.
                findByNameContainingIgnoreCaseOrAddressContainingIgnoreCaseOrDescriptionContainingIgnoreCase(
                        keyword, keyword, keyword
                );

        return stores.stream()
                .map(Store::fromEntity)
                .collect(Collectors.toList());
    }

    /**
     * 이름순 상점 조회
     */
    public Page<Store> getStoreListByName(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        List<Store> stores = storeRepository.findAll().stream()
                .map(Store::fromEntity)
                .collect(Collectors.toList());
        stores.sort(Comparator.comparing(Store::getName));

        return new PageImpl<>(stores, pageable, stores.size());
    }

    /**
     * 별점순 상점 조회
     */
    public Page<Store> getStoreListByStars(int page) {
        Pageable pageable = PageRequest.of(page, 10);
        List<Store> stores = storeRepository.findAll().stream()
                .map(Store::fromEntity)
                .collect(Collectors.toList());
        stores.sort((x, y) -> y.getStars().compareTo(x.getStars()));

        return new PageImpl<>(stores, pageable, stores.size());
    }

    /**
     * 상점 예약
     */
    public Booking bookingStore(UserEntity userEntity, long storeId, BookingParam bookingParam) {
        StoreEntity storeEntity = storeRepository.findById(storeId)
                .orElseThrow(() -> new RuntimeException());

        validateBookingStore(userEntity, storeEntity, bookingParam);

        return Booking.fromEntity(
                bookingRepository.save(
                        BookingEntity.builder()
                                .store(storeEntity)
                                .userEntity(userEntity)
                                .bookingDateTime(bookingParam.getBookingDateTime())
                                .userContact(bookingParam.getUserContact())
                                .bookingState(BookingState.PENDING)
                                .build()
                )
        );
    }

    /**
     * 예약 조회
     */
    public Booking searchBooking(UserEntity userEntity, long bookingId) {
        BookingEntity bookingEntity = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException());

        validateBookingOwner(userEntity, bookingEntity);

        return Booking.fromEntity(bookingEntity);
    }

    /**
     * 예약 취소
     */
    public Booking cancelBooking(UserEntity userEntity, long bookingId) {
        BookingEntity bookingEntity = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException());

        validateBookingOwner(userEntity, bookingEntity);

        bookingEntity.setBookingState(BookingState.CANCELED);

        return Booking.fromEntity(bookingRepository.save(bookingEntity));
    }

    /**
     * 리뷰 작성 및 수정
     */
    public Review writeReview(UserEntity userEntity, long bookingId, ReviewParam reviewParam) {
        BookingEntity bookingEntity = bookingRepository.findById(bookingId)
                .orElseThrow(() -> new RuntimeException());

        validateWriteReview(userEntity, bookingEntity, reviewParam.getStars());

        bookingEntity.setStars(reviewParam.getStars());
        bookingEntity.setReview(reviewParam.getReview());

        return Review.fromEntity(bookingRepository.save(bookingEntity));
    }

    private void validateWriteReview(UserEntity userEntity, BookingEntity bookingEntity, int stars) {
        if (userEntity.getId() != bookingEntity.getUserEntity().getId()) {
            throw new RuntimeException();
        }
        if (!bookingEntity.isVisitedYn()) {
            throw new RuntimeException();
        }
        if (stars < 1 || stars > 5) {
            throw new RuntimeException();
        }
    }

    private void validateBookingOwner(UserEntity userEntity, BookingEntity bookingEntity) {
        if (userEntity.getId() != bookingEntity.getUserEntity().getId()) {
            throw new RuntimeException();
        }
    }

    private void validateBookingStore(UserEntity userEntity, StoreEntity storeEntity, BookingParam bookingParam) {
        if (bookingRepository
                .countByUserEntityAndStoreAndBookingDateTime(
                        userEntity, storeEntity,
                        bookingParam.getBookingDateTime()) > 0) {
            throw new RuntimeException();
        }
    }

    private void validateDeleteUser(UserEntity userEntity) {
        if (bookingRepository.countByUserEntity(userEntity) > 0) {
            throw new RuntimeException();
        }
    }

    private void validateSignInUser(EmailPassParam emailPassEnter, UserEntity userEntity) {
        if (!passwordEncoder.matches(emailPassEnter.getPassword(), userEntity.getPassword())) {
            throw new RuntimeException();
        }
        if (!userEntity.isActivated()) {
            throw new RuntimeException();
        }
    }

    private void validateSignUpUser(String email) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException();
        }
    }
}
