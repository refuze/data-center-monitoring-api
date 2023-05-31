package com.example.datacentermonitoringapi.domain.security;

import org.springframework.security.core.GrantedAuthority;

public enum Role implements GrantedAuthority {
    USER,
    ADMIN,
    SENSOR;

    @Override
    public String getAuthority(){
        return "ROLE_" + this.name();
    }
}
