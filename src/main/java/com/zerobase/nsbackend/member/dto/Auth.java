package com.zerobase.nsbackend.member.dto;

import com.sun.istack.NotNull;
import com.zerobase.nsbackend.member.domain.Members;
import lombok.Builder;
import lombok.Data;

public class Auth {
    @Data
    @Builder
    public static class SignUp{
        @NotNull
        private String name;
        @NotNull
        private String email;
        @NotNull
        private String password;
        @NotNull
        private String phone_number;
        @NotNull
        private String nickname;

        public Members toEntity(){
            return Members.builder()
                .name(this.name)
                .email(this.email)
                .password(this.password)
                .build();
        }
    }

    @Data
    @Builder
    public static class SignUpResponse{
        private Long id;
        private String name;
        private String email;
        private String password;
    }

    @Data
    @Builder
    public static class SignIn{
        @NotNull
        private String email;
        @NotNull
        private String password;
    }

}
