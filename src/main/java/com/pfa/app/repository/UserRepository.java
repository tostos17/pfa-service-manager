package com.pfa.app.repository;

import com.pfa.app.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    @Query(value = "SELECT nextval('admin_id_seq')", nativeQuery = true)
    Long getNextAdminSequenceValue();
}