package fr.kougteam.myCellar.helper;

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
	
	public SqlOpenHelper(Context context) {
		super(context, DBNAME, null, VERSION);       
	}
	
	@Override
	public void onCreate(SQLiteDatabase db) {
		Log.i(SqlOpenHelper.class.getName(), "onCreate");
		PaysDao.onCreate(db);
		RegionDao.onCreate(db);
		AppellationDao.onCreate(db);
		VinDao.onCreate(db);
    }
	
	@Override
	public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
		Log.i(SqlOpenHelper.class.getName(), "onUpgrade");
		PaysDao.onUpgrade(db, oldVersion, newVersion);
		RegionDao.onUpgrade(db, oldVersion, newVersion);
		AppellationDao.onUpgrade(db, oldVersion, newVersion);
		VinDao.onUpgrade(db, oldVersion, newVersion);
	}
}
