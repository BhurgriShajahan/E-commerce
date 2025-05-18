package com.auth.service;

import com.auth.model.dto.request.LoginDto;
import com.auth.model.dto.request.UserDto;
import com.customutility.model.CustomResponseEntity;

public interface AuthService {

    CustomResponseEntity<?> registerNewUser(UserDto userDto) throws Exception;
    boolean findBySessionToken(String sessionToken, long currentTime);
    CustomResponseEntity<?> logOut(String token);
    CustomResponseEntity<?> login(LoginDto loginDto);
}

