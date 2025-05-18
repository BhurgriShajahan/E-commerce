package com.auth.service;


import com.auth.model.dto.AddRoleDto;
import com.auth.model.dto.RoleDto;
import com.customutility.model.CustomResponseEntity;

public interface RoleService {
    CustomResponseEntity<?> AddRole(AddRoleDto addRoleDto);

    CustomResponseEntity<?> getAllRoles();

    CustomResponseEntity<?> deleteRoleById(Long roleId);

    CustomResponseEntity<?> getRoleById(Long roleId);

    CustomResponseEntity<?> updateRole(RoleDto roleDto);

//    CustomResponseEntity<?> assignRole(Long userId, Long roleId);
//
//    CustomResponseEntity<?> unAssignRole(Long userId, Long roleId);
}
