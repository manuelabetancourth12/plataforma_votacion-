package com.voting.infrastructure.rest;

import com.voting.application.VotingService;
import com.voting.domain.model.CandidateId;
import com.voting.domain.model.IpAddress;
import com.voting.domain.model.RegionId;
import com.voting.domain.model.Vote;
import com.voting.domain.model.VoterId;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/** Controlador REST que expone POST /vote para recibir y validar votos entrantes. */
@RestController
@RequestMapping("/vote")
public class VoteController {

    private final VotingService votingService;

    public VoteController(VotingService votingService) {
        this.votingService = votingService;
    }

    @PostMapping
    public ResponseEntity<String> castVote(
            @Valid @RequestBody VoteRequestDto request,
            HttpServletRequest httpRequest) {
        try {
            String clientIp = resolveClientIp(httpRequest);
            Vote vote = Vote.cast(
                    new VoterId(request.voterId()),
                    new CandidateId(request.candidateId()),
                    new RegionId(request.regionId()),
                    new IpAddress(clientIp)
            );

            votingService.processVote(vote);
            return ResponseEntity.ok("Voto registrado con éxito.");

        } catch (SecurityException e) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(e.getMessage());
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    private String resolveClientIp(HttpServletRequest request) {
        String forwardedFor = request.getHeader("X-Forwarded-For");
        if (forwardedFor != null && !forwardedFor.isBlank()) {
            return forwardedFor.split(",")[0].trim();
        }
        String remoteAddr = request.getRemoteAddr();
        return (remoteAddr != null && !remoteAddr.isBlank()) ? remoteAddr : "unknown";
    }
}
