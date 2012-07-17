package fr.kougteam.myCellar.dao;

import java.util.ArrayList;
import java.util.List;

import fr.kougteam.myCellar.modele.Appellation;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Gestion de la table APPELLATIONS
 * 
 * @author Thomas Cousin
 *
 */
public class AppellationDao extends AbstractDao<Appellation> {
	
	// Database table
	public static final String TABLE 		= "appellations";
	public static final String COL_PAYS		= "pays";
	public static final String COL_REGION	= "region";
	public static final String COL_NOM		= "nom";
	
	// Database creation SQL statement
	private static final String DATABASE_CREATE = 
			"create table " + TABLE + " (" +
				COL_ID + " integer primary key autoincrement not null, " +
				COL_PAYS + " integer, " +
				COL_REGION + " integer, " +
				COL_NOM + " text " +
			");";

		
	public AppellationDao(Context context) {
		super(context);
	}

	private static void init(SQLiteDatabase database) {
		try {
			String sql = "INSERT INTO " + TABLE + " ("+COL_ID+","+COL_PAYS+","+COL_REGION+","+COL_NOM+") VALUES (?, ?, ?, ?);";
			database.beginTransaction();
			database.execSQL(sql, new Object[] {1, 1, 1, "Edelswicker"});
			//FIXME ajouter les autres appellations
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
		Log.w(AppellationDao.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE);
		onCreate(database);
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
		cv.put(COL_PAYS, a.getIdPays());
		cv.put(COL_REGION, a.getIdRegion());
		cv.put(COL_NOM, a.getNom());	
		return cv;
	}
	
	public Appellation getById(int id) {
		Appellation a = new Appellation();
		String sql = " SELECT " + COL_PAYS + ", " + COL_REGION + ", " + COL_NOM +
					 " FROM " + TABLE + 
					 " WHERE " + COL_ID + "=" + id;
		if (bdd==null) super.openForRead();
		Cursor c = bdd.rawQuery(sql, null);
		if (c.getCount()==1) {
			c.moveToFirst();
			a.setId(id);
			a.setIdPays(c.getInt(0));
			a.setIdRegion(c.getInt(1));
			a.setNom(c.getString(2));
		}
		return a;
	}
	
	public List<Appellation> getAll() {
		List<Appellation> list = new ArrayList<Appellation>();
		String sql = " SELECT " + COL_ID + ", " + COL_PAYS + ", " + COL_REGION + ", " + COL_NOM + " FROM " + TABLE ;
		if (bdd==null) super.openForRead();
		Cursor c = bdd.rawQuery(sql, null);
		c.moveToFirst();
		while (!c.isAfterLast()) {
			Appellation a = new Appellation();
			a.setId(c.getInt(0));
			a.setIdPays(c.getInt(1));
			a.setIdRegion(c.getInt(2));
			a.setNom(c.getString(3));
			list.add(a);
			c.moveToNext();
		}
		return list;
	}
}
