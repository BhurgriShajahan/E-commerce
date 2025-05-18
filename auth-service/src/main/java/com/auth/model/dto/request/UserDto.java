package com.auth.model.dto.request;

import com.auth.model.entities.Role;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.Set;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDto {
    private long user_id;
    private String email;
    private String userName;
    private String first_name;
    private String last_name;
    private String password;
    private String mobileNumber;
    private boolean is_active;
    private Set<Role> roles;
    private String city;
    private LocalDate dateOfBirth;
}
