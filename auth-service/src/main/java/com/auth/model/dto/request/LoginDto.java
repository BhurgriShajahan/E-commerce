package com.auth.model.dto.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LoginDto {
    private String emailorUsername;
    private String password;

}