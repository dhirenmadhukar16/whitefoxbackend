package com.example.whitefox.customers.controller;

import com.example.whitefox.admin.entity.SupportTicket;
import com.example.whitefox.admin.entity.SupportTicketReply;
import com.example.whitefox.admin.repository.SupportTicketReplyRepository;
import com.example.whitefox.admin.repository.SupportTicketRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/customer/support")
@CrossOrigin("*")
public class CustomerSupportController {

    @Autowired
    private SupportTicketRepository supportTicketRepository;

    @Autowired
    private SupportTicketReplyRepository replyRepository;

    @GetMapping("/{customerId}")
    public ResponseEntity<List<SupportTicket>> getMyTickets(@PathVariable UUID customerId) {
        return ResponseEntity.ok(supportTicketRepository.findByCustomerId(customerId));
    }

    @PostMapping("/{customerId}")
    public ResponseEntity<SupportTicket> createTicket(@PathVariable UUID customerId, @RequestBody SupportTicket ticket) {
        ticket.setCustomerId(customerId);
        return ResponseEntity.ok(supportTicketRepository.save(ticket));
    }

    @GetMapping("/{customerId}/tickets/{ticketId}/replies")
    public ResponseEntity<List<SupportTicketReply>> getReplies(@PathVariable UUID customerId, @PathVariable UUID ticketId) {
        return ResponseEntity.ok(replyRepository.findByTicketIdOrderByTimestampAsc(ticketId));
    }

    @PostMapping("/{customerId}/tickets/{ticketId}/replies")
    public ResponseEntity<SupportTicketReply> addReply(@PathVariable UUID customerId, @PathVariable UUID ticketId, @RequestBody SupportTicketReply reply) {
        reply.setTicketId(ticketId);
        reply.setSenderId(customerId);
        reply.setSenderType("CUSTOMER");
        return ResponseEntity.ok(replyRepository.save(reply));
    }
}
