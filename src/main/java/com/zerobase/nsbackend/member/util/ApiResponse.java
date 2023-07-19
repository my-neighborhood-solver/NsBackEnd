package com.zerobase.nsbackend.member.util;

import com.zerobase.nsbackend.member.type.ErrorCode;
import com.zerobase.nsbackend.member.type.ResponseStatus;
import com.zerobase.nsbackend.member.vo.ResponseVo;

public class ApiResponse {
    public static <T> ResponseVo<T> SUCCESS (ErrorCode message, T data) {
        return new ResponseVo(ResponseStatus.SUCCESS, message.getDescription(), data);
    }

    public static <T>ResponseVo<T> ERROR (ErrorCode message, T data) {
        return new ResponseVo(ResponseStatus.ERROR, message.getDescription(), data);
    }
}
