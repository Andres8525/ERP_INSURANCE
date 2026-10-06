package com.erp.insurance.claims;

import com.erp.insurance.domain.DomainEnums.ClaimStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.EnumMap;
import java.util.List;
import java.util.Map;

@Service
public class ClaimsService {
    private final ClaimRepository claims;

    public ClaimsService(ClaimRepository claims) {
        this.claims = claims;
    }

    @Transactional(readOnly = true)
    public Map<ClaimStatus, List<ClaimsController.ClaimResponse>> pipeline() {
        Map<ClaimStatus, List<ClaimsController.ClaimResponse>> pipeline = new EnumMap<>(ClaimStatus.class);
        for (ClaimStatus status : ClaimStatus.values()) {
            pipeline.put(status, claims.findByStatusOrderByCreatedAtDesc(status).stream()
                    .map(ClaimsController.ClaimResponse::from).toList());
        }
        return pipeline;
    }
}