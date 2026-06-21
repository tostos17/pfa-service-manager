package com.pfa.app.repository;

import com.pfa.app.model.Term;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TermRepository extends JpaRepository<Term, Long> {
    List<Term> findBySessionId(Long sessionId);
}