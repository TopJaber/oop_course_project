package com.kirilldergunov.server.dao;

import com.kirilldergunov.server.entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClientRepository extends JpaRepository<Client, Long> {
}
