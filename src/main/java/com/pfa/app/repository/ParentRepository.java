package com.pfa.app.repository;

import com.pfa.app.model.Parent;
import com.pfa.app.dto.response.ParentResponseDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ParentRepository extends JpaRepository<Parent, Long> {

    @Query("SELECT new com.pfa.app.dto.response.ParentResponseDto(" +
            "p.id, p.title, p.firstName, p.lastName, p.phone, p.email, p.address, p.registrationDate) " +
            "FROM Parent p")
    Page<ParentResponseDto> findAllParentsAsDto(Pageable pageable);
}
