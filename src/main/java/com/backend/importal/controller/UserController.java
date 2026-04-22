package com.backend.importal.controller;

import com.backend.importal.dto.*;
import com.backend.importal.service.UserService;
import com.backend.importal.util.EncryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "http://localhost:5173", allowedHeaders = "*")
public class UserController {

    @Autowired
    private UserService userService;

    //Login API
    @PostMapping("/login")
    public LoginResponseDTO login(@RequestBody LoginRequestDTO request) {
        try {
            LoginResponseDTO response =  userService.authenticateUser(request);
            return response;
        } catch (Exception e) {
            System.out.print(e);
            return null;
        }
    }

    //Create users from approved candidates
    @PostMapping("/create_users")
    public String createUsersFromApprovedCandidates() throws Exception {
        return userService.createUsersFromApprovedCandidates();
    }

    //Test encryption method
    @PostMapping("/testEnc")
    public String getEncryptedString(@RequestParam String data) throws Exception {
        // Encrypts given data using utility class
        return EncryptUtil.encrypt(data);
    }

    //Generate OTP for email
    @PostMapping("/getOtp")
    public String getOtpCode(@RequestParam String email){
        return userService.generateOtp(email);
    }

    //Verify OTP
    @PostMapping("/useOtp")
    public String useOtpCode(@RequestBody LoginRequestDTO otpRequest){
        return userService.useOtp(otpRequest.getEmail(), otpRequest.getOtp());
    }

    //Resend OTP
    @PostMapping("/resend-otp")
    public ResponseEntity<String> resendOtp(@RequestParam String email) {
        String result = userService.generateOtp(email);
        return ResponseEntity.ok(result);
    }

    //Reset password using email and new password
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequestDTO request) {
        String response = userService.resetPassword(request.getEmail(), request.getNewPassword());
        return ResponseEntity.ok(response);
    }

    //Get system user statistics
    @GetMapping("/stats")
    public UserStatsDTO getUserStats() {
        return userService.getUserStats();
    }

    //Get student status details
    @GetMapping("/student-status")
    public ResponseEntity<UserStatusDTO> getStudentStatus() {
        return ResponseEntity.ok(userService.getStudentStatus());
    }

}
