package com.fairy.rocketmq.domain;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.io.Serializable;

/**
 * @author 鹿少年
 * @version 1.0
 * @date 2022/5/29 15:56
 */
@Data
@AllArgsConstructor
public class Order implements Serializable {
    private Integer orderId;
    private Integer productId;
    private Integer productNum;
    private Double orderAmount;
}
