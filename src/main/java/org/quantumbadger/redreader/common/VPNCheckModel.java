package org.quantumbadger.redreader.common;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.support.annotation.NonNull;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.function.BiConsumer;

/**
 * Model层
 * 控制底层数据读写
 */
public final class VPNCheckModel implements Iterable<VPNCheckModel.VPNCheckItem> {
	public final class VPNCheckItem {
		public String name;
		public URI uri;
		public Boolean success;

		VPNCheckItem(String name, URI uri, Boolean success) {
			this.name = name;
			this.uri = uri;
			this.success = success;
		}
	}

	public final class SQLiteVPNCheckListHelper extends SQLiteOpenHelper {
		private static final String dbName = "VPNCheckModel.db";

		private static final int dbVersion = 1;

		final String tableName = VPNCheckModel.class.getName();

		private final String createTableSQL = String.format(
				"create table %s(%s,%s,%s,%s);",
				tableName,
				"id INTEGER PRIMARY AUTOINCREMENT KEY",
				"name TEXT NOT NULL",
				"uri TEXT NOT NULL",
				"time TIMESTAMP DEFAULT (datetime('now', 'localtime'))"
				);

		SQLiteVPNCheckListHelper(Context context) {
			super(context, dbName, null, dbVersion);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL(createTableSQL);

			final ContentValues contentValues = new ContentValues();
			final Map<String, URI> init = new HashMap<>();

			try {
				init.put("baidu", new URI("www.baidu.com"));
				init.put("google", new URI("www.google.com"));
				init.put("reddit", new URI("www.reddit.com"));
			} catch (URISyntaxException syntax) {
				syntax.printStackTrace();
			}

			for (Map.Entry<String, URI> entry : init.entrySet()) {
				contentValues.put("name", entry.getKey());
				contentValues.put("uri", entry.getValue().toString());
				db.insert(tableName, null, contentValues);
			}
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			System.out.println(String.format(
					Locale.getDefault(),
					"from version %d to %d",
					oldVersion,
					newVersion
			));
		}
	}

	private List<VPNCheckItem> objects = new ArrayList<>();

	private SQLiteVPNCheckListHelper sql;

	public void add(String name, URI uri) {
		this.add(name, uri, null);
	}

	public void add(String name, URI uri, Boolean success) {
		this.objects.add(new VPNCheckItem(name, uri, success));
	}

	public VPNCheckItem get(int i) {
		return objects.get(i);
	}

	public int size() {
		return objects.size();
	}

	public VPNCheckModel(Context context) {
		sql = new SQLiteVPNCheckListHelper(context);

		SQLiteDatabase db = sql.getReadableDatabase();
		Cursor cursor = db.query(sql.tableName, null, "*", null, null, null, null);

		try {
			while (cursor.moveToNext()) {
				String name = cursor.getString(1);
				URI uri = new URI(cursor.getString(2));
				add(name, uri);
			}
			cursor.close();
		} catch (URISyntaxException syntax) {
			System.out.println("error when loading checklist from database with exception:");
			syntax.printStackTrace();
		}
	}

	@NonNull
	@Override
	public Iterator<VPNCheckItem> iterator() {
		return new Iterator<VPNCheckItem>() {
			int size = size();
			int now = 0;

			@Override
			public boolean hasNext() {
				return now < size;
			}

			@Override
			public VPNCheckItem next() {
				return get(now++);
			}
		};
	}

	public void resetCheckList() {
		;
	}

	public void editCheckList(String newList) {
		;
	}
}
