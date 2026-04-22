package com.backend.importal.service;

import com.backend.importal.dto.LoginRequestDTO;
import com.backend.importal.dto.LoginResponseDTO;
import com.backend.importal.dto.UserStatsDTO;
import com.backend.importal.dto.UserStatusDTO;
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
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static com.backend.importal.util.EncryptUtil.encrypt;


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

    @Autowired
    private EmailService emailService;

    @Autowired
    private ErrorLogService errorLogService;

    public LoginResponseDTO authenticateUser(LoginRequestDTO loginRequestDTO) throws Exception {

        // Find user by email
        Optional<User> optionalUser = userRepository.findByPersonalEmail(loginRequestDTO.email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            // Decrypt stored password
            String decryptedPassword = EncryptUtil.decrypt(user.getPassword());

            // Compare passwords
            if (decryptedPassword.equals(loginRequestDTO.password)) {
                return new LoginResponseDTO(true, user.getName(), "Login Success!", user.getBatch(), user.getRole().getRoleName(), user.getLevel());
            } else {
                return new LoginResponseDTO(false, null, "Incorrect Password", null, null, null);
            }

        } else {
            return new LoginResponseDTO(false, null, "User not found", null, null, null);
        }
    }

    //Generate OTP and send to email
    public String generateOtp(String email) {

        // Check if user exists
        Optional<User> userEmail = userRepository.findByPersonalEmail(email);

        if(userEmail.isEmpty()){
            return "You are not registered to the IM Portal yet.\nPlease fill the detail form.";
        }

        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000);
        String otpCode = String.valueOf(otp);

        // Expire previous OTPs
        List<Otp> activeOtps = otpRepository.findByEmailAndExpired(email, false);
        if (!activeOtps.isEmpty()) {
            activeOtps.forEach(oldOtp -> oldOtp.setExpired(true));
            otpRepository.saveAll(activeOtps);
        }

        Otp otpRecord = new Otp();
        otpRecord.setEmail(email);
        otpRecord.setOtpCode(otpCode);
        otpRepository.save(otpRecord);

        emailService.sendOtpEmail(email, otpCode);

        System.out.printf("OTP for %s is %s%n", email, otpCode);

        return "OTP sent successfully";

    }

    //Verify OTP entered by user
    public String useOtp(String email, String enteredOtp) {

        Optional<Otp> otpOptional =
                otpRepository.findTopByEmailOrderByGeneratedTimeDesc(email);

        if (otpOptional.isEmpty()) {
            return "No OTP found!";
        }

        Otp currentOtp = otpOptional.get();

        // Check if already used
        if (currentOtp.isExpired()) {
            return "OTP already used!";
        }

        // Check expiry time (5 minutes)
        if (currentOtp.getGeneratedTime().plusMinutes(5).isBefore(LocalDateTime.now())) {

            currentOtp.setExpired(true);
            otpRepository.save(currentOtp);

            return "OTP expired!";
        }

        if (!currentOtp.getOtpCode().equals(enteredOtp)) {
            return "Incorrect OTP!";
        }

        // Mark as used
        currentOtp.setExpired(true);
        otpRepository.save(currentOtp);

        return "OTP Accepted!";
    }

    //Create user accounts from approved candidates
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
            user.setPassword(encrypt("1234"));

            user.setRole(c.getRole());

            userList.add(user);
        }
        userRepository.saveAll(userList);

        // Mark candidates as account created
        for(Candidate c : candidates){
            c.setAccountCreated(true);
        }
        candidateRepository.saveAll(candidates);

        return "User accounts created successfully!";
    }

    public String resetPassword(String email, String newPassword){
        Optional<User> userOptional = userRepository.findByPersonalEmail(email);

        if(userOptional.isEmpty()){
            return "User Not Found";
        }
        User user = userOptional.get();

        try {
            // Encrypt new password
            String encryptedPassword = encrypt(newPassword);
            user.setPassword(encryptedPassword);
            userRepository.save(user);

        } catch (Exception e) {
            // Log error
            errorLogService.logError(e, "UserService", "resetPassword");
            e.printStackTrace();
            return "Error while encrypting password";
        }

        return "Password updated successfully";
    }

    //Get user statistics
    public UserStatsDTO getUserStats() {
        long total = userRepository.count();
        long students = userRepository.countStudents();
        long lecturers = userRepository.countLecturers();
        long juniorStaff = userRepository.countJuniorStaff();

        return new UserStatsDTO(total, students, lecturers, juniorStaff);
    }

    //Get current academic start year
    public int getCurrentAcademicStartYear() {
        LocalDate now = LocalDate.now();
        int year = now.getYear();

        if (now.getMonthValue() < 9) {
            return year - 1;
        }
        return year;
    }

    //Get student active/inactive status
    public UserStatusDTO getStudentStatus() {

        List<User> users = userRepository.findAll();

        // Step 1: Filter valid student users
        List<User> studentUsers = users.stream()
                .filter(user ->
                        user.getRole() != null &&
                                user.getRole().getMasterRole() != null &&
                                user.getRole().getMasterRole().getRoleName() != null &&
                                user.getRole().getMasterRole().getRoleName().equalsIgnoreCase("STUDENT")
                )
                .filter(user -> user.getBatch() != null)
                .toList();

        // Find current academic year (MAX year from DB)
        int currentStartYear = studentUsers.stream()
                .map(user -> {
                    try {
                        return Integer.parseInt(user.getBatch().split("/")[0]);
                    } catch (Exception e) {
                        errorLogService.logError(e, "UserService", "getStudentStatus");
                        return 0;
                    }
                })
                .max(Integer::compare)
                .orElse(0);

        int minActiveYear = currentStartYear - 3;

        long activeStudents = studentUsers.stream()
                .filter(user -> {
                    try {
                        int startYear = Integer.parseInt(user.getBatch().split("/")[0]);
                        return startYear >= minActiveYear;
                    } catch (Exception e) {
                        errorLogService.logError(e, "UserService", "getStudentStatus");
                        return false;
                    }
                })
                .count();

        long inactiveStudents = studentUsers.stream()
                .filter(user -> {
                    try {
                        int startYear = Integer.parseInt(user.getBatch().split("/")[0]);
                        return startYear < minActiveYear;
                    } catch (Exception e) {
                        errorLogService.logError(e, "UserService", "getStudentStatus");
                        return false;
                    }
                })
                .count();

        return new UserStatusDTO(activeStudents, inactiveStudents);
    }
}