package com.backend.importal.service;

import com.backend.importal.dto.LoginRequestDTO;
import com.backend.importal.dto.LoginResponseDTO;
import com.backend.importal.entity.Candidate;
import com.backend.importal.entity.Otp;
import com.backend.importal.entity.Role;
import com.backend.importal.entity.User;
import com.backend.importal.repository.CandidateRepository;
import com.backend.importal.repository.OtpRepository;
import com.backend.importal.repository.RoleRepository;
import com.backend.importal.repository.UserRepository;
import com.backend.importal.util.EncryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    private CandidateRepository candidateRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpRepository otpRepository;

    public LoginResponseDTO authenticateUser(LoginRequestDTO loginRequestDTO) throws Exception {

        Optional<User> optionalUser = userRepository.findByPersonalEmail(loginRequestDTO.email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            String decryptedPassword = EncryptUtil.decrypt(user.getPassword());

            if (decryptedPassword.equals(loginRequestDTO.password)) {
                return new LoginResponseDTO(true, user.getName(), "Login Success!", user.getBatch(), user.getRole().getRoleName());
            } else {
                return new LoginResponseDTO(false, null, "Incorrect Password", null, null);
            }

        } else {
            return new LoginResponseDTO(false, null, "User not found", null, null);
        }
    }

    public String generateOtp(String email) {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);
        String otpCode = String.valueOf(otp);

        List<Otp> activeOtps = otpRepository.findByEmailAndExpired(email, false);
        if (!activeOtps.isEmpty()) {
            activeOtps.forEach(oldOtp -> oldOtp.setExpired(true));
            otpRepository.saveAll(activeOtps);
        }

        Otp otpRecord = new Otp();
        otpRecord.setEmail(email);
        otpRecord.setOtpCode(otpCode);
        otpRepository.save(otpRecord);

        System.out.printf("OTP for %s  is %s%n", email, otpCode);

        return otpCode;
    }

    public String useOtp(String email, String otp){
        Otp currentOtp = otpRepository.findByEmailAndOtpCodeAndExpired(email, otp, false, Limit.of(1));
        if(currentOtp != null){
            currentOtp.setExpired(true);
            otpRepository.save(currentOtp);
            return "OTP Accepted!";
        }else{
            return "Incorrect OTP!";
        }
    }

    public String createUsersFromApprovedCandidates() throws Exception {

        List<Candidate> candidates =
                candidateRepository.findByStatusAndAccountCreated("APPROVED", false);

        if (candidates.isEmpty()) {
            return "No approved candidates found!";
        }

        List<User> userList = new ArrayList<>();

        for (Candidate c : candidates) {

            // Prevent duplicate users
            if (userRepository.existsByPersonalEmail(c.getPersonalEmail())) {
                continue;
            }

            User user = new User();

            // Combine first + last name
            String fullName = c.getFirstName() + " " + c.getLastName();
            user.setName(fullName);

            user.setPersonalEmail(c.getPersonalEmail());
            user.setUniversityEmail(c.getUniversityEmail());
            user.setContactNumber(c.getContactNumber());
            user.setBatch(c.getBatch());
            user.setLevel(c.getLevel());

            // Default password (change later to Bcrypt)
            user.setPassword(EncryptUtil.encrypt("1234"));

            user.setRole(c.getRole());

            userList.add(user);

            //userRepository.save(user);

            // Mark candidate as processed
//            c.setAccountCreated(true);
//            candidateRepository.save(c);
        }
        userRepository.saveAll(userList);

        for(Candidate c : candidates){
            c.setAccountCreated(true);
        }
        candidateRepository.saveAll(candidates);

        return "User accounts created successfully!";
    }
}

