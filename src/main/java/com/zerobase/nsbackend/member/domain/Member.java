package com.zerobase.nsbackend.member.domain;

import com.zerobase.nsbackend.global.BaseTimeEntity;
import com.zerobase.nsbackend.member.type.Authority;
import java.util.ArrayList;
import java.util.Collection;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class Member extends BaseTimeEntity implements UserDetails {
    @Id @Column(name = "member_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String email;
    private Boolean isSocialLogin;
    private String password;
    private String nickname;
    private String profileImage;
    private String hashTag;
    @Enumerated(EnumType.STRING)
    private Authority authority;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "memberAddress_id")
    private MemberAddress memberAddress;
    private boolean isDeleted;

    public void updateUserNickname(String nickname){
        this.nickname = nickname;
    }
    public void updateUserImg(String img){
        this.profileImage = img;
    }
    public void deleteUser(){
        this.isDeleted = true;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority memberAuthority = new SimpleGrantedAuthority(authority.name());
        Collection<GrantedAuthority> authorities = new ArrayList<>();
        authorities.add(memberAuthority);
        return authorities;
    }

    @Override
    public String getUsername() {
        return null;
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
        return !isDeleted;
    }
}
