package com.video.app.services.impl;

import com.video.app.entities.User;
import com.video.app.entities.VIP;
import com.video.app.entities.VIPType;
import com.video.app.mail.MailService;
import com.video.app.repositories.VIPRepository;
import com.video.app.services.UserService;
import com.video.app.services.VIPService;
import com.video.app.utils.DataResponse;
import jakarta.mail.MessagingException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Transactional
@Service
public class VIPServiceImpl implements VIPService {
    @Autowired
    private VIPRepository vipRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private UserService userService;
    private static final long ONE_DAY_MS = 24 * 60 * 60000;
    @Autowired
    private MailService mailService;
    private ExecutorService executorService = Executors.newFixedThreadPool(6);
    private static final String MONTHS_PERMISSION = "1 3 6";

    @Override
    public VIP findLatestVIPByUserId(Long userId) {
        return this.vipRepository.findLatestVIPByUserId(userId);
    }

    @Override
    public VIP findLatestVIPBYUsername(String username) {
        return this.vipRepository.findLatestVIPByUsername(username);
    }

    @Override
    public boolean isExpired(Date expiredAt) {
        return expiredAt.before(new Date());
    }

    @Override
    public DataResponse register(String username, int month) {
        if (!MONTHS_PERMISSION.contains(month + "")) {
            return new DataResponse("Month incorrect!!! Just allowed 1,3,6", null, false);
        }
        User user = this.userService.findByUsername(username);
        if (user == null) return new DataResponse("User not found", null, false);
        VIP vip = this.vipRepository.findLatestVIPByUsername(username);
        if (vip != null && vip.getActive() && !this.isExpired(vip.getExpiredAt())) {
            return new DataResponse("Already have a vip ", null, false);
        }
        VIP vipNew = VIP.builder()
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiredAt(new Date(System.currentTimeMillis() + vipTypeToTime(month)))
                .user(user)
                .active(true)
                .build();
        VIP vipSave = this.vipRepository.save(vipNew);
        executorService.submit(() -> {
            try {
                this.mailService.sendMailRegisterVIP(user, vipSave);
            } catch (MessagingException e) {
                throw new RuntimeException(e);
            }
        });
        return new DataResponse("Register successfully!", vipSave, true);
    }

    private long vipTypeToTime(int month) {
        return switch (month) {
            case 1 -> 30 * ONE_DAY_MS;
            case 3 -> 3 * 30 * ONE_DAY_MS;
            case 6 -> 6 * 30 * ONE_DAY_MS;
            default -> throw new IllegalStateException("Unexpected value: " + month);
        };
    }

    @Override
    public VIP cancel(VIP vip) {
        if (vip == null) return null;
        vip.setActive(false);
        this.entityManager.merge(vip);
        return vip;
    }
}
