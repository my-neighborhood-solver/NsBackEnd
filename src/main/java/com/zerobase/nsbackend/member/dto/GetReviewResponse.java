package com.zerobase.nsbackend.member.dto;

import com.zerobase.nsbackend.errand.domain.entity.Errand;
import com.zerobase.nsbackend.errand.domain.vo.ReviewGrade;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
public class GetReviewResponse {
    private Long errandId;
    private String errandTitle;
    private String grade;
    private String comment;
}
