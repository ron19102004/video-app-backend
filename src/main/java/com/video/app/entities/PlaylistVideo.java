package com.video.app.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "playlist_videos")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PlaylistVideo extends BaseEntity {
    @ColumnDefault("false")
    private Boolean deleted;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "video_id", referencedColumnName = "id", nullable = false)
    private Video video;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "playlist_id", referencedColumnName = "id", nullable = false)
    private Playlist playlist;
}
