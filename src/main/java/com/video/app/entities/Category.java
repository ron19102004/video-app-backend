package com.video.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "categories")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Category extends BaseEntity{
    private String name;
    private String slug;
    private String image;
    @ColumnDefault("false")
    @JsonIgnore
    private Boolean deleted;

    @OneToMany(mappedBy = "category",fetch = FetchType.LAZY)
    private List<Video> videos;
}
