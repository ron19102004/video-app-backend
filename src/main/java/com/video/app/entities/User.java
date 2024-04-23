package com.video.app.entities;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.ColumnDefault;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "users")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User extends BaseEntity implements UserDetails {
    @Column(nullable = false)
    private String fullName;
    @Column(nullable = false)
    private String phone;
    @Column(nullable = false)
    private String email;
    @Column(nullable = false)
    private String username;
    @Column(nullable = false)
    private String password;
    @ColumnDefault("false")
    private Boolean confirmed;
    @Column(nullable = false)
    private Role role;

    @OneToOne(cascade = CascadeType.ALL,fetch = FetchType.LAZY)
    @JoinColumn(name = "otp", referencedColumnName = "id")
    private OTP otp;
    @OneToMany(mappedBy = "uploader",fetch = FetchType.LAZY)
    private List<Video> videos;
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<View> views;
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<Playlist> playlists;
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<Comment> comments;
    @OneToMany(mappedBy = "follower",fetch = FetchType.LAZY)
    private List<Follow> followers;
    @OneToMany(mappedBy = "followed",fetch = FetchType.LAZY)
    private List<Follow> listFollowed;
    @OneToMany(mappedBy = "user",fetch = FetchType.LAZY)
    private List<VIP> vips;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(this.role.toString()));
    }

    @Override
    public String getPassword() {
        return this.password;
    }

    @Override
    public String getUsername() {
        return this.username;
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
