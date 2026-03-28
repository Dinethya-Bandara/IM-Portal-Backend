package com.backend.importal.service;

import com.backend.importal.dto.CandidateDTO;
import com.backend.importal.entity.Candidate;
import com.backend.importal.entity.Role;
import com.backend.importal.entity.User;
import com.backend.importal.repository.CandidateRepository;
import com.backend.importal.repository.UserRepository;
import com.backend.importal.repository.RoleRepository;
import com.backend.importal.util.EncryptUtil;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
public class CandidateService {

    private final CandidateRepository candidateRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private Cloudinary cloudinary;

    public CandidateService(CandidateRepository candidateRepository) {
        this.candidateRepository = candidateRepository;
    }

    // Save candidate
    public Candidate saveCandidate(CandidateDTO candidateDto) throws IOException {
        System.out.println(candidateDto);
        Candidate newCandidate = new Candidate();

        newCandidate.setFirstName(candidateDto.getFirstName());
        newCandidate.setLastName(candidateDto.getLastName());
        newCandidate.setStudentNumber(candidateDto.getStudentNumber());
        newCandidate.setUniversityEmail(candidateDto.getUniversityEmail());
        newCandidate.setPersonalEmail(candidateDto.getPersonalEmail());
        newCandidate.setContactNumber(candidateDto.getContactNumber());
        newCandidate.setBatch(candidateDto.getBatch());
        newCandidate.setLevel(candidateDto.getLevel());

        newCandidate.setStatus("PENDING");

        if (candidateDto.getStudentIdImage() != null) {
            Map uploadResult = cloudinary.uploader().upload(candidateDto.getStudentIdImage().getBytes(), ObjectUtils.asMap(
                    "folder", "candidates"
            ));
            newCandidate.setStudentIdUrl((String) uploadResult.get("secure_url"));
        }
        Optional<Role> newRole = roleRepository.findByRoleName(candidateDto.getRole());
        newCandidate.setRole(newRole.get());

        return candidateRepository.save(newCandidate);
    }

    // Get all candidates
    public List<Candidate> getAllCandidatesByStatus(String status){
        return candidateRepository.findByStatus(status);
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