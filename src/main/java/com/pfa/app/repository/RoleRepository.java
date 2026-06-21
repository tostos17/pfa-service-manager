package com.pfa.app.repository;

import com.pfa.app.dto.response.RoleDto;
import com.pfa.app.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);

    @Query("SELECT new com.pfa.app.dto.response.RoleDto(r.name) FROM Role r")
    List<RoleDto> fetchRoles();
}