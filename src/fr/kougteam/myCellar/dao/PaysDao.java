package fr.kougteam.myCellar.dao;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import fr.kougteam.myCellar.modele.Pays;

/**
 * Gestion de la table PAYS
 * 
 * @author Thomas Cousin
 *
 */
public class PaysDao extends AbstractDao<Pays> {
	
	// Database table
	public static final String TABLE 	= "pays";
	public static final String COL_NOM	= "nom";
	
	// Database creation SQL statement
	private static final String DATABASE_CREATE = 
			"create table " + TABLE + " (" +
				COL_ID + " integer primary key autoincrement not null, " +
				COL_NOM + " text " +
			");";

	public PaysDao(Context context) {
		super(context);
	}
	
	private static void init(SQLiteDatabase database) {
		try {
			String sql = "INSERT INTO " + TABLE + " ("+COL_ID+","+COL_NOM+") VALUES (?, ?);";
			database.beginTransaction();
			database.execSQL(sql, new Object[] {1, "France"});
			database.execSQL(sql, new Object[] {2, "Allemagne"});
			database.execSQL(sql, new Object[] {3, "Italie"});
			database.execSQL(sql, new Object[] {4, "Espagne"});
			//FIXME ajouter d'autres pays ?
			database.setTransactionSuccessful();
		} catch (SQLException ex) {
			Log.e("init error", ex.getMessage());
		} finally {
			database.endTransaction();
		}
	}
	
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
		init(database);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(PaysDao.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE);
		onCreate(database);
	}
	
	/**
	 * Retourne les données contenu dans l'objet sous forme de ContentValues
	 * 
	 * @param p l'objet contenant les données
	 * 
	 * @return
	 */
	public static ContentValues getContentValues(Pays p) {
		ContentValues cv = new ContentValues();
		cv.put(COL_ID, p.getId());
		cv.put(COL_NOM, p.getNom());
		
		return cv;
	}
	
	public Pays getById(int id) {
		Pays p = new Pays();
		String sql = " SELECT " + COL_NOM + 
					 " FROM " + TABLE + 
					 " WHERE " + COL_ID + "=" + id;
		if (bdd==null) super.openForRead();
		Cursor c = bdd.rawQuery(sql, null);
		if (c.getCount()==1) {
			c.moveToFirst();
			p.setId(id);
			p.setNom(c.getString(0));
		}
		return p;
	}
	
	public List<Pays> getAll() {
		List<Pays> list = new ArrayList<Pays>();
		String sql = " SELECT " + COL_ID + ", " + COL_NOM + 
					 " FROM " + TABLE ;
		if (bdd==null) super.openForRead();
		Cursor c = bdd.rawQuery(sql, null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			Pays p = new Pays();
			p.setId(c.getInt(0));
			p.setNom(c.getString(1));
			list.add(p);
			c.moveToNext();
		}
		return list;
	}
}
