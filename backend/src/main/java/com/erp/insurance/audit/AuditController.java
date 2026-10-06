package com.erp.insurance.audit;

import com.erp.insurance.domain.DomainEnums.AnomalyResolutionStatus;
import com.erp.insurance.domain.DomainEnums.AnomalySeverity;
import com.erp.insurance.domain.DomainEnums.AnomalyType;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.erp.insurance.audit.AuditDtos.*;

@RestController
@RequestMapping("/api/audit")
@PreAuthorize("hasAnyRole('ADMIN', 'AUDITOR')")
public class AuditController {
    private final AuditService audit;

    public AuditController(AuditService audit) { this.audit = audit; }

    @GetMapping("/anomalies")
    public Page<AnomalyResponse> anomalies(@RequestParam(defaultValue = "0") @Min(0) int page,
                                           @RequestParam(defaultValue = "25") @Min(1) @Max(100) int size,
                                           @RequestParam(required = false) AnomalySeverity severity,
                                           @RequestParam(required = false) AnomalyResolutionStatus resolutionStatus,
                                           @RequestParam(required = false) AnomalyType type) {
        return audit.list(page, size, severity, resolutionStatus, type);
    }

    @PostMapping("/resolve-batch")
    public ResolveBatchResponse resolveBatch(@Valid @RequestBody ResolveBatchRequest request,
                                              JwtAuthenticationToken authentication) {
        return audit.resolveBatch(request, UUID.fromString(authentication.getToken().getSubject()));
    }
}