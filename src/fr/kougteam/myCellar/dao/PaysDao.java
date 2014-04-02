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
				+ "...");
		if (oldVersion<2 && newVersion>=2) {
			// Ajout d'un pays vide
			database.execSQL("INSERT INTO "+TABLE+" ("+COL_ID+","+COL_NOM+") VALUES (-1,'')");
		
			// Ajout de pays suppl�mentaires
			database.execSQL("INSERT INTO "+TABLE+" ("+COL_ID+","+COL_NOM+") VALUES (5,'USA')");
			database.execSQL("INSERT INTO "+TABLE+" ("+COL_ID+","+COL_NOM+") VALUES (6,'Australie')");
			database.execSQL("INSERT INTO "+TABLE+" ("+COL_ID+","+COL_NOM+") VALUES (7,'Argentine')");
			database.execSQL("INSERT INTO "+TABLE+" ("+COL_ID+","+COL_NOM+") VALUES (8,'Portugal')");
			database.execSQL("INSERT INTO "+TABLE+" ("+COL_ID+","+COL_NOM+") VALUES (9,'Chili')");
			database.execSQL("INSERT INTO "+TABLE+" ("+COL_ID+","+COL_NOM+") VALUES (10,'Afrique du Sud')");
		}
		
		if (oldVersion<6 && newVersion>=6) {
			// Ajout de la Suisse
			database.execSQL("INSERT INTO "+TABLE+" ("+COL_ID+","+COL_NOM+") VALUES (11,'Suisse')");
			database.execSQL("INSERT INTO "+TABLE+" ("+COL_ID+","+COL_NOM+") VALUES (12,'Luxembourg')");
		}
	}
	
	/**
	 * Retourne les donn�es contenu dans l'objet sous forme de ContentValues
	 * 
	 * @param p l'objet contenant les donn�es
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
					 " FROM " + TABLE +
					 " ORDER BY " + COL_NOM;
		if (bdd==null) super.openForRead();	
		return bdd.rawQuery(sql, null);
	}

}
