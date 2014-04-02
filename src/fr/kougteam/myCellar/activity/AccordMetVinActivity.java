package fr.kougteam.myCellar.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import fr.kougteam.myCellar.R;
import fr.kougteam.myCellar.dao.MetDao;
import fr.kougteam.myCellar.dao.MetVinDao;
import fr.kougteam.myCellar.dao.VinDao;
import fr.kougteam.myCellar.modele.Met;
import fr.kougteam.myCellar.modele.MetVin;
import fr.kougteam.myCellar.tools.FileTools;
import fr.kougteam.myCellar.tools.FontTools;
import fr.kougteam.myCellar.tools.StringTools;

public class AccordMetVinActivity extends Activity {
	private MetDao metDao;
	private MetVinDao metVinDao;
	private VinDao vinDao;
	private Met met;

	private ListView vinsCellierListView;
	private Intent intent2DetailVin;
	
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accord_met_vin);
		
		metDao = new MetDao(this);	
		metVinDao = new MetVinDao(this);
		vinDao = new VinDao(this);
		
		View empty = findViewById(R.id.accordMetVinListeVinCellierEmpty);
		vinsCellierListView = (ListView)findViewById(R.id.accordMetVinListeVinCellier);	 
		vinsCellierListView.setEmptyView(empty);
		
		intent2DetailVin = new Intent(this, DetailVinActivity.class);
		
		
		Context context = getBaseContext();
		FileTools.copyFile(context, FontTools.DEFAULT_FONT_NAME);
		
		Bundle extra = this.getIntent().getExtras(); 
		if (extra!=null) {
			met = metDao.getById(extra.getInt("idMet"));
			fillFields(context);
		} 
		
		final ViewGroup mContainer = (ViewGroup) findViewById(android.R.id.content);
        FontTools.setDefaultAppFont(mContainer, getAssets());
        
        
	}
	
	@Override
	protected void onDestroy() {
		metDao.close();
		metVinDao.close();
		super.onDestroy();	
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		// Rafraichissement des donn�es
		if (met!=null && met.getId()>0) {
			met = metDao.getById(met.getId());
			fillFields(getBaseContext());
		}
	}
	
    private void fillFields(Context context) {
		((TextView)findViewById(R.id.accordMetVinSelectectedMet)).setText(met.getNom());
		
		Cursor c = metVinDao.getVinsByIdMet(met.getId());
    
		StringBuilder html = new StringBuilder();
		html.append("<html>"+
						"<head>"+
							"<style type='text/css'>"+
							"@font-face {"+
							"    font-family: MyFont;"+
							"    src: url('file://"+context.getFilesDir().getAbsolutePath()+"/"+FontTools.DEFAULT_FONT_NAME+"')"+
							"}"+
							"body {"+
							"    font-family: MyFont;"+
							"    color:#eee;"+
							"}"+
							"</style>"+
						"</head>"+
					"<body>");
		html.append("<ul>");
		List<String> propositions = new ArrayList<String>();
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			MetVin mv = new MetVin(met.getId(), c.getString(c.getColumnIndex(MetVinDao.COL_NOM_VIN)), c.getString(c.getColumnIndex(MetVinDao.COL_TYPE)));
			propositions.add(mv.getNomVin());
			html.append("<li>");
			html.append(StringTools.escapeHTML(mv.getNomVin()));
			if (mv.getType()!=null && !"".equals(mv.getType())) {
				html.append(" ("+StringTools.escapeHTML(mv.getType())+")");
			}
			html.append("</li>");
		}	
		html.append("</ul></body></html>");
		WebView web = ((WebView)findViewById(R.id.accordMetVinListeVinHtml));
		web.loadDataWithBaseURL(null, html.toString(), "text/html", "utf-8", "");
		web.setBackgroundColor(getResources().getColor(android.R.color.black));
		
		Cursor vinCursor = vinDao.getListVinsByPropositions(propositions);
		String[] from = new String[] { VinDao.COL_PRODUCTEUR, VinDao.COL_NOM, VinDao.COL_ANNEE, VinDao.COL_NB_BOUTEILLES, "nom_appellation" };
		int[] to = new int[] { R.id.listeVinsItemProducteur, R.id.listeVinsItemNom, R.id.listeVinsItemAnnee, R.id.listeVinsItemBouteilles, R.id.listeVinsItemAppellation };
		SimpleCursorAdapter vinAdapter = new SimpleCursorAdapter(this, R.layout.liste_vins_item, vinCursor, from, to) {
            @Override
            public View getView(int position, View convertView, ViewGroup parent){
            	ViewGroup view = (ViewGroup) super.getView(position, convertView, parent);
                if(convertView == null) FontTools.setDefaultAppFont(view, getAssets());
                return view;
            }
        };
		vinsCellierListView.setAdapter(vinAdapter);

		vinsCellierListView.setOnItemClickListener(new OnItemClickListener() {		 
		    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		    	Cursor selectedItem = (Cursor)parent.getItemAtPosition(pos);
		    	int idVin = selectedItem.getInt(selectedItem.getColumnIndex(VinDao.COL_ID));
		    	intent2DetailVin.putExtra("idVin", idVin);
				startActivity(intent2DetailVin);
			}	    
		});

    }
}
