package fr.kougteam.myCellar.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import fr.kougteam.myCellar.enums.Couleur;
import fr.kougteam.myCellar.modele.Vin;

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
	public static final String COL_IMAGE 		= "image";
	
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
				COL_NOTE + " real, " +
				COL_IMAGE + " blob"+
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
				+ "...");
		if (oldVersion<3 && newVersion==3) {
			database.execSQL("ALTER TABLE " + TABLE + " ADD "+COL_IMAGE+" BLOB");
		}
	}
	
	/**
	 * Retourn les données contenu dans l'objet sous forme de ContentValues
	 * 
	 * @param p l'objet contenant les donnée
	 * 
	 * @return
	 */
	public static ContentValues getContentValues(Vin v, boolean forInsert) {
		ContentValues cv = new ContentValues();
		if (!forInsert) cv.put(COL_ID, v.getId());
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
		cv.put(COL_IMAGE, v.getImage());
		return cv;
	}
	
	public long insert(Vin v) {
		return super.insert(TABLE, getContentValues(v, true));
	}
	
	public long update(Vin v) {
		return super.update(TABLE, getContentValues(v, false), v.getId());
	}
	
	public void delete(int id) {
		super.delete(TABLE, id);
	}
	
	public long retire1Bouteille(int id, int currentStock) {
		if (bdd==null) openForWrite();
		ContentValues cv = new ContentValues();
		cv.put(COL_NB_BOUTEILLES, currentStock-1);
		return bdd.update(TABLE, cv, COL_ID + " = " + id, null);
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
								COL_NOTE + ", " + 
								COL_IMAGE + " " + 
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
			v.setImage(c.getBlob(i++));
		}
		return v;
	}
	
	public int getTotalBouteillesByCouleur(final Couleur couleur, final boolean emptyBottlesOnly) {
		int total = 0;
		String sql = " SELECT SUM( "+ COL_NB_BOUTEILLES + ")" +
					 " FROM " + TABLE +
					 " WHERE " + COL_COULEUR + "= '" + couleur.name() + "' " ;
		
		if (emptyBottlesOnly) {
			sql += " AND " + COL_NB_BOUTEILLES + " = 0 ";
			
		} else {
			sql += " AND " + COL_NB_BOUTEILLES + " > 0 ";
		}
		
		if (bdd==null) super.openForRead();
		Cursor c = bdd.rawQuery(sql, null);
		if (c.getCount()==1) {
			c.moveToFirst();
			total = c.getInt(0);
		}
		return total;
	}
	
	public Cursor getListVinsDisposByCouleur(final Couleur couleur, final boolean emptyBottlesOnly) {
		String sql = " SELECT v."+COL_ID + ", " + 
							COL_COULEUR + ", " + 
							COL_PAYS + " , " +
							COL_REGION + ", " +
							COL_APPELLATION + ", " +
							COL_ANNEE + ", " +
							"v."+COL_NOM + ", " +
							COL_PRODUCTEUR + ", " +
							COL_COMMENTAIRES + ", " +
							COL_NB_BOUTEILLES + ", " +
							COL_NOTE + ", " + 
							" CASE WHEN "+COL_APPELLATION+"<0 THEN v." + COL_NOM + " ELSE a." + AppellationDao.COL_NOM + " END as nom_appellation " +
					" FROM " + TABLE + " v " +
					" LEFT JOIN " + AppellationDao.TABLE + " a ON a."+AppellationDao.COL_ID+"=v."+COL_APPELLATION +
					" WHERE " + COL_COULEUR + "= '" + couleur.name() + "' " ;
		
		if (emptyBottlesOnly) {
			sql += " AND " + COL_NB_BOUTEILLES + " = 0 ";
			
		} else {
			sql += " AND " + COL_NB_BOUTEILLES + " > 0 ";
		}

		sql +=	" ORDER BY " + COL_ANNEE + " DESC, nom_appellation, " + COL_PRODUCTEUR + ", v." + COL_NOM;
		
		if (bdd==null) super.openForRead();		
		
		return bdd.rawQuery(sql, null);
	}
	
	public Cursor getAll() {
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
					" FROM " + TABLE +
					" ORDER BY " + COL_ANNEE + " DESC, " + COL_APPELLATION + "," + COL_PRODUCTEUR + ", " + COL_NOM;
		if (bdd==null) super.openForRead();		
		return bdd.rawQuery(sql, null);
	}
	
	public Cursor getMatchingNoms(String nom) {
		String sql = " SELECT DISTINCT 1 _id, "+ COL_NOM + 
					" FROM " + TABLE +
					" WHERE UPPER("+ COL_NOM + ") LIKE '%"+nom.toUpperCase()+"%' " +
					" ORDER BY " + COL_NOM;
		if (bdd==null) super.openForRead();		
		return bdd.rawQuery(sql, null);
	}
	
	public Cursor getMatchingProducteurs(String producteur) {
		String sql = " SELECT DISTINCT 1 _id, "+ COL_PRODUCTEUR + 
					" FROM " + TABLE +
					" WHERE UPPER("+ COL_PRODUCTEUR + ") LIKE '%"+producteur.toUpperCase()+"%' " +
					" ORDER BY " + COL_PRODUCTEUR;
		if (bdd==null) super.openForRead();		
		return bdd.rawQuery(sql, null);
	}
}
