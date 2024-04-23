package com.video.app.entities;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "otp")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OTP extends BaseEntity {
    @OneToOne(mappedBy = "otp",fetch = FetchType.LAZY)
    private User user;
    private String value;
    @Temporal(TemporalType.DATE)
    private Date expiredAt;
}
