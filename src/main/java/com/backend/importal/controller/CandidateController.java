package com.backend.importal.controller;

import com.backend.importal.dto.CandidateDTO;
import com.backend.importal.entity.Candidate;
import com.backend.importal.repository.CandidateRepository;
import com.backend.importal.service.CandidateService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@RestController
@RequestMapping("/api/candidates")
@CrossOrigin(origins = "http://localhost:5173")
public class CandidateController {

    @Autowired
    private CandidateRepository candidateRepository;

    private final CandidateService candidateService;

    public CandidateController(CandidateService candidateService) {
        this.candidateService = candidateService;
    }

    @PostMapping(value = "/create", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public Candidate createCandidate(@ModelAttribute CandidateDTO candidateDto) throws IOException {
        return candidateService.saveCandidate(candidateDto);
    }

    @GetMapping("/getAllPending")
    public List<Candidate> getAllPendingCandidates(){
        return candidateService.getAllCandidatesByStatus("PENDING");
    }

    @GetMapping("/getAllApproved")
    public List<Candidate> getAllApprovedCandidates(){
        return candidateService.getAllCandidatesByStatus("APPROVED");
    }

    @PutMapping("/{id}/status")
    public Candidate updateStatus(
            @PathVariable Long id,
            @RequestParam String status){

        return candidateService.updateStatus(id, status);
    }
}