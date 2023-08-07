package com.zerobase.tablebookingservice.model;

import com.zerobase.tablebookingservice.model.constants.UserType;
import com.zerobase.tablebookingservice.persist.entity.UserEntity;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {
    private long id;
    private String email;
    private UserType userType;

    public static User fromEntity(UserEntity userEntity){
        return User.builder()
                .id(userEntity.getId())
                .email(userEntity.getEmail())
                .userType(UserType.ROLE_OWNER)
                .build();
    }
}
