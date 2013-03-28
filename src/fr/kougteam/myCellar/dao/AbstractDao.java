package fr.kougteam.myCellar.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import fr.kougteam.myCellar.helper.SqlOpenHelper;

public abstract class AbstractDao<T> {
	public static final String COL_ID = "_id";
	
	private SqlOpenHelper sqlHelper;
	protected SQLiteDatabase bdd;
	
	public AbstractDao(Context context) {
		sqlHelper = new SqlOpenHelper(context);
	}
	
	public void openForWrite() throws SQLException {
		bdd = sqlHelper.getWritableDatabase();
	}
	
	public void openForRead() throws SQLException {
		bdd = sqlHelper.getReadableDatabase();
	}
	
	public void close() {
		if (bdd != null) bdd.close();
	}
	
	public long insert(String table, ContentValues values) {
		if (bdd==null) openForWrite();
		return bdd.insert(table, null, values);
	}
	
	public long update(String table, ContentValues values, int keyId) {
		if (bdd==null) openForWrite();
		return bdd.update(table, values, COL_ID + " = " + keyId, null);
	}
	
	public void delete (String table, int keyId) {
		if (bdd==null) openForWrite();
		bdd.delete(table, COL_ID + " = " + keyId, null);
	}
	
	public abstract T getById(int id);
	
	public abstract Cursor getAll();
}
