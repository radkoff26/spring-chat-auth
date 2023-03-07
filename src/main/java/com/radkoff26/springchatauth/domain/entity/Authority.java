package com.radkoff26.springchatauth.domain.entity;

import org.springframework.security.core.GrantedAuthority;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Authority implements GrantedAuthority {
    private long id;
    private String role;

    @Override
    public String getAuthority() {
        return role;
    }
}
