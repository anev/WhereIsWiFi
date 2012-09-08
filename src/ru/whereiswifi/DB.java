package ru.whereiswifi;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DB {
	private Context context;
	private SQLiteDatabase db;
	private OpenHelper openHelper;

	private static DB instance;

	// const
	public static final String SPOT_TABLE = "Spot";

	public static DB inst(Context context) {
		if (instance == null) {
			instance = new DB(context);
		}
		return instance;
	}

	private DB(Context context) {
		this.context = context;
		openHelper = new OpenHelper(this.context);
		this.db = openHelper.getWritableDatabase();
	}

	public void saveSpot(ContentValues v) {
		if (!existSpot(v.getAsString("bssid"))) {
			db.insert(SPOT_TABLE, null, v);
			U.i("saved to " + SPOT_TABLE + ": " + v.toString());
		} else {
			U.d("spot with the bssid already exist: " + v.getAsString("bssid"));
			// todo обновить дату
		}
	}

	public boolean existSpot(String bssid) {
		Cursor c = db.rawQuery("select id from " + SPOT_TABLE
				+ " where bssid = ?", new String[] { bssid });
		return c.moveToFirst();
	}

	public void cleareSpots() {
		db.execSQL("DELETE FROM " + SPOT_TABLE + ";");
	}

	public List<ContentValues> listSpots() {
		List<ContentValues> list = new ArrayList<ContentValues>();

		Cursor cursor = this.db.query(SPOT_TABLE, new String[] { "id",
				"global_id", "lat", "lon", "ssid", "bssid", "last", "cap" },
				null, null, null, null, null);
		if (cursor.moveToFirst()) {
			do {
				list.add(cursor2values(cursor));
			} while (cursor.moveToNext());
		}

		return list;
	}

	private ContentValues cursor2values(Cursor cursor) {
		ContentValues v = new ContentValues();
		for (String k : cursor.getColumnNames()) {
			if (k.contains("id") && !k.contains("ssid")) {
				v.put(k, cursor.getLong(cursor.getColumnIndex(k)));
			} else {
				v.put(k, cursor.getString(cursor.getColumnIndex(k)));
			}
		}
		return v;
	}

	private static class OpenHelper extends SQLiteOpenHelper {

		private static final int VERSION = 2;

		OpenHelper(Context context) {
			super(context, "ru.whereiswifi", null, VERSION); // version here
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			String sql = "CREATE TABLE IF NOT EXISTS " + SPOT_TABLE
					+ " (id INTEGER PRIMARY KEY, global_id INTEGER, "
					+ "lat TEXT NOT NULL, lon TEXT NOT NULL, "
					+ "ssid TEXT NOT NULL, last INTEGER NOT NULL, "
					+ "cap TEXT);";
			db.execSQL(sql);
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			onCreate(db);
			if (oldVersion < 2) {
				db.execSQL("ALTER TABLE " + SPOT_TABLE + " ADD bssid TEXT;");
			}
		}
	}

}
