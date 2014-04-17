package fr.kougteam.myCellar.dao;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.text.format.DateFormat;
import android.util.Log;
import fr.kougteam.myCellar.enums.Couleur;
import fr.kougteam.myCellar.helper.FileHelper;
import fr.kougteam.myCellar.modele.Vin;
import fr.kougteam.myCellar.provider.ImageContentProvider;

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
	public static final String COL_ANNEE_MATURITE		= "annee_maturite";
	public static final String COL_PRIX			= "prix";
	public static final String COL_ETAGERE		= "etagere";
	public static final String COL_DATE_AJOUT	= "date_ajout";
	
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
				COL_ANNEE_MATURITE + " integer, " +
				COL_PRIX + " real, " +
				COL_ETAGERE + " text, " +
				COL_DATE_AJOUT + " text " +
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
		if (oldVersion<3 && newVersion>=3) {
			database.execSQL("ALTER TABLE " + TABLE + " ADD "+COL_IMAGE+" BLOB");
		}
		if (oldVersion<5 && newVersion>=5) {
			database.execSQL("ALTER TABLE " + TABLE + " ADD "+COL_ANNEE_MATURITE+" integer");
			database.execSQL("ALTER TABLE " + TABLE + " ADD "+COL_PRIX+" real");
			database.execSQL("ALTER TABLE " + TABLE + " ADD "+COL_ETAGERE+" text");
			database.execSQL("ALTER TABLE " + TABLE + " ADD "+COL_DATE_AJOUT+" text");
		}
		if (oldVersion<7 && newVersion>=7) {
			// L'étiquette est maintenant stockée sur la carte SD		
			File destDir= new File(ImageContentProvider.IMAGE_DIRECTORY);
			if (!destDir.exists()) {
				destDir.mkdirs();
			}
			
			Cursor c = database.query(TABLE, new String[]{COL_ID, COL_IMAGE}, null, null, null, null, null);
			for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				long id = c.getLong(c.getColumnIndex(COL_ID));
				byte[] image = c.getBlob(c.getColumnIndex(COL_IMAGE));
				if (image!=null && image.length>0) {
					try {
						File imgFile = new File(ImageContentProvider.IMAGE_DIRECTORY, "etq_"+Long.toString(id)+".jpg");
						BufferedOutputStream bos = new BufferedOutputStream(new FileOutputStream(imgFile));
						bos.write(image);
						bos.flush();
						bos.close();
					} catch (IOException ioe) {
						ioe.printStackTrace();
					}
				}
			}
			c.moveToFirst();
		}
	}
	
	/**
	 * Retourne les donn�es contenu dans l'objet sous forme de ContentValues
	 * 
	 * @param p l'objet contenant les donn�e
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
		cv.put(COL_PRIX, v.getPrix());
		cv.put(COL_ANNEE_MATURITE, v.getAnneeMaturite());
		cv.put(COL_ETAGERE, v.getEtagere());
		if (v.getDateAjout()!=null) {
			cv.put(COL_DATE_AJOUT, DateFormat.format("yyyy-MM-dd", v.getDateAjout()).toString());
		}
		return cv;
	}
	
	public long insert(Vin v) {
		return super.insert(TABLE, getContentValues(v, true));
	}
	
	public long update(Vin v) {
		return super.update(TABLE, getContentValues(v, false), v.getId());
	}
	
	public void delete(long id) {
		super.delete(TABLE, id);
		// Suppression de l'image de l'étiquette qui est sur la carte SD
		if (FileHelper.isSDPresent() && FileHelper.canWriteOnSD()) {
			File fileDir= new File(ImageContentProvider.IMAGE_DIRECTORY, "etq_"+id+".jpg");
			if (fileDir.exists()) {
				fileDir.delete();
			}
		}
	}
	
	public long retire1Bouteille(long id, int currentStock) {
		if (bdd==null) openForWrite();
		ContentValues cv = new ContentValues();
		cv.put(COL_NB_BOUTEILLES, currentStock-1);
		return bdd.update(TABLE, cv, COL_ID + " = " + id, null);
	}
	
	public Vin getById(long id) {
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
								COL_ANNEE_MATURITE + ", " + 
								COL_PRIX + ", " + 
								COL_ETAGERE + ", " + 
								COL_DATE_AJOUT+ " " +
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
			v.setAnneeMaturite(c.getInt(i++));
			v.setPrix(c.getFloat(i++));
			v.setEtagere(c.getString(i++));
			try {
				String dateStr = c.getString(i++);
				if (dateStr!=null && !"".equals(dateStr)) {
					SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
				    Date convertedDate = null;
				    convertedDate = dateFormat.parse(dateStr);
					v.setDateAjout(convertedDate);
				}
			} catch (ParseException e) {
				Log.e("Parse error for 'dateAjout' !", e.getMessage());
			}
		}
		c.close();
		return v;
	}
	
	public int getTotalBouteillesByCouleur(final Couleur couleur, final boolean emptyBottlesOnly, final int filterPaysId, final int filterRegionId, final int filterAppellationId, final int filterAnneeMaturite) {
		int total = 0;
		String sql = " SELECT SUM( "+ COL_NB_BOUTEILLES + ")" +
					 " FROM " + TABLE +
					 " WHERE " + COL_COULEUR + "= '" + couleur.name() + "' " ;
		
		if (emptyBottlesOnly) {
			sql += " AND " + COL_NB_BOUTEILLES + " = 0 ";
			
		} else {
			sql += " AND " + COL_NB_BOUTEILLES + " > 0 ";
		}
		
		if (filterAnneeMaturite != -1) {
			sql += " AND " + COL_ANNEE_MATURITE + " = " +filterAnneeMaturite;
		} else if (filterAppellationId != -1) {
			sql += " AND " + COL_APPELLATION + " = " +filterAppellationId;
		} else if (filterRegionId != -1) {
			sql += " AND " + COL_REGION + " = " +filterRegionId;
		} else if (filterPaysId != -1) {
			sql += " AND " + COL_PAYS + " = " +filterPaysId;
		} 
		
		if (bdd==null) super.openForRead();
		Cursor c = bdd.rawQuery(sql, null);
		if (c.getCount()==1) {
			c.moveToFirst();
			total = c.getInt(0);
		}
		c.close();
		return total;
	}
	
	public Cursor getListVinsDisposByCouleur(final Couleur couleur, final boolean emptyBottlesOnly, final int filterPaysId, final int filterRegionId, final int filterAppellationId, final int filterAnneeMaturite) {
		String sql = " SELECT v."+COL_ID + ", " + 
							COL_COULEUR + ", " + 
							COL_PAYS + " , " +
							COL_REGION + ", " +
							COL_APPELLATION + ", " +
							COL_ANNEE + ", " +
							"v."+COL_NOM + " "+COL_NOM + ", " +
							COL_PRODUCTEUR + ", " +
							COL_COMMENTAIRES + ", " +
							COL_NB_BOUTEILLES + ", " +
							COL_NOTE + ", " + 
							COL_ANNEE_MATURITE + ", " + 
							COL_PRIX + ", " + 
							COL_ETAGERE + ", " + 
							COL_DATE_AJOUT+ ", " +
							" CASE WHEN "+COL_APPELLATION+"<0 THEN v." + COL_NOM + " ELSE a." + AppellationDao.COL_NOM + " END as nom_appellation " +
					" FROM " + TABLE + " v " +
					" LEFT JOIN " + AppellationDao.TABLE + " a ON a."+AppellationDao.COL_ID+"=v."+COL_APPELLATION +
					" WHERE " + COL_COULEUR + "= '" + couleur.name() + "' " ;
		
		if (emptyBottlesOnly) {
			sql += " AND " + COL_NB_BOUTEILLES + " = 0 ";
			
		} else {
			sql += " AND " + COL_NB_BOUTEILLES + " > 0 ";
		}
		
		if (filterAnneeMaturite != -1) {
			sql += " AND " + COL_ANNEE_MATURITE + " = " +filterAnneeMaturite;
		} else if (filterAppellationId != -1) {
			sql += " AND " + COL_APPELLATION + " = " +filterAppellationId;
		} else if (filterRegionId != -1) {
			sql += " AND " + COL_REGION + " = " +filterRegionId;
		} else if (filterPaysId != -1) {
			sql += " AND " + COL_PAYS + " = " +filterPaysId;
		} 

		sql +=	" ORDER BY " + COL_ANNEE + " DESC, nom_appellation, " + COL_PRODUCTEUR + ", v." + COL_NOM;
		
		if (bdd==null) super.openForRead();		
		
		return bdd.rawQuery(sql, null);
	}
	
	/**
	 * Retourne la liste des vins disponibles dans le cellier 
	 * correspondant aux proposition de vins faites par le module des accords met/vin.
	 * 
	 * @param propositions
	 * @return
	 */
	public Cursor getListVinsByPropositions(final List<String> propositions) {
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
							COL_ANNEE_MATURITE + ", " + 
							COL_PRIX + ", " + 
							COL_ETAGERE + ", " + 
							COL_DATE_AJOUT+ ", " +
							" CASE WHEN "+COL_APPELLATION+"<0 THEN v." + COL_NOM + " ELSE a." + AppellationDao.COL_NOM + " END as nom_appellation " +
					" FROM " + TABLE + " v " +
					" LEFT JOIN " + AppellationDao.TABLE + " a ON a."+AppellationDao.COL_ID+"=v."+COL_APPELLATION +
					" WHERE " + COL_NB_BOUTEILLES + " > 0 AND (0=1 ";
		for (String p : propositions) {
			sql += " OR UPPER(REPLACE(a."+AppellationDao.COL_NOM+",'-',' ')) LIKE '%"+p.replaceAll("-", " ").replaceAll("'", "''").toUpperCase()+"%' ";
		}
		sql += ") ";
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
								COL_NOTE + ", " + 
								COL_ANNEE_MATURITE + ", " + 
								COL_PRIX + ", " + 
								COL_ETAGERE + ", " + 
								COL_DATE_AJOUT+ " " +
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
	
	public Cursor getRegionsWithNbBouteillesByPays(int idPays) {
		String sql = " SELECT DISTINCT r." + RegionDao.COL_ID + " AS "+RegionDao.COL_ID + ", r." + RegionDao.COL_NOM + " AS " + RegionDao.COL_NOM + ", SUM(v." + COL_NB_BOUTEILLES + ") AS " + COL_NB_BOUTEILLES + 
					 " FROM " + TABLE + " v "+
					 " JOIN " + RegionDao.TABLE + " r ON r."+RegionDao.COL_ID+"="+"v."+COL_REGION + 
					 " WHERE r." + RegionDao.COL_ID + "!=-1 AND v."+COL_NB_BOUTEILLES+">0 AND r." + RegionDao.COL_REGION_PARENT + "=0 AND r." + RegionDao.COL_PAYS + " = " + idPays +
					 " GROUP BY r." + RegionDao.COL_ID + ", r." + RegionDao.COL_NOM +
					 " UNION " +
					 " SELECT DISTINCT r1." + RegionDao.COL_ID + " AS "+RegionDao.COL_ID + ", r1." + RegionDao.COL_NOM + " AS " + RegionDao.COL_NOM + ", SUM(v." + COL_NB_BOUTEILLES + ") AS " + COL_NB_BOUTEILLES + 
					 " FROM " + TABLE + " v "+
					 " JOIN " + RegionDao.TABLE + " r2 ON r2."+RegionDao.COL_ID+"="+"v."+COL_REGION + 
					 " JOIN " + RegionDao.TABLE + " r1 ON r1."+RegionDao.COL_ID+"="+"r2."+RegionDao.COL_REGION_PARENT + 
					 " WHERE r1." + RegionDao.COL_ID+ "!=-1 AND v."+COL_NB_BOUTEILLES+">0 AND r2." + RegionDao.COL_REGION_PARENT + "!=0 AND r2." + RegionDao.COL_PAYS + " = " + idPays +
					 " GROUP BY r1." + RegionDao.COL_ID + ", r1." + RegionDao.COL_NOM +
					 " ORDER BY " + RegionDao.COL_NOM;
		if (bdd==null) super.openForRead();		
		return bdd.rawQuery(sql, null);
	}
	
	public Cursor getSousRegionsWithNbBouteillesByRegion(int idRegion) {
		String sql = " SELECT DISTINCT r." + RegionDao.COL_ID + ", r." + RegionDao.COL_NOM + ", SUM(v." + COL_NB_BOUTEILLES + ") AS " + COL_NB_BOUTEILLES + 
				 	 " FROM " + TABLE + " v "+
				 	 " JOIN " + RegionDao.TABLE + " r ON r."+RegionDao.COL_ID+"="+"v."+COL_REGION+
					 " WHERE r." + RegionDao.COL_ID + "!=-1 AND v."+COL_NB_BOUTEILLES+">0 AND r." + RegionDao.COL_REGION_PARENT + " = " + idRegion  +
					 " GROUP BY r." + RegionDao.COL_ID + ", r." + RegionDao.COL_NOM +
					 " ORDER BY r." + RegionDao.COL_NOM ;
		if (bdd==null) super.openForRead();		
		return bdd.rawQuery(sql, null);
	}
	
	public Cursor getAppellationsWithNbBouteillesByRegion(int idRegion) {
		String sql = " SELECT DISTINCT a." + AppellationDao.COL_ID + ", a." + AppellationDao.COL_NOM + ", SUM(v." + COL_NB_BOUTEILLES + ") AS " + COL_NB_BOUTEILLES + 
					 " FROM " + TABLE + " v "+
					 " JOIN " + AppellationDao.TABLE + " a ON a."+AppellationDao.COL_ID+"="+"v."+COL_APPELLATION+
					 " WHERE v."+COL_APPELLATION + "!=-1 AND v."+COL_NB_BOUTEILLES+">0 AND a." + AppellationDao.COL_REGION + " = " + idRegion  +
					 " GROUP BY a." + AppellationDao.COL_ID + ", a." + AppellationDao.COL_NOM +
					 " ORDER BY a." + AppellationDao.COL_NOM ;
		if (bdd==null) super.openForRead();		
		return bdd.rawQuery(sql, null);
	}
	
	public Cursor getAnneeMaturiteWithNbBouteilles() {
		String sql = " SELECT DISTINCT " + VinDao.COL_ANNEE_MATURITE+ " as _id, SUM(" + COL_NB_BOUTEILLES + ") AS " + COL_NB_BOUTEILLES + 
					 " FROM " + TABLE + 
					 " WHERE "+COL_NB_BOUTEILLES+">0 AND " + VinDao.COL_ANNEE_MATURITE + " IS NOT NULL " +
					 " GROUP BY 1" +
					 " ORDER BY 1";
		if (bdd==null) super.openForRead();		
		return bdd.rawQuery(sql, null);
	}
	
	public Cursor getPaysWithNbBouteilles() {
		String sql = " SELECT DISTINCT a." + PaysDao.COL_ID + ", a." + PaysDao.COL_NOM + ", SUM(v." + COL_NB_BOUTEILLES + ") AS " + COL_NB_BOUTEILLES + 
					 " FROM " + TABLE + " v "+
					 " JOIN " + PaysDao.TABLE + " a ON a."+PaysDao.COL_ID+"="+"v."+COL_PAYS+
					 " WHERE v."+COL_PAYS+"!=-1 AND v."+COL_NB_BOUTEILLES+">0 " +
					 " GROUP BY a." + PaysDao.COL_ID + ", a." + PaysDao.COL_NOM +
					 " ORDER BY a." + PaysDao.COL_NOM ;
		if (bdd==null) super.openForRead();		
		return bdd.rawQuery(sql, null);
	}
	
	
	public List<Integer> getDistinctPays(final boolean emptyBottlesOnly) {
		List<Integer> list = new ArrayList<Integer>();
		String sql = " SELECT DISTINCT " +COL_PAYS+ 
					 " FROM " + TABLE +
					 " WHERE "+COL_PAYS+"!=-1 ";
		if (emptyBottlesOnly) {
			sql += " AND " + COL_NB_BOUTEILLES + " = 0 ";
			
		} else {
			sql += " AND " + COL_NB_BOUTEILLES + " > 0 ";
		}
		
		if (bdd==null) super.openForRead();
		Cursor c = bdd.rawQuery(sql, null);
		while (c.moveToNext()) {
			list.add(c.getInt(0));
		}
		c.close();
		return list;
	}
}
