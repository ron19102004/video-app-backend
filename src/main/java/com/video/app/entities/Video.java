package com.video.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.Date;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "videos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Video extends BaseEntity {
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String slug;
    @Column(nullable = false)
    private String duration;
    @Lob
    @Column(nullable = false,columnDefinition = "TEXT")
    private String description;
    @Column(nullable = false)
    private String tag;
    @ColumnDefault("false")
    private Boolean deleted;
    @Temporal(TemporalType.DATE)
    @Column(nullable = false)
    private Date release;
    private Boolean vip;
    @Column(nullable = false)
    private Privacy privacy;
    private String image;
    @Column(columnDefinition = "TEXT")
    private String src;

    @ManyToOne
    @JoinColumn(name = "category_id", referencedColumnName = "id", nullable = false)
    private Category category;
    @ManyToOne
    @JoinColumn(name = "country_id", referencedColumnName = "id", nullable = false)
    private Country country;
    @ManyToOne
    @JoinColumn(name = "uploader_id", referencedColumnName = "id", nullable = false)
    private User uploader;

    @OneToMany(mappedBy = "video",fetch = FetchType.LAZY)
    @JsonIgnore
    private List<View> views;
    @JsonIgnore
    @OneToMany(mappedBy = "video",fetch = FetchType.LAZY)
    private List<PlaylistVideo> playlistVideos;
    @JsonIgnore
    @OneToMany(mappedBy = "video",fetch = FetchType.LAZY)
    private List<Comment> comments;
}
