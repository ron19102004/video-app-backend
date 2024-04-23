package com.video.app.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "views")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class VIP extends BaseEntity {
    @Temporal(TemporalType.DATE)
    private Date issuedAt;
    @Temporal(TemporalType.DATE)
    private Date expiredAt;
    @ColumnDefault("false")
    private Boolean active;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;
}
