package com.zerobase.tablebookingservice.model;

import com.zerobase.tablebookingservice.model.constants.ErrorCode;
import lombok.*;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
@Builder
public class ErrorResponse {
    private ErrorCode errorCode;
    private String errorMessage;
}
