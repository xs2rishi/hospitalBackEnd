package com.hospital.aspect;

import com.hospital.annotation.Audited;
import com.hospital.service.AuditLogService;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Aspect
@Component
public class AuditLogAspect {

    private static final Logger logger = LoggerFactory.getLogger(AuditLogAspect.class);

    @Autowired
    private AuditLogService auditLogService;

    @Around("@annotation(com.hospital.annotation.Audited)")
    public Object auditMethod(ProceedingJoinPoint joinPoint) throws Throwable {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();
        Audited audited = method.getAnnotation(Audited.class);

        String action = audited.action();
        String entityType = audited.entityType();
        String entityId = null;

        long startTime = System.currentTimeMillis();
        Object result = null;
        Throwable exception = null;

        try {
            logger.debug("Executing audited method: {}", method.getName());
            result = joinPoint.proceed();

            // Try to extract entity ID from result if it's an entity with getId method
            if (result != null && entityId == null) {
                try {
                    Method getIdMethod = result.getClass().getMethod("getId");
                    Object id = getIdMethod.invoke(result);
                    if (id != null) {
                        entityId = id.toString();
                    }
                } catch (Exception e) {
                    // Ignore if getId method doesn't exist
                }
            }

            auditLogService.logSuccess(action, entityType, entityId);
            return result;
        } catch (Throwable e) {
            exception = e;
            auditLogService.logError(action, entityType, entityId, e.getMessage());
            throw e;
        } finally {
            long executionTime = System.currentTimeMillis() - startTime;
            logger.debug("Audited method {} completed in {}ms, status: {}",
                    method.getName(), executionTime, exception == null ? "SUCCESS" : "ERROR");
        }
    }
}
