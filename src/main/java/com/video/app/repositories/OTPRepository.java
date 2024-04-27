package com.video.app.repositories;

import com.video.app.entities.OTP;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OTPRepository extends JpaRepository<OTP,Long> {
}
