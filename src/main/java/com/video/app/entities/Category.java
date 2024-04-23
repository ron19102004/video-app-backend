package com.video.app.entities;

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
    private Boolean deleted;

    @OneToMany(mappedBy = "category")
    private List<Video> videos;
}
