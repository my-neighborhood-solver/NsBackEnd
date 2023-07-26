package com.zerobase.nsbackend.auth.dto;

import com.sun.istack.NotNull;
import com.zerobase.nsbackend.member.domain.Member;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

public class Auth {
    @Getter
    @Setter
    @Builder
    public static class SignUp{
        @NotNull
        private String nickname;
        @NotNull
        private String email;
        @NotNull
        private String password;

        public Member toEntity(){
            return Member.builder()
                .nickname(this.nickname)
                .email(this.email)
                .password(this.password)
                .isSocialLogin(false)
                .isDeleted(false)
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
        @NotNull
        private String email;
        @NotNull
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
