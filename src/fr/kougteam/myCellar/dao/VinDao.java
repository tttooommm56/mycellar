package fr.kougteam.myCellar.dao;

import java.util.ArrayList;
import java.util.List;

import fr.kougteam.myCellar.enums.Couleur;
import fr.kougteam.myCellar.modele.Vin;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

/**
 * Gestion de la table VINS
 * 
 * @author Thomas Cousin
 *
 */
public class VinDao extends AbstractDao<Vin> {
	
	// Database table
	public static final String TABLE 			= "vins";
	public static final String COL_COULEUR		= "couleur";
	public static final String COL_PAYS 		= "pays";
	public static final String COL_REGION		= "region";
	public static final String COL_APPELLATION	= "appellation";
	public static final String COL_ANNEE 		= "annee";
	public static final String COL_NOM 			= "nom";
	public static final String COL_PRODUCTEUR 	= "producteur";
	public static final String COL_COMMENTAIRES = "commentaires";
	public static final String COL_NB_BOUTEILLES = "nb_bouteilles";
	public static final String COL_NOTE 		= "note";
	
	// Database creation SQL statement
	private static final String DATABASE_CREATE = 
			"create table " + TABLE + " (" +
				COL_ID + " integer primary key autoincrement not null, " +
				COL_COULEUR + " text, " +
				COL_PAYS + " integer, " +
				COL_REGION + " integer, " +
				COL_APPELLATION + " text, " +
				COL_ANNEE + " integer, " +
				COL_NOM + " text, " +
				COL_PRODUCTEUR + " text, " +
				COL_COMMENTAIRES + " text, " +
				COL_NB_BOUTEILLES + " integer, " +
				COL_NOTE + " real " +
			");";

	public VinDao(Context context) {
		super(context);
	}
	
	public static void onCreate(SQLiteDatabase database) {
		database.execSQL(DATABASE_CREATE);
	}

	public static void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
		Log.w(VinDao.class.getName(), "Upgrading database from version "
				+ oldVersion + " to " + newVersion
				+ ", which will destroy all old data");
		database.execSQL("DROP TABLE IF EXISTS " + TABLE);
		onCreate(database);
	}
	
	/**
	 * Retourn les données contenu dans l'objet sous forme de ContentValues
	 * 
	 * @param p l'objet contenant les donnée
	 * 
	 * @return
	 */
	public static ContentValues getContentValues(Vin v) {
		ContentValues cv = new ContentValues();
		cv.put(COL_ID, v.getId());
		cv.put(COL_COULEUR, v.getCouleur().getCode());
		cv.put(COL_PAYS, v.getIdPays());
		cv.put(COL_REGION, v.getIdRegion());
		cv.put(COL_APPELLATION, v.getIdAppellation());
		cv.put(COL_ANNEE, v.getAnnee());
		cv.put(COL_NOM, v.getNom());
		cv.put(COL_PRODUCTEUR, v.getProducteur());
		cv.put(COL_COMMENTAIRES, v.getCommentaire());
		cv.put(COL_NB_BOUTEILLES, v.getNbBouteilles());
		cv.put(COL_NOTE, v.getNote());
		return cv;
	}
	
	public Vin getById(int id) {
		Vin v = new Vin();
		String sql = " SELECT "+COL_COULEUR + ", " + 
								COL_PAYS + " , " +
								COL_REGION + ", " +
								COL_APPELLATION + ", " +
								COL_ANNEE + ", " +
								COL_NOM + ", " +
								COL_PRODUCTEUR + ", " +
								COL_COMMENTAIRES + ", " +
								COL_NB_BOUTEILLES + ", " +
								COL_NOTE + " " + 
					 " FROM " + TABLE + 
					 " WHERE " + COL_ID + "=" + id;
		if (bdd==null) super.openForRead();
		Cursor c = bdd.rawQuery(sql, null);
		if (c.getCount()==1) {
			c.moveToFirst();
			int i = 0;
			v.setId(id);
			v.setCouleur(Couleur.getFromId(c.getString(i++)));
			v.setIdPays(c.getInt(i++));
			v.setIdRegion(c.getInt(i++));
			v.setIdAppellation(c.getInt(i++));
			v.setAnnee(c.getInt(i++));
			v.setNom(c.getString(i++));
			v.setProducteur(c.getString(i++));
			v.setCommentaire(c.getString(i++));
			v.setNbBouteilles(c.getInt(i++));
			v.setNote(c.getDouble(i++));
		}
		return v;
	}
	
	public Cursor getAll() {
//		List<Vin> list = new ArrayList<Vin>();
		String sql = " SELECT "+COL_ID + ", " + 
								COL_COULEUR + ", " + 
								COL_PAYS + " , " +
								COL_REGION + ", " +
								COL_APPELLATION + ", " +
								COL_ANNEE + ", " +
								COL_NOM + ", " +
								COL_PRODUCTEUR + ", " +
								COL_COMMENTAIRES + ", " +
								COL_NB_BOUTEILLES + ", " +
								COL_NOTE + " " + 
					" FROM " + TABLE ;
		if (bdd==null) super.openForRead();
//		Cursor c = bdd.rawQuery(sql, null);
//		c.moveToFirst();
//		while (!c.isAfterLast()) {
//			Vin v = new Vin();
//			int i = 0;
//			v.setId(c.getInt(i++));
//			v.setCouleur(Couleur.getFromId(c.getString(i++)));
//			v.setIdPays(c.getInt(i++));
//			v.setIdRegion(c.getInt(i++));
//			v.setIdAppellation(c.getInt(i++));
//			v.setAnnee(c.getInt(i++));
//			v.setNom(c.getString(i++));
//			v.setProducteur(c.getString(i++));
//			v.setCommentaire(c.getString(i++));
//			v.setNbBouteilles(c.getInt(i++));
//			v.setNote(c.getDouble(i++));
//			list.add(v);
//			c.moveToNext();
//		}
//		return list;
		
		return bdd.rawQuery(sql, null);
	}
}
