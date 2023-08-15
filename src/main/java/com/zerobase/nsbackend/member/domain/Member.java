package com.zerobase.nsbackend.member.domain;

import com.zerobase.nsbackend.errand.domain.entity.Errand;
import com.zerobase.nsbackend.global.BaseTimeEntity;
import com.zerobase.nsbackend.member.type.Authority;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
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
    @Enumerated(EnumType.STRING)
    private Authority authority;
    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "member_address_id")
    private MemberAddress memberAddress;
    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, mappedBy = "member")
    private List<InterestBoard> interestBoards = new ArrayList<>();
    @Builder.Default
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "member_hashtag")
    private Set<MemberHashtag> hashtags =
        Collections.newSetFromMap(new ConcurrentHashMap<>());
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
    public void deleteInterestBoard(Errand errand){
        this.interestBoards.remove(InterestBoard.toEntity(this,errand));
    }
    public List<String> getHashtags(){
        return this.hashtags.stream().map(MemberHashtag::getContent).collect(Collectors.toList());
    }
    public void addHashtag(String content){
        hashtags.add(MemberHashtag.of(content));
    }
    public void deleteHashtag(String content){
        hashtags.remove(MemberHashtag.of(content));
    }
    public boolean existHashtag(String content){
        return hashtags.contains(MemberHashtag.of(content));
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
