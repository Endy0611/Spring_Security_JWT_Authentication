package com.example.spring_security_jwt_authentication.service;

import com.example.spring_security_jwt_authentication.model.request.AppUserRequest;
import org.springframework.security.core.userdetails.UserDetailsService;

public interface AppUserService extends UserDetailsService {
    Object register(AppUserRequest request);
}
