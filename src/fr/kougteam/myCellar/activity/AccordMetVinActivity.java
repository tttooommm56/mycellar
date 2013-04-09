package fr.kougteam.myCellar.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
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
import fr.kougteam.myCellar.enums.Couleur;
import fr.kougteam.myCellar.modele.Met;
import fr.kougteam.myCellar.modele.MetVin;
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
		
		Bundle extra = this.getIntent().getExtras(); 
		if (extra!=null) {
			met = metDao.getById(extra.getInt("idMet"));
			fillFields();
		} 
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
		
		// Rafraichissement des données
		if (met!=null && met.getId()>0) {
			met = metDao.getById(met.getId());
			fillFields();
		}
	}
	
    private void fillFields() {
		((TextView)findViewById(R.id.accordMetVinSelectectedMet)).setText(met.getNom());
		
		Cursor c = metVinDao.getListByIdMet(met.getId());
    
		StringBuilder html = new StringBuilder("<ul style='color:#eee;'>");
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
		html.append("</ul>");
		WebView web = ((WebView)findViewById(R.id.accordMetVinListeVinHtml));
		web.loadData(html.toString(), "text/html", "utf-8");
		web.setBackgroundColor(getResources().getColor(android.R.color.black));
		
		Cursor vinCursor = vinDao.getListVinsByPropositions(propositions);
		String[] from = new String[] { VinDao.COL_PRODUCTEUR, VinDao.COL_ANNEE, VinDao.COL_NB_BOUTEILLES, "nom_appellation" };
		int[] to = new int[] { R.id.listeVinsItemProducteur, R.id.listeVinsItemAnnee, R.id.listeVinsItemBouteilles, R.id.listeVinsItemAppellation };
		SimpleCursorAdapter vinAdapter = new SimpleCursorAdapter(this, R.layout.liste_vins_item, vinCursor, from, to);
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
