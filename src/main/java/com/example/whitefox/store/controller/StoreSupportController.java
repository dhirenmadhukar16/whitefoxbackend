package com.example.whitefox.store.controller;

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
@RequestMapping("/api/store/support")
@CrossOrigin("*")
public class StoreSupportController {

    @Autowired
    private SupportTicketRepository supportTicketRepository;

    @Autowired
    private SupportTicketReplyRepository replyRepository;

    @GetMapping("/{storeId}")
    public ResponseEntity<List<SupportTicket>> getStoreTickets(@PathVariable UUID storeId) {
        return ResponseEntity.ok(supportTicketRepository.findByStoreId(storeId));
    }

    @GetMapping("/{storeId}/tickets/{ticketId}/replies")
    public ResponseEntity<List<SupportTicketReply>> getReplies(@PathVariable UUID storeId, @PathVariable UUID ticketId) {
        return ResponseEntity.ok(replyRepository.findByTicketIdOrderByTimestampAsc(ticketId));
    }

    @PostMapping("/{storeId}/tickets/{ticketId}/replies")
    public ResponseEntity<SupportTicketReply> addReply(@PathVariable UUID storeId, @PathVariable UUID ticketId, @RequestBody SupportTicketReply reply) {
        reply.setTicketId(ticketId);
        reply.setSenderId(storeId);
        reply.setSenderType("STORE_STAFF");
        return ResponseEntity.ok(replyRepository.save(reply));
    }
}
