package wi24rd.radar.vital.tcp.domain.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import wi24rd.radar.vital.tcp.protocol.FunctionEnum;

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
