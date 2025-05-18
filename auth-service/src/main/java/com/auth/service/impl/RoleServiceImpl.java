package com.auth.service.impl;

import com.auth.model.dto.AddRoleDto;
import com.auth.model.dto.RoleDto;
import com.auth.model.entities.Role;
import com.auth.repository.RoleRepository;
import com.auth.service.RoleService;
import com.customutility.model.CustomResponseEntity;
import jakarta.persistence.EntityManager;
import jakarta.persistence.ParameterMode;
import jakarta.persistence.StoredProcedureQuery;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
@RequiredArgsConstructor
public class RoleServiceImpl implements RoleService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RoleServiceImpl.class);
    private final RoleRepository roleRepository;
    private final EntityManager entityManager;
//    @Autowired
//    UserRepository userRepository;


//    public RoleServiceImpl(EntityManager entityManager) {
//        this.entityManager = entityManager;
//    }

    @Override
    public CustomResponseEntity AddRole(AddRoleDto addRoleDto) {
        if (addRoleDto.getName()==null || addRoleDto.getName().isBlank()) {
            LOGGER.info("data cannot be null");
            return CustomResponseEntity.error("data cannot be null");
        }
        if (roleRepository.existsByName(addRoleDto.getName())) {
            LOGGER.info("Role Already Exist");
            return CustomResponseEntity.error("Role Already Exist");
        }

        Role role = new Role();
        role.setName(addRoleDto.getName());
        roleRepository.save(role);
        return new CustomResponseEntity(roleRepository.save(role),"Role Added Successfully");
    }

//    public List<User> getUsersByRole(String roleName) {
//        StoredProcedureQuery query = entityManager.createStoredProcedureQuery("GetUsersByRole", User.class);
//
//        // Set input parameter
//        query.registerStoredProcedureParameter(1, String.class, ParameterMode.IN);
//        query.setParameter(1, roleName);
//
//        // Execute the stored procedure
//        return query.getResultList();
//    }

    @Override
    public CustomResponseEntity getAllRoles() {
        List<Role> roleList = roleRepository.findAllRolesWithOutSuperAdmin();
        return new CustomResponseEntity(roleList,"");
    }

    @Override
    public CustomResponseEntity deleteRoleById(Long roleId) {
        Role role = roleRepository.findById(roleId).orElse(null);
        if(Objects.isNull(role)){

            LOGGER.error("Role Does Not Exist With Id "  + roleId);
            return CustomResponseEntity.error(String.format("Role Does Not Exist With Id "  + roleId));
        }
        roleRepository.delete(role);
        LOGGER.error("Role Deleted Successfully With Id "  + roleId);
        return new CustomResponseEntity(String.format("Role Deleted Successfully With Id "  + roleId));
    }

    @Override
    public CustomResponseEntity getRoleById(Long roleId) {
        Role role = roleRepository.findById(roleId).orElse(null);
        if(role == null){

            LOGGER.error("Role Does Not Exist With Id "  + roleId);
            return CustomResponseEntity.error(String.format("Role Does Not Exist With Id "  + roleId));
        }
        RoleDto roleDto = new RoleDto();
        roleDto.setName(role.getName());
        roleDto.setId(role.getId());
        return new CustomResponseEntity(roleDto,"");

    }

    @Override
    public CustomResponseEntity updateRole(RoleDto roleDto) {
        if (roleRepository.existsByName(roleDto.getName())) {
            LOGGER.info("Role Already Exist");
            return CustomResponseEntity.error("Role Already Exist");
        }

        Role role = roleRepository.findById(roleDto.getId()).orElse(null);
        if(role == null){

            LOGGER.error("Role Does Not Exist With Id "  + roleDto.getId());
            return CustomResponseEntity.error(String.format("Role Does Not Exist With Id "  + roleDto.getId()));
        }
        role.setName(roleDto.getName());
        roleRepository.save(role);
        LOGGER.info(String.format("Role updated Successfully With Id "  + roleDto.getId()));
        return new CustomResponseEntity(role,String.format("Role updated Successfully With Id "  + roleDto.getId()));

    }

//    @Override
//    public CustomResponseEntity assignRole(Long userId, Long roleId) {
//        User user = userRepository.findById(userId).orElse(null);
//        if(user == null) {
//            LOGGER.error("User Not Found");
//            return CustomResponseEntity.error("User Not Found");
//        }
//        Role role = roleRepository.findById(roleId).orElse(null);
//        if(role == null) {
//            LOGGER.error("Role Not Found");
//            return CustomResponseEntity.error("Role Not Found");
//        }
//        user.getRoles().add(role);
//        userRepository.save(user);
//        LOGGER.error("Role Assign Successfully");
//        return new CustomResponseEntity(user,"Role Assign Successfully");
//    }
//
//    @Override
//    public CustomResponseEntity unAssignRole(Long userId, Long roleId) {
//        User user = userRepository.findById(userId).orElse(null);
//        if(user == null) {
//            LOGGER.error("User Not Found");
//            return CustomResponseEntity.error("User Not Found");
//        }
//        Role role = roleRepository.findById(roleId).orElse(null);
//        if(role == null) {
//            LOGGER.error("Role Not Found");
//            return CustomResponseEntity.error("Role Not Found");
//        }
//        user.getRoles().remove(role);
//        userRepository.save(user);
//        LOGGER.error("Role UnAssign Successfully");
//        return new CustomResponseEntity(user,"Role UnAssign Successfully");
//    }
}
