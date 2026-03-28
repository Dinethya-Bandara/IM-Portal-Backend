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

    @PostMapping("/create_users")
    public String createUsersFromApprovedCandidates() throws Exception {
        return userService.createUsersFromApprovedCandidates();
    }

    @PostMapping("/testEnc")
    public String getEncryptedString(@RequestParam String data) throws Exception {
        return EncryptUtil.encrypt(data);
    }

    @PostMapping("/getOtp")
    public String getOtpCode(@RequestParam String email){
        return userService.generateOtp(email);
    }

    @PostMapping("/useOtp")
    public ResponseEntity<String> useOtpCode(
            @RequestParam String email,
            @RequestParam String otp){

        String result = userService.useOtp(email, otp);

        if(result.equals("OTP Accepted!")){
            return ResponseEntity.ok(result);
        } else {
            return ResponseEntity.badRequest().body(result);
        }
    }

    @PostMapping("/resend-otp")
    public ResponseEntity<String> resendOtp(@RequestParam String email) {
        String result = userService.generateOtp(email);
        return ResponseEntity.ok(result);
    }

    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody ResetPasswordRequestDTO request) {
        String response = userService.resetPassword(request.getEmail(), request.getNewPassword());
        return ResponseEntity.ok(response);
    }

    @GetMapping("/stats")
    public UserStatsDTO getUserStats() {
        return userService.getUserStats();
    }

    @GetMapping("/student-status")
    public ResponseEntity<UserStatusDTO> getStudentStatus() {
        return ResponseEntity.ok(userService.getStudentStatus());
    }

}
