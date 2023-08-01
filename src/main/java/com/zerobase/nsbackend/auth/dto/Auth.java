package com.zerobase.nsbackend.auth.dto;

import com.zerobase.nsbackend.member.domain.Member;
import com.zerobase.nsbackend.member.domain.MemberAddress;
import com.zerobase.nsbackend.member.type.Authority;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

public class Auth {
    @Getter
    @Setter
    @Builder
    public static class SignUp{
        @NotBlank
        private String nickname;
        @NotBlank
        private String email;
        @NotBlank
        private String password;

        public Member toEntity(MemberAddress memberAddress){
            return Member.builder()
                .nickname(this.nickname)
                .email(this.email)
                .password(this.password)
                .isSocialLogin(false)
                .isDeleted(false)
                .authority(Authority.ROLE_USER)
                .memberAddress(memberAddress)
                .build();
        }
    }

    @Getter
    @Setter
    @Builder
    public static class SignUpResponse{
        private Long id;
        private String nickname;
        private String email;
    }

    @Getter
    @Setter
    @Builder
    public static class SignIn{
        @Email
        private String email;
        @NotBlank
        private String password;
    }

    @Getter
    @Setter
    @Builder
    public static class SignInResponse{
        private Long id;
        private String nickname;
        private String email;
        private String token;
    }

}
