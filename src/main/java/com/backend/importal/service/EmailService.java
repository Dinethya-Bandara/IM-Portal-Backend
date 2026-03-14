package com.backend.importal.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class EmailService {

//    public String generateOtp() {
//        SecureRandom random = new SecureRandom();
//        int otp = 100000 + random.nextInt(900000);
//        return String.valueOf(otp);
//    }
//
//
//    // Send OTP email
//    public String sendOtpEmail(String toEmail) {
//
//
//        String otp = generateOtp();
//
//
//        SimpleMailMessage message = new SimpleMailMessage();
//        message.setTo(toEmail);
//        message.setSubject("Your OTP Code");
//        message.setText(
//                "Your One-Time Password (OTP) is: " + otp + "\n\n" +
//                        "This OTP is valid for a limited time. Do not share it with anyone."
//        );
//
//        mailSender.send(message);
//        return otp;
//    }
}
