package com.pfa.app.service;

import com.pfa.app.dto.request.CreateRoleRequest;
import com.pfa.app.dto.response.ApiResponse;
import com.pfa.app.dto.response.RoleDto;

import java.util.List;

public interface RoleService {
    ApiResponse<String> addRole(CreateRoleRequest createRoleRequest);

    ApiResponse<List<RoleDto>> getRoles();
}
