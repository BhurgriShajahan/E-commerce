package com.auth.service;

import com.auth.model.dto.TokenExpirationDto;
import com.customutility.model.CustomResponseEntity;

public interface TokenExpirationService {

    CustomResponseEntity createTokenExpiration(TokenExpirationDto tokenExpirationDto);

    CustomResponseEntity updateTokeExpiration(TokenExpirationDto tokenExpirationDto);

    CustomResponseEntity deleteTokenExpiration(Long tokenExpireId);

    CustomResponseEntity getTokenExpiration(Long tokenExpireId);

    CustomResponseEntity getAllTokenExpiration();

}
