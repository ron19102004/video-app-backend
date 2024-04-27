package com.video.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "countries")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Country extends BaseEntity{
    private String name;
    private String slug;
    @ColumnDefault("false")
    @JsonIgnore
    private Boolean deleted;

    @OneToMany(mappedBy = "country", fetch = FetchType.LAZY)
    private List<Video> videos;
}
