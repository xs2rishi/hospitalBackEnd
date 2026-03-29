package com.hospital.service;

import com.hospital.dto.AuditLogDTO;
import com.hospital.dto.AuditLogFilter;
import com.hospital.entity.AuditLog;
import com.hospital.repository.AuditLogRepository;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuditLogService {

    private static final Logger logger = LoggerFactory.getLogger(AuditLogService.class);

    @Autowired
    private AuditLogRepository auditLogRepository;

    public void logAction(String action, String entityType, String entityId, String status, String errorMessage,
            String requestDetails) {
        try {
            String username = getCurrentUsername();
            HttpServletRequest request = getCurrentRequest();

            String ipAddress = null;
            String userAgent = null;

            if (request != null) {
                ipAddress = getClientIpAddress(request);
                userAgent = request.getHeader("User-Agent");
            }

            AuditLog auditLog = AuditLog.builder()
                    .timestamp(LocalDateTime.now())
                    .username(username)
                    .action(action)
                    .entityType(entityType)
                    .entityId(entityId)
                    .ipAddress(ipAddress)
                    .userAgent(userAgent)
                    .status(status)
                    .errorMessage(errorMessage)
                    .requestDetails(requestDetails)
                    .build();

            auditLogRepository.save(auditLog);
            logger.debug("Audit log created: action={}, user={}, status={}", action, username, status);
        } catch (Exception e) {
            logger.error("Failed to create audit log", e);
        }
    }

    public void logSuccess(String action, String entityType, String entityId) {
        logAction(action, entityType, entityId, "SUCCESS", null, null);
    }

    public void logFailure(String action, String entityType, String entityId, String errorMessage) {
        logAction(action, entityType, entityId, "FAILURE", errorMessage, null);
    }

    public void logError(String action, String entityType, String entityId, String errorMessage) {
        logAction(action, entityType, entityId, "ERROR", errorMessage, null);
    }

    public List<AuditLogDTO> getAllLogs() {
        return auditLogRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AuditLogDTO> getLogsByUsername(String username) {
        return auditLogRepository.findByUsernameOrderByTimestampDesc(username).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AuditLogDTO> getLogsByAction(String action) {
        return auditLogRepository.findByActionOrderByTimestampDesc(action).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AuditLogDTO> getLogsByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        return auditLogRepository.findByTimestampBetween(startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public List<AuditLogDTO> getLogsByFilter(AuditLogFilter filter) {
        return auditLogRepository.findByFilters(
                filter.getUsername(),
                filter.getAction(),
                filter.getEntityType(),
                filter.getStatus(),
                filter.getStartDate(),
                filter.getEndDate()).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    private AuditLogDTO convertToDTO(AuditLog auditLog) {
        return AuditLogDTO.builder()
                .id(auditLog.getId())
                .timestamp(auditLog.getTimestamp())
                .username(auditLog.getUsername())
                .action(auditLog.getAction())
                .entityType(auditLog.getEntityType())
                .entityId(auditLog.getEntityId())
                .ipAddress(auditLog.getIpAddress())
                .status(auditLog.getStatus())
                .errorMessage(auditLog.getErrorMessage())
                .build();
    }

    private String getCurrentUsername() {
        try {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication != null && authentication.isAuthenticated()
                    && !"anonymousUser".equals(authentication.getPrincipal())) {
                return authentication.getName();
            }
        } catch (Exception e) {
            logger.debug("Could not get current username", e);
        }
        return "anonymous";
    }

    private HttpServletRequest getCurrentRequest() {
        try {
            ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder
                    .getRequestAttributes();
            if (attributes != null) {
                return attributes.getRequest();
            }
        } catch (Exception e) {
            logger.debug("Could not get current request", e);
        }
        return null;
    }

    private String getClientIpAddress(HttpServletRequest request) {
        String[] headerNames = {
                "X-Forwarded-For",
                "Proxy-Client-IP",
                "WL-Proxy-Client-IP",
                "HTTP_X_FORWARDED_FOR",
                "HTTP_X_FORWARDED",
                "HTTP_X_CLUSTER_CLIENT_IP",
                "HTTP_CLIENT_IP",
                "HTTP_FORWARDED_FOR",
                "HTTP_FORWARDED",
                "HTTP_VIA",
                "REMOTE_ADDR"
        };

        for (String header : headerNames) {
            String ip = request.getHeader(header);
            if (ip != null && !ip.isEmpty() && !"unknown".equalsIgnoreCase(ip)) {
                return ip.split(",")[0].trim();
            }
        }

        return request.getRemoteAddr();
    }
}
