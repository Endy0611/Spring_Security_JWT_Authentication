package com.example.spring_security_jwt_authentication.service.impl;

import com.example.spring_security_jwt_authentication.model.entity.AppUser;
import com.example.spring_security_jwt_authentication.model.request.AppUserRequest;
import com.example.spring_security_jwt_authentication.model.response.AppUserResponse;
import com.example.spring_security_jwt_authentication.repository.AppUserRepository;
import com.example.spring_security_jwt_authentication.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        AppUser user = appUserRepository.getUserByEmail(email);
        if (user == null) throw new UsernameNotFoundException("User not found");
        System.out.println("User logging in: " + email);
        System.out.println("Authorities found: " + user.getAuthorities());

        return user;
    }


    @Override
    public AppUserResponse register(AppUserRequest request) {
        request.setPassword(passwordEncoder.encode(request.getPassword()));
        AppUser appUser = appUserRepository.register(request);
        for (String role : request.getRoles()){
            if (role.equalsIgnoreCase("ROLE_USER")){
                appUserRepository.insertUserIdAndRoleId(appUser.getUserId(), 1L);
            }
            if (role.equalsIgnoreCase("ROLE_ADMIN")){
                appUserRepository.insertUserIdAndRoleId(appUser.getUserId(), 2L);
            }
        }

        return modelMapper.map(appUserRepository.getUserById(appUser.getUserId()), AppUserResponse.class);
    }
}
