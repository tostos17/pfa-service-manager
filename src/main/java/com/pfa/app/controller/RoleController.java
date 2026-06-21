package com.pfa.app.controller;

import com.pfa.app.constants.StringValues;
import com.pfa.app.dto.request.CreateRoleRequest;
import com.pfa.app.dto.response.ApiResponse;
import com.pfa.app.dto.response.RoleDto;
import com.pfa.app.service.RoleService;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping(StringValues.ROLE_PATH)
@RequiredArgsConstructor
@Tag(name = "Role Management")
public class RoleController {

    private final RoleService roleService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<String> addRole(HttpServletRequest httpServletRequest, @RequestBody @Valid CreateRoleRequest createRoleRequest) {
        return roleService.addRole(createRoleRequest);
    }

    @GetMapping
    public ApiResponse<List<RoleDto>> addRole() {
        return roleService.getRoles();
    }
}
