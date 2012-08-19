package fr.kougteam.myCellar.activity;

import android.app.TabActivity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import fr.kougteam.myCellar.R;
import fr.kougteam.myCellar.dao.VinDao;
import fr.kougteam.myCellar.enums.Couleur;

public class ListeVinsActivity extends TabActivity {
	private VinDao vinDao;
	private SimpleCursorAdapter vinAdapter;
	private ListView vinsRougeListView;
	private ListView vinsBlancListView;
	private ListView vinsRoseListView;
	
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.liste_vins);
		
		vinDao = new VinDao(this);
		
		// initialisation des onglets
		TabHost tabs = getTabHost();
	    tabs.setup();
	    
	    TabSpec tspecRouge = tabs.newTabSpec("Tab1");       
	    tspecRouge.setIndicator(Couleur.ROUGE.getLabel());
	    tspecRouge.setContent(R.id.listeVinsRougeTab);
        tabs.addTab(tspecRouge); 

        TabSpec tspecBlanc = tabs.newTabSpec("Tab2");
        tspecBlanc.setIndicator(Couleur.BLANC.getLabel());
        tspecBlanc.setContent(R.id.listeVinsBlancTab);
        tabs.addTab(tspecBlanc);

        TabSpec tspecRose = tabs.newTabSpec("Tab3");
        tspecRose.setIndicator(Couleur.ROSE.getLabel());
        tspecRose.setContent(R.id.listeVinsRoseTab); 
        
	    tabs.addTab(tspecRose);
	    
	    vinsRougeListView = (ListView)findViewById(R.id.listeVinsRougeTab);
	    loadVinsRougeList();
	    
	    vinsBlancListView = (ListView)findViewById(R.id.listeVinsBlancTab);
	    loadVinsBlancList();
	    
	    vinsRoseListView = (ListView)findViewById(R.id.listeVinsRoseTab);
	    loadVinsRoseList();
	}
	
	private void loadVinsRougeList() {
		Cursor vinCursor = vinDao.getListVinsDisposByCouleur(Couleur.ROUGE);
		String[] from = new String[] { VinDao.COL_PRODUCTEUR, VinDao.COL_ANNEE, VinDao.COL_NB_BOUTEILLES, "nom_appellation" };
		int[] to = new int[] { R.id.listeVinsItemProducteur, R.id.listeVinsItemAnnee, R.id.listeVinsItemBouteilles, R.id.listeVinsItemAppellation };
		vinAdapter = new SimpleCursorAdapter(this, R.layout.liste_vins_item, vinCursor, from, to);
		vinsRougeListView.setAdapter(vinAdapter);

		vinsRougeListView.setOnItemClickListener(new OnItemClickListener() {		 
		    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				Cursor c = (Cursor)parent.getItemAtPosition(pos);
				//TODO
			}	    
		});
	}
	
	private void loadVinsBlancList() {
		Cursor vinCursor = vinDao.getListVinsDisposByCouleur(Couleur.BLANC);
		String[] from = new String[] { VinDao.COL_PRODUCTEUR, VinDao.COL_ANNEE, VinDao.COL_NB_BOUTEILLES, "nom_appellation" };
		int[] to = new int[] { R.id.listeVinsItemProducteur, R.id.listeVinsItemAnnee, R.id.listeVinsItemBouteilles, R.id.listeVinsItemAppellation };
		vinAdapter = new SimpleCursorAdapter(this, R.layout.liste_vins_item, vinCursor, from, to);
		vinsBlancListView.setAdapter(vinAdapter);

		vinsBlancListView.setOnItemClickListener(new OnItemClickListener() {		 
		    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				Cursor c = (Cursor)parent.getItemAtPosition(pos);
				//TODO
			}	    
		});
	}
	
	private void loadVinsRoseList() {
		Cursor vinCursor = vinDao.getListVinsDisposByCouleur(Couleur.ROSE);
		String[] from = new String[] { VinDao.COL_PRODUCTEUR, VinDao.COL_ANNEE, VinDao.COL_NB_BOUTEILLES, "nom_appellation" };
		int[] to = new int[] { R.id.listeVinsItemProducteur, R.id.listeVinsItemAnnee, R.id.listeVinsItemBouteilles, R.id.listeVinsItemAppellation };
		vinAdapter = new SimpleCursorAdapter(this, R.layout.liste_vins_item, vinCursor, from, to);
		vinsRoseListView.setAdapter(vinAdapter);

		vinsRoseListView.setOnItemClickListener(new OnItemClickListener() {		 
		    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				Cursor c = (Cursor)parent.getItemAtPosition(pos);
				//TODO
			}	    
		});
	}
}
