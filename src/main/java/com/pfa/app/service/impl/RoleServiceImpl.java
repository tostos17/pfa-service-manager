package com.pfa.app.service.impl;

import com.pfa.app.dto.request.CreateRoleRequest;
import com.pfa.app.dto.response.ApiResponse;
import com.pfa.app.dto.response.RoleDto;
import com.pfa.app.model.Role;
import com.pfa.app.repository.RoleRepository;
import com.pfa.app.service.RoleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    @Override
    public ApiResponse<String> addRole(CreateRoleRequest createRoleRequest) {
        Role role = new Role();
        role.setName(createRoleRequest.getName().toUpperCase());

        try {
            roleRepository.save(role);
            return ApiResponse.created("Role created successfully");
        } catch (Exception e) {
            log.error(e.getMessage());
        }

        return null;
    }

    @Override
    public ApiResponse<List<RoleDto>> getRoles() {
        return ApiResponse.ok(roleRepository.fetchRoles());
    }

}
