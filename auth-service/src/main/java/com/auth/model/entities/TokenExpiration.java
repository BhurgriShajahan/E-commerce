package com.auth.model.entities;

import com.auth.enums.TokenType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.naming.Name;

@Entity
@Table(name = "ca_exp_token", schema = "admin")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TokenExpiration {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private TokenType tokenType;

    @Column(name = "token_exp_time")
    private Long expirationTimeInMins;
}
