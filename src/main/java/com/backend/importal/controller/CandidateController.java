package com.backend.importal.controller;

import com.backend.importal.entity.Candidate;
import com.backend.importal.service.CandidateService;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/candidates")
@CrossOrigin(origins = "*")
public class CandidateController {

    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @PostMapping
    public Candidate createCandidate(@RequestBody Candidate candidate){
        candidate.setStatus("PENDING");
        return candidateService.saveCandidate(candidate);
    }

    @GetMapping
    public List<Candidate> getAllCandidates(){
        return candidateService.getAllCandidates();
    }

    @PutMapping("/{id}/status")
    public Candidate updateStatus(
            @PathVariable Long id,
            @RequestParam String status){

        return candidateService.updateStatus(id, status);
    }
}