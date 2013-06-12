package fr.kougteam.myCellar.dao;

import java.io.IOException;
import java.io.InputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import fr.kougteam.myCellar.helper.FileHelper;
import fr.kougteam.myCellar.modele.Appellation;

/**
 * Gestion de la table APPELLATIONS
 * 
 * @author Thomas Cousin
 *
 */
public class AppellationDao extends AbstractDao<Appellation> {
	
	// Database table
	public static final String TABLE 		= "appellations";
	public static final String COL_REGION	= "id_region";
	public static final String COL_NOM		= "nom";
	
	// Database creation SQL statement
	private static final String DATABASE_CREATE = 
			"create table " + TABLE + " (" +
				COL_ID + " integer primary key autoincrement not null, " +
				COL_REGION + " integer, " +
				COL_NOM + " text " +
			");";

		
	public AppellationDao(Context context) {
		super(context);
	}
	
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	public static void onUpgrade(Context ctxt, SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(AppellationDao.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ "...");
		if (oldVersion<=1 && newVersion>=2) {
			// Ajout d'une appellation vide
			database.execSQL("INSERT INTO "+TABLE+" ("+COL_ID+","+COL_REGION+","+COL_NOM+") VALUES (-1,-1,'')"); 
		}
		
	}
	
	/**
	 * Retourne les données contenu dans l'objet sous forme de ContentValues
	 * 
	 * @param a l'objet contenant les donnée
	 * 
	 * @return
	 */
	public static ContentValues getContentValues(Appellation a) {
		ContentValues cv = new ContentValues();
		cv.put(COL_ID, a.getId());
		cv.put(COL_REGION, a.getIdRegion());
		cv.put(COL_NOM, a.getNom());	
		return cv;
	}
	
	public Appellation getById(int id) {
		Appellation a = new Appellation();
		String sql = " SELECT " + COL_REGION + ", " + COL_NOM +
					 " FROM " + TABLE + 
					 " WHERE " + COL_ID + "=" + id;
		if (bdd==null) super.openForRead();
		Cursor c = bdd.rawQuery(sql, null);
		if (c.getCount()==1) {
			c.moveToFirst();
			a.setId(id);
			a.setIdRegion(c.getInt(0));
			a.setNom(c.getString(1));
		}
		return a;
	}
	
	public Cursor getAll() {
		String sql = " SELECT " + COL_ID + ", " + COL_REGION + ", " + COL_NOM + " FROM " + TABLE ;
		if (bdd==null) super.openForRead();		
		return bdd.rawQuery(sql, null);
	}
	
	public Cursor getListByRegion(int idRegion) {
		String sql = " SELECT " + COL_ID + ", " + COL_REGION + ", " + COL_NOM + 
					" FROM " + TABLE +
					" WHERE " + COL_REGION + " = " + idRegion + " OR " + COL_ID + "=-1" +
					" ORDER BY " + COL_NOM;
		if (bdd==null) super.openForRead();		
		return bdd.rawQuery(sql, null);
	}
}
