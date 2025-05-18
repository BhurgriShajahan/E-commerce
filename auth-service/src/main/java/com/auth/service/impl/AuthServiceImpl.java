package com.auth.service.impl;

import com.auth.config.MyUserDetailsService;
import com.auth.model.dto.request.LoginDto;
import com.auth.model.dto.request.UserDto;
import com.auth.model.entities.BlacklistedToken;
import com.auth.model.entities.Role;
import com.auth.model.entities.User;
import com.auth.model.mapper.UserMapper;
import com.auth.repository.BlackListTokenRepository;
import com.auth.repository.RoleRepository;
import com.auth.repository.UserRepository;
import com.auth.service.AuthService;
import com.auth.service.BlackListTokenService;
import com.auth.util.EncryptionUtils;
import com.auth.util.JwtUtil;
import com.customutility.model.CustomResponseEntity;
import lombok.RequiredArgsConstructor;
import org.hibernate.service.spi.ServiceException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private static Logger LOGGER = LoggerFactory.getLogger(AuthServiceImpl.class);
    private final UserMapper userMapper;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final MyUserDetailsService customUserDetailsService;
    private final BlackListTokenService tokenService;
    private final BlackListTokenRepository blackListTokenRepository;
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;


    //Signup user
    @Override
    public CustomResponseEntity registerNewUser(UserDto userDto) throws Exception {

        // Check if user already exists by email
        Optional<User> userExist = userRepository.findByEmail(userDto.getEmail());
        if (userExist.isPresent()) {
            return CustomResponseEntity.error("User with email " + userDto.getEmail() + " already exists.");
        }

        // Map DTO to entity
        User user = userMapper.toEntity(userDto);

        // Encrypt the password
        user.setPassword(EncryptionUtils.encrypt(userDto.getPassword()));

        // Initialize role set
        Set<Role> assignedRoles = new HashSet<>();

        // Extract and assign roles from DTO
        List<String> roleNames = userDto.getRoles().stream()
                .map(Role::getName)
                .toList();

        for (String roleName : roleNames) {
            Optional<Role> role = roleRepository.findByName(roleName);
            role.ifPresent(assignedRoles::add);
        }

        if (assignedRoles.isEmpty()) {
            return CustomResponseEntity.error("No valid roles found to assign.");
        }

        user.setRoles(assignedRoles);

        // Initialize optional fields
        user.set_active(true);
        user.setSessionToken(null);
        user.setSessionTokenExpireTime(0L);

        // Save user
        userRepository.save(user);

        return new CustomResponseEntity<>(userMapper.toDto(user), "User registered successfully");
    }


    @Override
    public CustomResponseEntity<?> login(LoginDto loginDto) {
        try {
            User user = userRepository.findByEmailOrUserName(loginDto.getEmailorUsername(), loginDto.getEmailorUsername())
                    .orElseThrow(() -> new ServiceException("Invalid username or password."));

            // Decrypt both stored and login passwords for comparison
            String decryptedLoginPassword = EncryptionUtils.decrypt(loginDto.getPassword());
            String decryptedStoredPassword = EncryptionUtils.decrypt(user.getPassword());

            if (!decryptedLoginPassword.equals(decryptedStoredPassword)) {
                throw new RuntimeException("Invalid Password");
            }

            // Authenticate using AuthenticationManager
            authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginDto.getEmailorUsername(), user.getPassword())
            );

            Set<GrantedAuthority> authorities = customUserDetailsService.getAuthorities(user);

            // roles should be a Set<GrantedAuthority> based on the required type
            Set<GrantedAuthority> roles = authorities.stream()
                    .map(authority -> new SimpleGrantedAuthority(authority.getAuthority()))
                    .collect(Collectors.toSet()); // Using Collectors.toSet() to create a Set

            boolean hasUserRole = roles.stream()
                    .anyMatch(role -> role.getAuthority().equals("ROLE_USER"));

            boolean hasAdminRole = roles.stream()
                    .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

            // Generate the token with roles
            String token = jwtUtil.generateToken(user.getEmail(), roles, user.getUser_id());
            Date expirationDate = new Date(jwtUtil.getTokenExpireTime(token).getTime());

            LOGGER.info("Expiration = " + jwtUtil.getTokenExpireTime(token).getTime());
            LOGGER.info("Expiration Date and Time with a Specific DateTime Format : " + expirationDate);

            // Set session token
            user.setSessionToken(token);

            // Set token expiration time in long format
            user.setSessionTokenExpireTime(jwtUtil.getTokenExpireTime(token).getTime());

            // Save updated user info
            userRepository.save(user);

            Map<String, Object> data = new HashMap<>();
            data.put("userId", user.getUser_id());
            data.put("token", token);
            data.put("firstName", user.getFirst_name());
            data.put("lastName", user.getLast_name());
            data.put("expirationTime", jwtUtil.getTokenExpireTime(token).getTime());

            return new CustomResponseEntity<>(data, "Login successful");

        } catch (Exception e) {
            return CustomResponseEntity.error(e.getMessage());
        }
    }

    @Override
    public boolean findBySessionToken(String sessionToken, long currentTime) {

        boolean tokenBlacklisted = tokenService.isTokenBlacklisted(sessionToken);

        if (tokenBlacklisted) {
            return false;
        }

        return this.userRepository.isValidToken(sessionToken, currentTime);
    }

    @Override
    public CustomResponseEntity<?> logOut(String token) {


        if (token != null && token.startsWith("Bearer ")) {
            token = token.substring(7);

            Date tokenExpireTime = jwtUtil.getTokenExpireTime(token);

            if (tokenExpireTime == null) {
                LOGGER.error("invalid token");
                return CustomResponseEntity.error("invalid token!");
            }

            LocalDateTime localDateTime = tokenExpireTime.toInstant()
                    .atZone(ZoneId.systemDefault())
                    .toLocalDateTime();

            String username = jwtUtil.extractUsername(token);

            User customer = userRepository.findByEmail(username).get();
            if (customer == null) {
                LOGGER.error("customer does not exists");
                return CustomResponseEntity.error("customer not found");
            }

            BlacklistedToken dto = new BlacklistedToken();
            dto.setToken(token);
            dto.setExpirationTime(localDateTime);
            dto.setUser(customer);
            blackListTokenRepository.save(dto);
            LOGGER.info("token blacklisted ....");

            return new CustomResponseEntity<>("Logged out successfully.");
        } else {
            return CustomResponseEntity.error("token missing or invalid!");
        }
    }
}
