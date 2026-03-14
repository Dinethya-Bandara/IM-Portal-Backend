package com.backend.importal.service;

import com.backend.importal.dto.LoginRequestDTO;
import com.backend.importal.dto.LoginResponseDTO;
import com.backend.importal.entity.Otp;
import com.backend.importal.entity.User;
import com.backend.importal.repository.OtpRepository;
import com.backend.importal.repository.UserRepository;
import com.backend.importal.util.EncryptUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;


@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private OtpRepository otpRepository;

    public LoginResponseDTO authenticateUser(LoginRequestDTO loginRequestDTO) throws Exception {
        Optional<User> optionalUser = userRepository.findByEmail(loginRequestDTO.email);

        if (optionalUser.isPresent()) {
            User user = optionalUser.get();

            if (user.getPassword().equals(EncryptUtil.encrypt(loginRequestDTO.password))) {
                return new LoginResponseDTO(true, user.getName(), "Login Success!", user.getBatch(), user.getRole().getRoleName());
            } else {
                return new LoginResponseDTO(false, null, "Incorrect Password", null, null);
            }

        }
        else {
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
}

