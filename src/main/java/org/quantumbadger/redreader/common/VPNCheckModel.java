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
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

/**
 * Model层
 * 控制底层数据读写
 */
public final class VPNCheckModel implements Iterable<VPNCheckModel.VPNCheckItem> {
	public static final class VPNCheckItem {
		public String name;
		public URI uri;
		public Boolean success;

		VPNCheckItem(String name, URI uri) {
			this(name, uri, null);
		}

		VPNCheckItem(String name, URI uri, Boolean success) {
			this.name = name;
			this.uri = uri;
			this.success = success;
		}

		@Override
		public boolean equals(Object o) {
			if (this == o) return true;
			if (o == null || getClass() != o.getClass()) return false;
			VPNCheckItem that = (VPNCheckItem) o;
			return name.equals(that.name) &&
					uri.equals(that.uri);
		}

		@Override
		public String toString() {
			return String.format(
					Locale.getDefault(),
					"VPNCheckItem[name: %s, uri: %s, success: %s]",
					name, uri, success
			);
		}
	}

	public final class SQLiteVPNCheckListHelper extends SQLiteOpenHelper {
		private static final String dbName = "VPNCheckModel.db";

		private static final int dbVersion = 1;

		final String tableName = VPNCheckModel.class.getSimpleName();

		private final String createTableSQL = String.format(
				"create table %s(%s,%s,%s,%s);",
				tableName,
				"id INTEGER PRIMARY KEY AUTOINCREMENT",
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

			addToSQL(defaultObjects);
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

	private List<VPNCheckItem> defaultObjects = new ArrayList<>();
	private List<VPNCheckItem> objects = new ArrayList<>();

	private SQLiteVPNCheckListHelper sql;

	public void add(String name, URI uri) {
		this.objects.add(new VPNCheckItem(name, uri));
	}

	public void add(String name, URI uri, Boolean success) {
		this.objects.add(new VPNCheckItem(name, uri, success));
	}

	public void clear() {
		this.objects.clear();
	}

	public VPNCheckItem get(int i) {
		return objects.get(i);
	}

	public int size() {
		return objects.size();
	}

	public VPNCheckModel(Context context) {
		// default
		try {
			defaultObjects.add(
					new VPNCheckItem(
							"baidu", new URI("www.baidu.com"), false
					));
			defaultObjects.add(
					new VPNCheckItem(
							"google", new URI("www.google.com"), false
					));
			defaultObjects.add(
					new VPNCheckItem(
							"reddit", new URI("www.reddit.com"), false
					));
		} catch (URISyntaxException syntax) {
			syntax.printStackTrace();
		}

		// sql
		sql = new SQLiteVPNCheckListHelper(context);

		// load
		load();
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

	private void addToSQL(List<VPNCheckItem> objects) {
		final SQLiteDatabase db = sql.getWritableDatabase();
		final ContentValues contentValues = new ContentValues();

		for (VPNCheckItem item : objects) {
			contentValues.put("name", item.name);
			contentValues.put("uri", item.uri.toString());
			db.insert(sql.tableName, null, contentValues);
		}
	}

	private void load() {
		SQLiteDatabase db = sql.getReadableDatabase();
		Cursor cursor = db.query(
				sql.tableName,
				new String[] {"name", "uri"},
				null, null,
				null, null, null
		);

		clear();
		try {
			while (cursor.moveToNext()) {
				String name = cursor.getString(cursor.getColumnIndex("name"));
				URI uri = new URI(cursor.getString(cursor.getColumnIndex("uri")));
				add(name, uri);
			}
			cursor.close();
		} catch (URISyntaxException syntax) {
			System.out.println("error when loading checklist from database with exception:");
			syntax.printStackTrace();
		}
	}

	private void deleteAll() {
		SQLiteDatabase db = sql.getWritableDatabase();
		db.execSQL(String.format("%s %s", "delete from", sql.tableName));
	}

	public void resetCheckList() {
		deleteAll();
		addToSQL(defaultObjects);
		clear();
		this.objects.addAll(this.defaultObjects);
	}

	static List<VPNCheckItem> stringToCheckItems(String str) {
		if (str == null) {
			return null;
		}

		str = str.trim();

		List<VPNCheckItem> list = new ArrayList<>();

		if (!str.contains(" ")) {
			return null;
		}

		List<String> lines = new ArrayList<>();

		if (str.contains("\n")) {
			for (String line : str.split("\n")) {
				lines.add(line);
			}
		} else {
			lines.add(str);
		}

		try {
			for (String line : lines) {
				String[] strings = line.split("\\s+");

				if (strings.length == 2) {
					list.add(new VPNCheckItem(
							strings[0],
							new URI(strings[1])
					));
				}
			}
		} catch (URISyntaxException syntax) {
			syntax.printStackTrace();
		}

		return list;
	}

	public void editCheckList(String str) {
		List<VPNCheckItem> newList = stringToCheckItems(str);

		if (newList == null) {
			return;
		}

		this.objects.addAll(newList);

		deleteAll();
		addToSQL(this.objects);
	}
}
