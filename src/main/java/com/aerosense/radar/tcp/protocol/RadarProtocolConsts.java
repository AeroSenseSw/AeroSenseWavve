package com.aerosense.radar.tcp.protocol;

import com.google.common.collect.Range;

/**
 * @author jia.wu
 */
public class RadarProtocolConsts {

    public static final int RET_SUCCESS = 1;
    public static final int RET_FAILURE = 0;

    /**防摔雷达高度1.4米*/
    public static final float INSTALL_HEIGHT_1_4 = 1.4f;
    /**防摔雷达高度2.2米*/
    public static final float INSTALL_HEIGHT_2_2 = 2.2f;
    /**Wavve Pro雷达高度2.8米*/
    public static final float INSTALL_HEIGHT_2_8 = 2.8f;
    /**Assure雷达安装高度范围*/
    public static final Range<Float> ASSURE_INSTALL_HEIGHT_RANGE = Range.closed(INSTALL_HEIGHT_1_4, INSTALL_HEIGHT_2_2);
    /**防摔雷达安装模式-侧装*/
    public static final int INSTALL_MODE_SIDE_WALL = 0;
    /**呼吸心率雷达安装模式-床头安装*/
    public static final int INSTALL_MODE_HEADBOARD = 1;
    /**呼吸心率雷达安装模式-顶部安装*/
    public static final int INSTALL_MODE_CEILING = 2;

    /**呼吸心率雷达安装高度默认值：1米*/
    public static final float INSTALL_HEIGHT_HEADBOARD_DEFAULT= 1F;
    /**呼吸心率雷达安装高度默认值：2.5米*/
    public static final float INSTALL_HEIGHT_CEILING_DEFAULT_2 = 2.5F;
    /**呼吸心率雷达床头安装高度范围*/
    public static final Range<Float> WAVVE_INSTALL_HEIGHT_CEILING_RANGE = Range.closed(0.8f, 1.2f);
    /**防摔雷达安装高度默认值：1.4米*/
    public static final float INSTALL_HEIGHT_AF_DEFAULT = 1.4F;

    /**算法关闭状态值*/
    public static final int ALGORITHM_STATUS_CLOSE = 0;
    /**算法打开状态值*/
    public static final int ALGORITHM_STATUS_OPEN = 1;

    /**算法打开状态值-翻生报警*/
    public static final int ALGORITHM_STATUS_TURN_OVER_OPEN = 1;
    /**算法打开状态值-坐立报警*/
    public static final int ALGORITHM_STATUS_SIT_UP_OPEN = 2;
    /**算法打开状态值-翻生、坐立报警*/
    public static final int ALGORITHM_STATUS_TURN_OVER_AND_SIT_UP_OPEN = 3;

    /**翻生报警值*/
    public static final int ALARM_TURN_OVER_VALUE = 0;
    /**坐立报警值*/
    public static final int ALARM_SIT_UP_VALUE = 1;

    /**上报时间间隔单位：50毫秒*/
    public static final int REPORT_TIME_UNIT = 50;
    /**
     * 设置目标距离
     * 0：不设置，自动匹配
     * >0：设置距离单位m
     */
    public static final int RADAR_TARGET_DISTANCE_AUTO = 0;

    /**
     * 雷达在线
     */
    public static final int RADAR_ONLINE = 0;
    /**
     * 雷达离线
     */
    public static final int RADAR_OFFLINE = 1;

    /**
     * sin60°的值，用于计算呼吸训练雷达的检测目标距离
     */
    public static final float SIN_60 = 0.8660254037844386F;

    /**
     * 临时bind版本号
     */
    public static final String TEMP_BIND_VERSION = "temp_bind";

    /**
     * 防摔雷达工作距离最大值
     */
    public static final float ANTI_FALL_WORK_RANGE_MAX_VALUE =  7.0f;

    /**
     * 防摔雷达工作距离最小值
     */
    public static final float ANTI_FALL_WORK_RANGE_MIN_VALUE =  1.0f;
}
