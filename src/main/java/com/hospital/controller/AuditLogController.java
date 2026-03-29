package com.hospital.controller;

import com.hospital.dto.AuditLogDTO;
import com.hospital.dto.AuditLogFilter;
import com.hospital.service.AuditLogService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/audit-logs")
@CrossOrigin(origins = "*")
@Tag(name = "Audit Logs", description = "Audit log management endpoints")
@SecurityRequirement(name = "bearerAuth")
public class AuditLogController {

    @Autowired
    private AuditLogService auditLogService;

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get all audit logs", description = "Retrieve all audit logs (Admin only)")
    public ResponseEntity<List<AuditLogDTO>> getAllLogs() {
        return ResponseEntity.ok(auditLogService.getAllLogs());
    }

    @GetMapping("/user/{username}")
    @PreAuthorize("hasRole('ADMIN') or #username == authentication.name")
    @Operation(summary = "Get logs by username", description = "Retrieve audit logs for a specific user")
    public ResponseEntity<List<AuditLogDTO>> getLogsByUsername(@PathVariable String username) {
        return ResponseEntity.ok(auditLogService.getLogsByUsername(username));
    }

    @GetMapping("/action/{action}")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get logs by action", description = "Retrieve audit logs for a specific action (Admin only)")
    public ResponseEntity<List<AuditLogDTO>> getLogsByAction(@PathVariable String action) {
        return ResponseEntity.ok(auditLogService.getLogsByAction(action));
    }

    @GetMapping("/date-range")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Get logs by date range", description = "Retrieve audit logs within a date range (Admin only)")
    public ResponseEntity<List<AuditLogDTO>> getLogsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate) {
        return ResponseEntity.ok(auditLogService.getLogsByDateRange(startDate, endDate));
    }

    @PostMapping("/filter")
    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Filter audit logs", description = "Retrieve audit logs based on multiple filter criteria (Admin only)")
    public ResponseEntity<List<AuditLogDTO>> filterLogs(@RequestBody AuditLogFilter filter) {
        return ResponseEntity.ok(auditLogService.getLogsByFilter(filter));
    }
}
