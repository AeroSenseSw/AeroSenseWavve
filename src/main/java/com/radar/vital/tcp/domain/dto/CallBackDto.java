package com.radar.vital.tcp.domain.dto;

import com.radar.vital.tcp.protocol.FunctionEnum;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**=
 * @author ：ywb
 * @date ：Created in 2022/12/6 9:18
 * @modified By：
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CallBackDto {

    private String radarId;
    private FunctionEnum functionEnum;
    private Object data;
    private String radarVersion;

}
