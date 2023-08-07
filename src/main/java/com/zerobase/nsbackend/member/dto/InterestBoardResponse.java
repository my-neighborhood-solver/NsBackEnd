package com.zerobase.nsbackend.member.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@AllArgsConstructor
@Builder
public class InterestBoardResponse {
    private Long errandId;
    private String errandTitle;
}
