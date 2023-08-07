package com.zerobase.tablebookingservice.web;

import com.zerobase.tablebookingservice.model.ArriveParam;
import com.zerobase.tablebookingservice.service.KioskService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class KioskController {
    private final KioskService kioskService;

    /**
     * 방문 확인
     */
    @PutMapping("/kiosk/arrive")
    public ResponseEntity<?> confirmArrival(@RequestBody ArriveParam param) {
        return ResponseEntity.ok(kioskService.confirmArrive(param));
    }
}
