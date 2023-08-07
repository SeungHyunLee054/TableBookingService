package com.zerobase.tablebookingservice.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StoreParam {
    private String name;
    private String address;
    private String description;
}
