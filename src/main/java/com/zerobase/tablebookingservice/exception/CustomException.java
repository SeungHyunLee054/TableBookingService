package com.zerobase.tablebookingservice.exception;

import com.zerobase.tablebookingservice.model.constants.ErrorCode;
import lombok.Getter;

@Getter
public class CustomException extends Throwable {
    private ErrorCode errorCode;
    private String errorMessage;

    public CustomException(ErrorCode errorCode) {
        this.errorCode = errorCode;
        this.errorMessage = errorCode.getDescription();
    }
}
