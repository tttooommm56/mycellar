package fr.kougteam.myCellar.adapter;

import java.io.File;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.SimpleCursorAdapter;
import android.widget.TableRow;
import fr.kougteam.myCellar.R;
import fr.kougteam.myCellar.dao.VinDao;
import fr.kougteam.myCellar.provider.ImageContentProvider;
import fr.kougteam.myCellar.tools.FontTools;

public class VinCursorAdapter extends SimpleCursorAdapter {
	private Cursor cursor;
    private Context context;
    private static final String[] FROM = new String[] {VinDao.COL_ID, VinDao.COL_PRODUCTEUR, VinDao.COL_NOM, VinDao.COL_ANNEE, VinDao.COL_NB_BOUTEILLES, "nom_appellation" };
	private static final int[] TO = new int[] {R.id.footer, R.id.listeVinsItemProducteur, R.id.listeVinsItemNom, R.id.listeVinsItemAnnee, R.id.listeVinsItemBouteilles, R.id.listeVinsItemAppellation };
	
	public VinCursorAdapter(Context context, int layout, Cursor c) {
	    super(context, layout, c, FROM, TO);
	    this.cursor = c;
	    this.context = context;
	}
	
	public View getView(int position, View convertView, ViewGroup parent){
    	ViewGroup view = (ViewGroup) super.getView(position, convertView, parent);
        if(convertView == null) {
        	FontTools.setDefaultAppFont(view, this.context.getAssets());        	
        } 
    	this.getCursor().moveToPosition(position);
    	File etiquetteFile = new File(ImageContentProvider.IMAGE_DIRECTORY, "etq_"+this.cursor.getLong(this.cursor.getColumnIndex(VinDao.COL_ID))+".jpg");
		if (etiquetteFile.exists()) {
			ImageView imageView = (ImageView) view.findViewById(R.id.listeVinsItemEtiquetteImg);
			ImageContentProvider.fillImageViewWithFileResized(imageView, etiquetteFile, 65, 80);	
		}
         
        return view;
    }
}
