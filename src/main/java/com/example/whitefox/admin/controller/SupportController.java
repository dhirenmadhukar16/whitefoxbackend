package com.example.whitefox.admin.controller;

import com.example.whitefox.admin.entity.SupportTicket;
import com.example.whitefox.admin.repository.SupportTicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/admin-panel/support")
@CrossOrigin("*")
public class SupportController {

    @Autowired
    private SupportTicketRepository supportTicketRepository;

    @GetMapping
    public ResponseEntity<List<SupportTicket>> getTickets() {
        return ResponseEntity.ok(supportTicketRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<SupportTicket> createTicket(@RequestBody SupportTicket ticket) {
        return ResponseEntity.ok(supportTicketRepository.save(ticket));
    }
}
