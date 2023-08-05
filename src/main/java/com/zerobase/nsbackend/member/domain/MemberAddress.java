package com.zerobase.nsbackend.member.domain;

import com.zerobase.nsbackend.global.BaseTimeEntity;
import com.zerobase.nsbackend.member.dto.PutUserAddressRequest;
import javax.persistence.Column;
import javax.persistence.Entity;
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

@Entity
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Builder
public class MemberAddress extends BaseTimeEntity {
    @Id @Column(name = "member_address_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @OneToOne(mappedBy = "memberAddress")
    private Member member;
    private String streetNameAddress;
    private Float latitude;//위도
    private Float longitude;//경도
    private boolean permission;

    public void updateUserAddress(float latitude, float longitude, String streetNameAddress){
        this.latitude = latitude;
        this.longitude = longitude;
        this.streetNameAddress = streetNameAddress;
    }
}
