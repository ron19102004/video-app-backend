package com.video.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table(name = "otp")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class OTP {
    @Id
    private Long id;
    @JsonIgnore
    @OneToOne
    @MapsId
    @JoinColumn(name = "id")
    private User user;
    private String value;
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiredAt;
}
