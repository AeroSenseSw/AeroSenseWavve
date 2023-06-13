
package wi24rd.radar.vital.tcp.exception;

/**
 *  
 *
 * @author： jia.w@aerosnese.com
 * @date： 2021/8/6 11:02
 * @version: 1.0
 */
public class RadarException extends RuntimeException {
    public RadarException() {
    }

    public RadarException(String message) {
        super(message);
    }

    public RadarException(String message, Throwable cause) {
        super(message, cause);
    }

    public RadarException(Throwable cause) {
        super(cause);
    }

    public RadarException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
