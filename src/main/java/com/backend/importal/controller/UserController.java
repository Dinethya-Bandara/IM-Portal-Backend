package com.backend.importal.controller;

import com.backend.importal.dto.LoginRequestDTO;
import com.backend.importal.dto.LoginResponseDTO;
import com.backend.importal.service.UserService;
import com.backend.importal.util.EncryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
@CrossOrigin(origins = "*", allowedHeaders = "*")
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

    @PostMapping("/testEnc")
    public String getEncryptedString(@RequestParam String data) throws Exception {
        return EncryptUtil.encrypt(data);
    }

    @PostMapping("/getOtp")
    public String getOtpCode(@RequestParam String email){
        return userService.generateOtp(email);
    }

    @PostMapping("/useOtp")
    public String useOtpCode(@RequestParam String email, @RequestParam String otp){
        return userService.useOtp(email, otp);
    }

    @PostMapping("/create-users")
    public String createUsers() {
        try {
            return userService.createUsersFromApprovedCandidates();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error creating users!";
        }
    }

}
