package com.zerobase.nsbackend.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

public class Interests {
    @Getter
    @AllArgsConstructor
    @Builder
    public static class interestBoardResponse{
        private Long errandId;
        private String errandTitle;
    }

}
