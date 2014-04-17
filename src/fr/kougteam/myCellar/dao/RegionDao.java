package fr.kougteam.myCellar.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import fr.kougteam.myCellar.modele.Region;

/**
 * Gestion de la table REGIONS
 * 
 * @author Thomas Cousin
 *
 */
public class RegionDao extends AbstractDao<Region> {
	
	// Database table
	public static final String TABLE 				= "regions";
	public static final String COL_PAYS				= "id_pays";
	public static final String COL_NOM				= "nom";
	public static final String COL_REGION_PARENT	= "id_region_parent";
	
	// Database creation SQL statement
	private static final String DATABASE_CREATE = 
			"create table " + TABLE + " (" +
				COL_ID + " integer primary key autoincrement not null, " +
				COL_PAYS + " integer, " +
				COL_NOM + " text, " +
				COL_REGION_PARENT + " integer " +
			");";

	public RegionDao(Context context) {
		super(context);
	}
	
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	public static void onUpgrade(Context ctxt, SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(RegionDao.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ "...");
		if (oldVersion<=1 && newVersion>=2) {
			// Ajout d'une r�gion vide
			database.execSQL("INSERT INTO "+TABLE+" ("+COL_ID+","+COL_PAYS+","+COL_NOM+","+COL_REGION_PARENT+") VALUES (-1,1,'',0)"); 
		}
		
		if (oldVersion<=4 && newVersion>=5) {
			database.execSQL("INSERT INTO REGIONS VALUES(36,1,'La r�gion nantaise',34)");
			database.execSQL("INSERT INTO REGIONS VALUES(37,1,'Anjou-Saumur',34)");
			database.execSQL("INSERT INTO REGIONS VALUES(38,1,'La Touraine',34)");
			database.execSQL("INSERT INTO REGIONS VALUES(39,1,'Les vignobles du Centre',34)");
		}  
	}
	
	/**
	 * Retourne les donn�es contenu dans l'objet sous forme de ContentValues
	 * 
	 * @param p l'objet contenant les donn�e
	 * 
	 * @return
	 */
	public static ContentValues getContentValues(Region r) {
		ContentValues cv = new ContentValues();
		cv.put(COL_ID, r.getId());
		cv.put(COL_PAYS, r.getIdPays());
		cv.put(COL_NOM, r.getNom());	
		return cv;
	}
	
	public Region getById(long id) {
		Region r = new Region();
		String sql = " SELECT " + COL_PAYS + ", " + COL_NOM + ", " + COL_REGION_PARENT +
					 " FROM " + TABLE + 
					 " WHERE " + COL_ID + "=" + id;
		if (bdd==null) super.openForRead();
		Cursor c = bdd.rawQuery(sql, null);
		if (c.getCount()==1) {
			c.moveToFirst();
			r.setId(id);
			r.setIdPays(c.getInt(0));
			r.setNom(c.getString(1));
			r.setIdRegionParent(c.getInt(2));
		}
		return r;
	}
	
	public Cursor getAll() {
		String sql = " SELECT " + COL_ID + ", " + COL_PAYS + ", " + COL_NOM + ", " + COL_REGION_PARENT +
					 " FROM " + TABLE  +
					 " ORDER BY " + COL_NOM ;
		if (bdd==null) super.openForRead();		
		return bdd.rawQuery(sql, null);
	}
	
	public Cursor getRegionsByPays(int idPays) {
		String sql = " SELECT " + COL_ID + ", " + COL_PAYS + ", " + COL_NOM + ", " + COL_REGION_PARENT +
					 " FROM " + TABLE + 
					 " WHERE " + COL_REGION_PARENT + "=0 AND " + COL_PAYS + " = " + idPays +
					 " ORDER BY " + COL_NOM ;
		if (bdd==null) super.openForRead();		
		return bdd.rawQuery(sql, null);
	}
	
	public Cursor getSousRegionsByRegion(int idRegion) {
		String sql = " SELECT " + COL_ID + ", " + COL_PAYS + ", " + COL_NOM + ", " + COL_REGION_PARENT +
					 " FROM " + TABLE + 
					 " WHERE " + COL_REGION_PARENT + " = " + idRegion  + " OR " + COL_ID + "=-1" +
					 " ORDER BY " + COL_NOM ;
		if (bdd==null) super.openForRead();		
		return bdd.rawQuery(sql, null);
	}
	
	public boolean hasSousRegion(int idRegion) {
		String sql = " SELECT 1 " + 
					 " FROM " + TABLE + 
					 " WHERE " + COL_REGION_PARENT + " = " + idRegion;
		if (bdd==null) super.openForRead();		
		return bdd.rawQuery(sql, null).getCount() > 0;
	}
}
