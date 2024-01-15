package com.aerosense.radar.tcp.connection;

/**
 * 
 *
 * @author： jia.w@aerosnese.com
 * @date： 2021/8/23 10:24
 * @version: 1.0
 */
public abstract class AbsractRadarAddressMap implements RadarAddressMap{

	private String serverAddress;

	public AbsractRadarAddressMap(String serverAddress) {
		this.serverAddress = serverAddress;
	}

	@Override
	public String getServerAddress() {
		return serverAddress;
	}

	public void setServerAddress(String serverAddress) {
		this.serverAddress = serverAddress;
	}
}
