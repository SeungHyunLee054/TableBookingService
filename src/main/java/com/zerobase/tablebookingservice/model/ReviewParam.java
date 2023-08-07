package com.zerobase.tablebookingservice.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ReviewParam {
    private int stars;
    private String review;
}
