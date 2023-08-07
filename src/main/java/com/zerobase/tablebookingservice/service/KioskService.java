package com.zerobase.tablebookingservice.service;

import com.zerobase.tablebookingservice.model.Arrive;
import com.zerobase.tablebookingservice.model.ArriveParam;
import org.springframework.transaction.annotation.Transactional;

public interface KioskService {
    /**
     * 방문 확인
     */
    @Transactional
    Arrive confirmArrive(ArriveParam param);
}
