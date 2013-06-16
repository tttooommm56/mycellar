package fr.kougteam.myCellar.helper;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import fr.kougteam.myCellar.R;

import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class DbBackupHelper {

	public static final String TAG = DbBackupHelper.class.getName();

	/** Directory that files are to be read from and written to **/
	protected static final File DATABASE_DIRECTORY =
		new File(Environment.getExternalStorageDirectory(),"MonCellier");

	protected static final String IMPORT_FILE_NAME = "myCellar_v"+SqlOpenHelper.DBVERSION+".db";
	/** File path of Db to be imported **/
	protected static final File IMPORT_FILE = new File(DATABASE_DIRECTORY, IMPORT_FILE_NAME);
	

	public static final String PACKAGE_NAME = "fr.kougteam.myCellar";
	public static final String DATABASE_NAME = SqlOpenHelper.DBNAME;
	public static final String DATABASE_TABLE = "entryTable";

	/** Contains: /data/data/com.example.app/databases/example.db **/
	private static final File DATA_DIRECTORY_DATABASE =
			new File(Environment.getDataDirectory() +
			"/data/" + PACKAGE_NAME +
			"/databases/" + DATABASE_NAME );

	/** Saves the application database to the
	 * export directory under MyDb.db **/
	public static  boolean exportDb(){
		if( ! isSDPresent() ) return false;

		File dbFile = DATA_DIRECTORY_DATABASE;

		File exportDir = DATABASE_DIRECTORY;
		File file = new File(exportDir, IMPORT_FILE_NAME);

		if (!exportDir.exists()) {
			exportDir.mkdirs();
		}

		try {
			file.createNewFile();
			copyFile(dbFile, file);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	/** Replaces current database with the IMPORT_FILE if
	 * import database is valid and of the correct type **/
	public static boolean restoreDb(){
		if(!isSDPresent()) return false;

		File exportFile = DATA_DIRECTORY_DATABASE;
		File importFile = IMPORT_FILE;

//		if( ! checkDbIsValid(importFile) ) return false;

		if (!importFile.exists()) {
			Log.d(TAG, "File does not exist");
			return false;
		}

		try {
			exportFile.createNewFile();
			copyFile(importFile, exportFile);
			return true;
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
	}

	private static void copyFile(File src, File dst) throws IOException {
		FileChannel inChannel = new FileInputStream(src).getChannel();
		FileChannel outChannel = new FileOutputStream(dst).getChannel();
		try {
			inChannel.transferTo(0, inChannel.size(), outChannel);
		} finally {
			if (inChannel != null)
				inChannel.close();
			if (outChannel != null)
				outChannel.close();
		}
	}

	/** Returns whether an SD card is present and writable **/
	public static boolean isSDPresent() {
		return Environment.getExternalStorageState().equals(
				Environment.MEDIA_MOUNTED);
	}
}