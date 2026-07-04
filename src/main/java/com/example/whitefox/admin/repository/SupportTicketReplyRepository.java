package com.example.whitefox.admin.repository;

import com.example.whitefox.admin.entity.SupportTicketReply;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;
import java.util.UUID;

public interface SupportTicketReplyRepository extends JpaRepository<SupportTicketReply, UUID> {
    List<SupportTicketReply> findByTicketIdOrderByTimestampAsc(UUID ticketId);
}
