package ru.whereiswifi;

public class WifiSpot {
	public final Long id;
	public final Long globalId;
	public final String lat;
	public final String lon;
	public final String ssid;
	public final String lastDiscovered;

	public WifiSpot(Long id, Long globalId, String lat, String lon,
			String ssid, String lastDiscovered) {
		super();
		this.id = id;
		this.globalId = globalId;
		this.lat = lat;
		this.lon = lon;
		this.ssid = ssid;
		this.lastDiscovered = lastDiscovered;
	}
}
