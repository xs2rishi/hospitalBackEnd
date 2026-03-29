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
public class AuditLogFilter {
    private String username;
    private String action;
    private String entityType;
    private String status;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
}
