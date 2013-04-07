package fr.kougteam.myCellar.dao;

import java.io.InputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import fr.kougteam.myCellar.helper.FileHelper;
import fr.kougteam.myCellar.helper.SqlOpenHelper;
import fr.kougteam.myCellar.modele.MetVin;

/**
 * Gestion de la table METS
 * 
 * @author Thomas Cousin
 *
 */
public class MetVinDao extends AbstractDao<MetVin> {
	
	// Database table
	public static final String TABLE 		= "mets_vins";
	public static final String COL_ID_MET	= "id_met";
	public static final String COL_NOM_VIN	= "nom_vin";
	public static final String COL_TYPE		= "type";
	
	// Database creation SQL statement
	private static final String DATABASE_CREATE = 
			"create table " + TABLE + " (" +
				COL_ID_MET + " integer, " +
				COL_NOM_VIN + " text, " +
				COL_TYPE + " text " +
			");";

	public MetVinDao(Context context) {
		super(context);
	}
	
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	public static void onUpgrade(Context ctxt, SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(MetVinDao.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ "...");
		if (oldVersion<4 && newVersion==4) {
			//database.execSQL("DROP TABLE " +TABLE+";");
			database.execSQL(DATABASE_CREATE);
			
			Log.i(SqlOpenHelper.class.getName(), "Insertion des donnees dans la table METS_VINS");
			try {
		         InputStream is = ctxt.getResources().getAssets().open("insert_mets_vins.sql");        
		         String[] statements = FileHelper.parseSqlFile(is);      
		         for (String statement : statements) {
		        	 database.execSQL(statement);
		         }
		         
		     } catch (Exception ex) {
		    	 Log.e("onCreate error !", ex.getMessage());
		     }
		}
	}
	
	/**
	 * Retourne les données contenu dans l'objet sous forme de ContentValues
	 * 
	 * @param p l'objet contenant les données
	 * 
	 * @return
	 */
	public static ContentValues getContentValues(MetVin p) {
		ContentValues cv = new ContentValues();
		cv.put(COL_ID_MET, p.getIdMet());
		cv.put(COL_NOM_VIN, p.getNomVin());
		cv.put(COL_TYPE, p.getType());
		return cv;
	}
	
	public MetVin getById(int id) {
		return null;
	}
	
	public Cursor getListByIdMet(int idMet) {
		String sql = " SELECT " + COL_ID_MET + ", " + COL_NOM_VIN + ", " + COL_TYPE + 
					 " FROM " + TABLE +
					 " WHERE " + COL_ID_MET + "=" + idMet +
					 " ORDER BY " + COL_NOM_VIN;
		if (bdd==null) super.openForRead();	
		return bdd.rawQuery(sql, null);
	}
	
	public Cursor getAll() {
		String sql = " SELECT " + COL_ID_MET + ", " + COL_NOM_VIN + ", " + COL_TYPE + 
					 " FROM " + TABLE +
					 " ORDER BY " + COL_NOM_VIN;
		if (bdd==null) super.openForRead();	
		return bdd.rawQuery(sql, null);
	}

}
