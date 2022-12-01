package com.fairy.kafka.utils;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class CommonResponse<T>{
    T data;
    static boolean success;
    Integer code;
    String message;

    public static CommonResponse success() {
        return CommonResponse.builder().code(200).message("success").build();
    }
}
