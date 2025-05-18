package com.auth.controller;

import com.auth.model.dto.AddRoleDto;
import com.auth.model.dto.RoleDto;
import com.auth.service.RoleService;
import com.customutility.model.CustomResponseEntity;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

//@Controller
@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/role")
public class RoleController {

    private final RoleService roleService;


    @PostMapping("/addRole")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CustomResponseEntity addRole(@RequestBody AddRoleDto addRoleDto)
    {
        return this.roleService.AddRole(addRoleDto);
    }
    @GetMapping("/getAllRoles")
//    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public CustomResponseEntity getAllRoles(){

        return this.roleService.getAllRoles();
    }
    @DeleteMapping("/deleteRole/{roleId}")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CustomResponseEntity deleteRoleById(@PathVariable Long roleId){

        return this.roleService.deleteRoleById(roleId);
    }
    @GetMapping("/getRoleById/{roleId}")
//    @PreAuthorize("hasAnyRole('ROLE_USER', 'ROLE_ADMIN')")
    public CustomResponseEntity getRoleById(@PathVariable Long roleId){

        return this.roleService.getRoleById(roleId);
    }
    @PutMapping(value = "/updateRole")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
    public CustomResponseEntity updateRole(@RequestBody RoleDto roleDto) {
        return this.roleService.updateRole(roleDto);
    }

//    @PostMapping("/assignRole/{userId}")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public CustomResponseEntity assignRole(@PathVariable Long userId,@RequestParam Long roleId)
//    {
//
//        return this.roleService.assignRole(userId, roleId);
//    }
//
//    @PostMapping("/unAssignRole/{userId}")
//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    public CustomResponseEntity unAssignRole(@PathVariable Long userId,@RequestParam Long roleId)
//    {
//
//        return this.roleService.unAssignRole(userId, roleId);
//    }
}
