package fr.kougteam.myCellar.helper;

import java.io.InputStream;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import fr.kougteam.myCellar.dao.AppellationDao;
import fr.kougteam.myCellar.dao.PaysDao;
import fr.kougteam.myCellar.dao.RegionDao;
import fr.kougteam.myCellar.dao.VinDao;

/**
 * Gestion de la BDD
 * 
 * @author Thomas Cousin
 *
 */
public class SqlOpenHelper extends SQLiteOpenHelper {
	
	public static final String 	DBNAME 	= "myCellar.db";
	public static final int 	VERSION = 1;
	private Context myContext;
	
	public SqlOpenHelper(Context context) {
		super(context, DBNAME, null, VERSION);  
		myContext = context;
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(SqlOpenHelper.class.getName(), "Creation des tables");
		PaysDao.onCreate(db);
		RegionDao.onCreate(db);
		AppellationDao.onCreate(db);
		VinDao.onCreate(db);
		
		insertDefaultData(db);
    }
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(SqlOpenHelper.class.getName(), "onUpgrade");
		PaysDao.onUpgrade(db, oldVersion, newVersion);
		RegionDao.onUpgrade(db, oldVersion, newVersion);
		AppellationDao.onUpgrade(db, oldVersion, newVersion);
		VinDao.onUpgrade(db, oldVersion, newVersion);
		
		insertDefaultData(db);
	}
	
	private void insertDefaultData(SQLiteDatabase db) {
		Log.i(SqlOpenHelper.class.getName(), "Insertion des donnees");
		try {
	         InputStream is = myContext.getResources().getAssets().open("install_db.sql");        
	         String[] statements = FileHelper.parseSqlFile(is);      
	         for (String statement : statements) {
	        	 db.execSQL(statement);
	         }
	         
	     } catch (Exception ex) {
	    	 Log.e("onCreate error !", ex.getMessage());
	     }
	}
}
