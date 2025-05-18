package com.auth.model.dto;

import com.auth.enums.TokenType;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenExpirationDto {
    private Long id;

    private TokenType tokenType;

    private Long expirationTimeInMins;
}
