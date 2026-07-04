package com.example.whitefox.realtime.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class RealtimeEvent {

    private String type;

    private String title;

    private String message;

    private UUID referenceId;

    private String status;

    private LocalDateTime createdAt;
}