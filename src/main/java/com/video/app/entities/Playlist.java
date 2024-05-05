package com.video.app.entities;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "playlist")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Playlist extends BaseEntity {
    private String name;
    @Column(nullable = false)
    private Privacy privacy;
    @ColumnDefault("false")
    private Boolean deleted;
    private String image;

    @ManyToOne(fetch = FetchType.LAZY)
    @JsonIgnore
    @JoinColumn(name = "user_id", referencedColumnName = "id", nullable = false)
    private User user;
    @OneToMany(mappedBy = "playlist",fetch = FetchType.LAZY)
    private List<PlaylistVideo> playlistVideos;
}
