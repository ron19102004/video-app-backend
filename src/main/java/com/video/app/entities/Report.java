package com.video.app.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "reports")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Report extends BaseEntity {
    private String content;
    private String email;
    @ColumnDefault("false")
    private Boolean checked;
}
