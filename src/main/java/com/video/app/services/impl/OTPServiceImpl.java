package com.video.app.services.impl;

import com.amazonaws.services.cognitoidp.model.UserNotFoundException;
import com.video.app.entities.OTP;
import com.video.app.entities.User;
import com.video.app.repositories.OTPRepository;
import com.video.app.repositories.UserRepository;
import com.video.app.services.OTPService;
import com.video.app.utils.DataResponse;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class OTPServiceImpl implements OTPService {
    @Autowired
    private OTPRepository otpRepository;
    @Autowired
    private UserRepository userRepository;
    private final long EXPIRATION_TIME_MS = 1 * 60000;
    @PersistenceContext
    private EntityManager entityManager;

    private String generateOTP(int length) {
        StringBuilder otp = new StringBuilder();
        ThreadLocalRandom random = ThreadLocalRandom.current();
        for (int i = 0; i < length; i++) {
            otp.append(random.nextInt(10));
        }
        return otp.toString();
    }

    @Transactional
    @Override
    public String generate(String email) {
        User user = this.userRepository.findByEmail(email);
        if (user == null) throw new UserNotFoundException("User not found");
        String otp = this.generateOTP(6);
        OTP otpEntity = this.otpRepository.findById(user.getId()).orElse(null);
        if (otpEntity == null) {
            this.otpRepository.save(OTP.builder()
                    .user(user)
                    .value(otp)
                    .expiredAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS))
                    .build());
            return otp;
        }
        otpEntity.setValue(otp);
        otpEntity.setExpiredAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME_MS));
        this.entityManager.merge(otpEntity);
        return otp;
    }

    @Override
    public DataResponse isOTPValid(String otp, User user) {
        OTP _otp = this.otpRepository.findById(user.getId()).orElse(null);
        if (_otp == null)
            return new DataResponse("OTP not found", null, false);
        if (_otp.getValue().equals(otp) && !isExpired(_otp))
            return new DataResponse("Verified successfully!", null, true);
        else return new DataResponse("Verified fail!", null, false);
    }

    private boolean isExpired(OTP otp) {
        return otp.getExpiredAt().before(new Date());
    }
}
