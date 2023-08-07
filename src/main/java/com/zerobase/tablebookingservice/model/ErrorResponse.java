package com.zerobase.tablebookingservice.model;

import com.zerobase.tablebookingservice.model.constants.ErrorCode;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class ErrorResponse {
    private ErrorCode errorCode;
    private String errorMessage;
}
