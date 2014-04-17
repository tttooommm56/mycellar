package fr.kougteam.myCellar.dao;

import java.io.InputStream;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import fr.kougteam.myCellar.helper.FileHelper;
import fr.kougteam.myCellar.helper.SqlOpenHelper;
import fr.kougteam.myCellar.modele.Met;

/**
 * Gestion de la table METS
 * 
 * @author Thomas Cousin
 *
 */
public class MetDao extends AbstractDao<Met> {
	
	// Database table
	public static final String TABLE 	= "mets";
	public static final String COL_NOM	= "nom";
	
	// Database creation SQL statement
	private static final String DATABASE_CREATE = 
			"create table " + TABLE + " (" +
				COL_ID + " integer primary key, " +
				COL_NOM + " text " +
			");";

	public MetDao(Context context) {
		super(context);
	}
	
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	public static void onUpgrade(Context ctxt, SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(MetDao.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ "...");
		if (oldVersion<4 && newVersion>=4) {
			//database.execSQL("DROP TABLE " +TABLE+";");
			database.execSQL(DATABASE_CREATE);
			
			Log.i(SqlOpenHelper.class.getName(), "Insertion des donnees dans la table METS");
			try {
		         InputStream is = ctxt.getResources().getAssets().open("insert_mets.sql");        
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
	 * Retourne les donn�es contenu dans l'objet sous forme de ContentValues
	 * 
	 * @param p l'objet contenant les donn�es
	 * 
	 * @return
	 */
	public static ContentValues getContentValues(Met p) {
		ContentValues cv = new ContentValues();
		cv.put(COL_ID, p.getId());
		cv.put(COL_NOM, p.getNom());
		
		return cv;
	}
	
	public Met getById(long id) {
		Met p = new Met();
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
	
	public Cursor findMets(String search) {
		String sql = " SELECT " + COL_ID + ", " + COL_NOM + 
					 " FROM " + TABLE +
					 " WHERE UPPER("+COL_NOM+") LIKE '%"+search.toUpperCase().trim()+"%'"+
					 " ORDER BY " + COL_NOM;
		if (bdd==null) super.openForRead();	
		return bdd.rawQuery(sql, null);
	}
	
	public Cursor getAll() {
		String sql = " SELECT " + COL_ID + ", " + COL_NOM + 
					 " FROM " + TABLE +
					 " ORDER BY " + COL_NOM;
		if (bdd==null) super.openForRead();	
		return bdd.rawQuery(sql, null);
	}

}
