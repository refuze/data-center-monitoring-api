package com.example.datacentermonitoringapi.domain.security;

public class UserMapper {

    public static UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .username(user.getUsername())
                .role(user.getRole())
                .isNotificationEnabled(user.isNotificationEnabled())
                .build();
    }

}
