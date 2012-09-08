package ru.whereiswifi;

import java.util.List;

import ru.whereiswifi.MyLocation.LocationResult;

import android.app.IntentService;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

public class Service extends IntentService {

	public Service() {
		super("WhereIsFiWi service");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		U.w("service!!!");

		WifiManager wifi = (WifiManager) getSystemService(Context.WIFI_SERVICE);
		final List<ScanResult> list = wifi.getScanResults();

		MyLocation loc = new MyLocation();
		loc.getLocation(this, new LocationResult() {

			@Override
			public void gotLocation(Location l) {
				saveData(l.getLatitude(), l.getLongitude(), list);
			}
		});
	}

	private void saveData(double lat, double lon, List<ScanResult> list) {
		String latS = String.valueOf(lat);
		String lonS = String.valueOf(lon);
		for (ScanResult r : list) {
			U.w(r.toString());
			ContentValues v = new ContentValues();
			v.put("lat", latS);
			v.put("lon", lonS);
			v.put("ssid", r.SSID);
			v.put("bssid", r.BSSID);
			v.put("cap", r.capabilities);
			v.put("last", String.valueOf(System.currentTimeMillis()));

			DB.inst(this).saveSpot(v);
		}
	}
}
