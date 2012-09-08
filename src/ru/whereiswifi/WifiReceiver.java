package ru.whereiswifi;

import java.util.List;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

public class WifiReceiver extends BroadcastReceiver {
	@SuppressLint({ "ShowToast", "NewApi", "NewApi", "NewApi" })
	@Override
	public void onReceive(final Context context, Intent intent) {
		final String action = intent.getAction();
		U.w("start processing...");

		NotificationManager nm = (NotificationManager) context
				.getSystemService(Context.NOTIFICATION_SERVICE);
		Notification n = new Notification(android.R.drawable.star_on,
				"wifi scan results enabled", System.currentTimeMillis());

		CharSequence contentTitle = "Whre is wi-fi?";
		CharSequence contentText = "It is here, bro!";
		Intent notificationIntent = new Intent(context, MainActivity.class);
		PendingIntent contentIntent = PendingIntent.getActivity(context, 0,
				notificationIntent, 0);

		n.setLatestEventInfo(context, contentTitle, contentText, contentIntent);

		nm.notify(1, n);

		// service
		Intent serviceIntent = new Intent(context, Service.class);
		context.startService(serviceIntent);
	}
}
