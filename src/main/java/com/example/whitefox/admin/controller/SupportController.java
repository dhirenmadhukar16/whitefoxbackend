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

    @Autowired
    private com.example.whitefox.admin.repository.SupportTicketReplyRepository replyRepository;

    @GetMapping
    public ResponseEntity<List<SupportTicket>> getTickets() {
        return ResponseEntity.ok(supportTicketRepository.findAll());
    }

    @PostMapping
    public ResponseEntity<SupportTicket> createTicket(@RequestBody SupportTicket ticket) {
        return ResponseEntity.ok(supportTicketRepository.save(ticket));
    }

    @GetMapping("/{id}/replies")
    public ResponseEntity<List<com.example.whitefox.admin.entity.SupportTicketReply>> getReplies(@PathVariable java.util.UUID id) {
        return ResponseEntity.ok(replyRepository.findByTicketIdOrderByTimestampAsc(id));
    }

    @PostMapping("/{id}/replies")
    public ResponseEntity<com.example.whitefox.admin.entity.SupportTicketReply> addReply(@PathVariable java.util.UUID id, @RequestBody com.example.whitefox.admin.entity.SupportTicketReply reply) {
        reply.setTicketId(id);
        reply.setSenderType("ADMIN");
        return ResponseEntity.ok(replyRepository.save(reply));
    }
}
