package com.zerobase.nsbackend.member.dto;

import com.zerobase.nsbackend.errand.domain.vo.ReviewGrade;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class GetReviewResponse {
    private String grade;
    private String comment;
}
