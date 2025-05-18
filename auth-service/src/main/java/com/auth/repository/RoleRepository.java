package com.auth.repository;


import com.auth.model.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
    boolean existsByName(String name);

    Optional<Role> findByName(String role);

    @Query("SELECT r FROM Role r WHERE r.name <> 'ROLE_SUPER_ADMIN'")
    List<Role> findAllRolesWithOutSuperAdmin();
}
