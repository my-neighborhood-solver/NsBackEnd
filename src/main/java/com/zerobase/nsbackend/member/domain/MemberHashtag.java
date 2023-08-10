package com.zerobase.nsbackend.member.domain;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class MemberHashtag {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String content;

    private MemberHashtag(String content){this.content = content;}
    public static MemberHashtag of(String content){return new MemberHashtag(content);}

    @Override
    public boolean equals(Object o){
        if(this == o){
            return  true;
        }
        if(o == null || getClass() != o.getClass()){
            return false;
        }
        MemberHashtag that = (MemberHashtag) o;
        return content.equals(that.content);
    }

    @Override
    public int hashCode(){
        int hash = 7;
        hash = 31 * hash + (content == null ? 0 : content.hashCode());
        return hash;
    }

}
