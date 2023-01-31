package com.aerosense.radar.tcp.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * @author ：ywb
 * @date ：Created in 2022/12/6 9:26
 * @modified By：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ReportDto {

    private float breathBpm;
    private float breathLine;
    private float heartBpm;
    private float heartLine;
    private float distance;
    private float signalIntensity;
    private float state;

}
