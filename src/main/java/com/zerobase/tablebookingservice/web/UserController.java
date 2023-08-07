package com.zerobase.tablebookingservice.web;

import com.zerobase.tablebookingservice.model.BookingParam;
import com.zerobase.tablebookingservice.model.EmailPassParam;
import com.zerobase.tablebookingservice.model.ReviewParam;
import com.zerobase.tablebookingservice.model.User;
import com.zerobase.tablebookingservice.persist.entity.UserEntity;
import com.zerobase.tablebookingservice.security.TokenProvider;
import com.zerobase.tablebookingservice.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final TokenProvider tokenProvider;

    /**
     * 회원 가입
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signup(@RequestBody EmailPassParam request) {
        return ResponseEntity.ok(userService.signup(request));
    }

    /**
     * 로그인
     */
    @PostMapping("/signin")
    public ResponseEntity<?> signin(@RequestBody EmailPassParam request) {
        User user = userService.signin(request);
        String token = tokenProvider
                .createToken(user.getEmail(), user.getUserType());

        return ResponseEntity.ok(token);
    }

    /**
     * 탈퇴
     */
    @DeleteMapping("/user/{id}")
    public ResponseEntity<?> deleteUser(@AuthenticationPrincipal UserEntity userEntity) {
        userService.deleteUser(userEntity);

        return ResponseEntity.ok().build();
    }

    /**
     * 상점 검색
     */
    @GetMapping("/search")
    public ResponseEntity<?> searchStore(@RequestBody String keyword) {
        return ResponseEntity.ok(userService.searchStore(keyword));
    }

    /**
     * 이름순 상점 조회
     */
    @GetMapping("/search/by-name/{page}")
    public ResponseEntity<?> storeListByName(@PathVariable int page) {
        return ResponseEntity.ok(userService.getStoreListByName(page));
    }

    /**
     * 별점순 상점 조회
     */
    @GetMapping("/search/by-stars/{page}")
    public ResponseEntity<?> storeListByStars(@PathVariable int page) {
        return ResponseEntity.ok(userService.getStoreListByStars(page));
    }


    /**
     * 상점 예약
     */
    @PostMapping("/user/reserve/{storeId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> reserveStore(@AuthenticationPrincipal UserEntity userEntity, @PathVariable long storeId, @RequestBody BookingParam bookingParam) {
        return ResponseEntity.ok(userService
                .bookingStore(userEntity, storeId, bookingParam));
    }

    /**
     * 예약 확인
     */
    @GetMapping("/user/reserve/{bookingId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> seeReservation(@AuthenticationPrincipal UserEntity userEntity, @PathVariable long bookingId) {
        return ResponseEntity.ok(userService
                .searchBooking(userEntity, bookingId));
    }

    /**
     * 예약 취소
     */
    @DeleteMapping("/user/reserve/{bookingId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> cancelReservation(@AuthenticationPrincipal UserEntity userEntity, @PathVariable long bookingId) {
        return ResponseEntity.ok(userService
                .cancelBooking(userEntity, bookingId));
    }

    /**
     * 리뷰 작성 및 수정
     */
    @PutMapping("/user/reserve/{reservationId}")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<?> writeReview(@AuthenticationPrincipal UserEntity userEntity, @PathVariable long reservationId, @RequestBody ReviewParam reviewParam) {
        return ResponseEntity.ok(userService
                .writeReview(userEntity, reservationId, reviewParam));
    }
}
