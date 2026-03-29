package com.hospital.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AuditLogDTO {
    private Long id;
    private LocalDateTime timestamp;
    private String username;
    private String action;
    private String entityType;
    private String entityId;
    private String ipAddress;
    private String status;
    private String errorMessage;
}
