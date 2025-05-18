package com.auth.repository;

import com.auth.model.entities.User;
import org.mapstruct.control.MappingControl;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmail(String email);

    Optional<User> findByEmailOrUserName(String email, String username);

    Optional<User> findByUserName(String username);

    Optional<User> findByEmailAndMobileNumber(String email, String mobileNumber);

    @Query("SELECT COUNT(u) > 0 FROM User u WHERE u.sessionToken = :token AND u.sessionTokenExpireTime > :currentTime")
    boolean isValidToken(@Param("token") String token, @Param("currentTime") long currentTime);


    @Query("SELECT u FROM User u JOIN u.roles r WHERE r.name = :roleName")
    List<User> findByRoleName(@Param("roleName") String roleName);

}
