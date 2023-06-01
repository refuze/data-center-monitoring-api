package com.example.datacentermonitoringapi.domain.security;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {

    private long id;
    private String username;
    private String email;
    private boolean isNotificationEnabled;
    private Role role;

}
