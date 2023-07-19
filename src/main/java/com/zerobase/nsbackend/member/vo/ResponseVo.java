package com.zerobase.nsbackend.member.vo;

import com.zerobase.nsbackend.member.type.ResponseStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ResponseVo<T> {
    private final ResponseStatus status;
    private final String message;
    private final T data;
}
