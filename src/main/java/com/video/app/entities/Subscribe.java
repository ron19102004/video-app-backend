package com.video.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "subscribes")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Subscribe extends BaseEntity {
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "user_subscribe_id", referencedColumnName = "id", nullable = false)
    private User subscribe;
    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "user_be_subscribed_id", referencedColumnName = "id", nullable = false)
    private User subscribed;
}
