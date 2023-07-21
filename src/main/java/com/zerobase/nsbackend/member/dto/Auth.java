package com.zerobase.nsbackend.member.dto;

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
        private String name;
        @NotNull
        private String email;
        @NotNull
        private String password;
        @NotNull
        private String phoneNumber;
        @NotNull
        private String nickname;

        public Member toEntity(){
            return Member.builder()
                .name(this.name)
                .email(this.email)
                .password(this.password)
                .build();
        }
    }

    @Getter
    @Setter
    @Builder
    public static class SignUpResponse{
        private Long id;
        private String name;
        private String email;
        private String password;
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
        private String name;
        private String email;
    }

}
