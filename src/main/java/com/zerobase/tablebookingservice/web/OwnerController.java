package com.zerobase.tablebookingservice.web;

import com.zerobase.tablebookingservice.model.EmailPassParam;
import com.zerobase.tablebookingservice.model.StoreParam;
import com.zerobase.tablebookingservice.model.User;
import com.zerobase.tablebookingservice.model.constants.BookingState;
import com.zerobase.tablebookingservice.persist.entity.UserEntity;
import com.zerobase.tablebookingservice.security.TokenProvider;
import com.zerobase.tablebookingservice.service.OwnerServiceImpl;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/owner")
@RequiredArgsConstructor
public class OwnerController {
    private final OwnerServiceImpl ownerService;
    private final TokenProvider tokenProvider;

    /**
     * 회원가입
     */
    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@RequestBody EmailPassParam param) {
        return ResponseEntity.ok(ownerService.signup(param));
    }

    /**
     * 로그인 및 인증 토큰 발급
     */
    @PostMapping("/signin")
    public ResponseEntity<?> signIn(@RequestBody EmailPassParam param) {
        User user = ownerService.signin(param);
        String token = tokenProvider.createToken(user.getEmail(), user.getUserType());

        return ResponseEntity.ok(token);
    }

    /**
     * 탈퇴
     */
    @DeleteMapping("/account/{id}")
    public ResponseEntity<?> deleteOwnerAccount(@AuthenticationPrincipal UserEntity userEntity) {
        ownerService.deleteOwnerAccount(userEntity);

        return ResponseEntity.ok().build();
    }

    /**
     * 상점추가
     */
    @PostMapping("/store/add")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<?> addStore(@AuthenticationPrincipal UserEntity userEntity, @RequestBody StoreParam param) {
        return ResponseEntity.ok(ownerService.addStore(userEntity, param));
    }

    /**
     * 상점 정보 수정
     */
    @PutMapping("/store/{storeId}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<?> editStoreInfo(@AuthenticationPrincipal UserEntity userEntity, @PathVariable Long storeId, @RequestBody StoreParam param) {
        return ResponseEntity.ok(ownerService.editStoreInfo(userEntity, storeId, param));
    }

    /**
     * 상점 삭제
     */
    @DeleteMapping("/store/{storeId}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<?> deleteStore(@AuthenticationPrincipal UserEntity userEntity, @PathVariable Long storeId) {
        ownerService.deleteStore(userEntity, storeId);

        return ResponseEntity.ok().build();
    }

    /**
     * 예약 확인
     */
    @GetMapping("/store/booking")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<?> checkBookings(@AuthenticationPrincipal UserEntity userEntity) {
        return ResponseEntity.ok(ownerService.getBookings(userEntity));
    }

    // 점주 예약 확정 및 거절인
    @PutMapping("/store/booking/{bookingId}")
    @PreAuthorize("hasRole('OWNER')")
    public ResponseEntity<?> confirmBooking(@AuthenticationPrincipal UserEntity userEntity, @PathVariable Long bookingId, @RequestBody BookingState state) {
        return ResponseEntity.ok(ownerService.confirmBooking(userEntity, bookingId, state));

    }
}
