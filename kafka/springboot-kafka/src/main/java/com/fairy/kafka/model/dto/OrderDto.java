package com.fairy.kafka.model.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.io.Serializable;

/**
 * @author hll
 * @version 1.0
 * @date 2022/7/6 14:36
 */
@AllArgsConstructor
@Builder
@Data
public class OrderDto implements Serializable {
    private Integer orderId;
    private Integer productId;
    private Integer productNum;
    private Double orderAmount;
}
