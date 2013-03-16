package fr.kougteam.myCellar.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
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
	
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
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
	
	public Cursor getAll() {
		String sql = " SELECT " + COL_ID + ", " + COL_NOM + 
					 " FROM " + TABLE ;
		if (bdd==null) super.openForRead();	
		return bdd.rawQuery(sql, null);
	}

}
