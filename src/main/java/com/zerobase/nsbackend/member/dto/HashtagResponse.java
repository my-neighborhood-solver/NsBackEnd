package com.zerobase.nsbackend.member.dto;

import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class HashtagResponse {
    private List<String> hashtag;

    private HashtagResponse(List<String> hashtags){
        this.hashtag = hashtags;
    }

    public static HashtagResponse of(List<String> hashtags){
        return new HashtagResponse(hashtags);
    }
}
