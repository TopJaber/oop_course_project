package com.kirilldergunov.server.service;

import com.kirilldergunov.server.dao.ClientRepository;
import com.kirilldergunov.server.entity.Client;
import org.springframework.stereotype.Service;

@Service
public class ClientService {

    private final ClientRepository repo;

    public ClientService(ClientRepository repo) {
        this.repo = repo;
    }

    public Client save(Client client) {
        return repo.save(client);
    }
}
