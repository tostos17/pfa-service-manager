package com.pfa.app.repository;

import com.pfa.app.model.Invoice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    List<Invoice> findByAccountPlayerPlayerId(String playerId);
}