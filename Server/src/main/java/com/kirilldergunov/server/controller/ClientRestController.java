package com.kirilldergunov.server.controller;

import com.kirilldergunov.server.entity.Client;
import com.kirilldergunov.server.service.ClientService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/clients")
public class ClientRestController {

    private final ClientService service;

    public ClientRestController(ClientService service) {
        this.service = service;
    }

    @PostMapping
    public Client create(@RequestBody Client client) {
        return service.save(client);
    }
}