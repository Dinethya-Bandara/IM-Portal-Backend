package com.backend.importal.service;

import com.backend.importal.entity.Candidate;
import com.backend.importal.entity.Role;
import com.backend.importal.entity.User;
import com.backend.importal.repository.CandidateRepository;
import com.backend.importal.repository.UserRepository;
import com.backend.importal.repository.RoleRepository;
import com.backend.importal.util.EncryptUtil;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    public CandidateService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    // Save candidate
    public Candidate saveCandidate(Candidate candidate) {
        return candidateRepository.save(candidate);
    }

    // Get all candidates
    public List<Candidate> getAllCandidates(){
        return candidateRepository.findAll();
    }

    // Update status (approve/reject)
    public Candidate updateStatus(Long id, String status) {

        Candidate candidate = candidateRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Candidate not found " + id));

        if (status == null) {
            throw new RuntimeException("Status cannot be null");
        }

        System.out.println("Updating ID: " + id + " to " + status);

        candidate.setStatus(status);

        return candidateRepository.save(candidate);
    }

}