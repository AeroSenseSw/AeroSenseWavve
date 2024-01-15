package com.aerosense.radar.tcp.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author jia.wu
 * @version 1.0.0
 * @description: FallDetectDto
 * @date 2024/1/11 17:19
 */
@Data
@AllArgsConstructor
public class FallDetectDto {

    private Float x;
    private Float y;
}
