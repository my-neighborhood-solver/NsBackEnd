package com.zerobase.nsbackend.member.domain;

import com.zerobase.nsbackend.errand.domain.entity.Errand;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class InterestBoard{
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "member_id")
    private Member member;
    @ManyToOne
    @JoinColumn(name = "errand_id")
    private Errand errand;

    public InterestBoard(Member member, Errand errand) {
        this.member = member;
        this.errand = errand;
    }

    public static InterestBoard toEntity(Member member, Errand errand){
        return new InterestBoard(member,errand);
    }

    @Override
    public boolean equals(Object obj){
        if(this == obj){
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        InterestBoard other = (InterestBoard) obj;
        return other.member.getId().equals(member.getId())
            && other.errand.getId().equals(errand.getId());
    }

}
