package com.video.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "vip")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VIP extends BaseEntity {
    @Temporal(TemporalType.TIMESTAMP)
    private Date issuedAt;
    @Temporal(TemporalType.TIMESTAMP)
    private Date expiredAt;
    @ColumnDefault("false")
    private Boolean active;
    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;
}
