package com.zerobase.tablebookingservice.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SignIn {
    private String email;
    private String password;
}
