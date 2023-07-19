package com.zerobase.nsbackend.member.dto;

import com.sun.istack.NotNull;
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
