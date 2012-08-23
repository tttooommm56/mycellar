package fr.kougteam.myCellar.activity;

import android.app.AlertDialog;
import android.app.TabActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.Toast;
import fr.kougteam.myCellar.R;
import fr.kougteam.myCellar.dao.VinDao;
import fr.kougteam.myCellar.enums.Couleur;

public class ListeVinsActivity extends TabActivity {
	
	private static final String TAB_ROUGE = "TAB_ROUGE";
	private static final String TAB_BLANC = "TAB_BLANC";
	private static final String TAB_ROSE = "TAB_ROSE";
	
	private Context mCtxt;
	private VinDao vinDao;
	private SimpleCursorAdapter vinAdapter;
	private ListView vinsRougeListView;
	private ListView vinsBlancListView;
	private ListView vinsRoseListView;
	private boolean listRougeLoaded;
	private boolean listBlancLoaded;
	private boolean listRoseLoaded;
	
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.liste_vins);
		mCtxt = getApplicationContext();
		
		vinDao = new VinDao(this);
		
		listRougeLoaded = false;
		listBlancLoaded = false;
		listRoseLoaded = false;
		
		// initialisation des onglets
		TabHost tabs = getTabHost();
	    tabs.setup();
	    
	    TabSpec tspecRouge = tabs.newTabSpec(TAB_ROUGE);       
	    tspecRouge.setIndicator(Couleur.ROUGE.getLabel());
	    tspecRouge.setContent(R.id.listeVinsRougeTab);
        tabs.addTab(tspecRouge); 

        TabSpec tspecBlanc = tabs.newTabSpec(TAB_BLANC);
        tspecBlanc.setIndicator(Couleur.BLANC.getLabel());
        tspecBlanc.setContent(R.id.listeVinsBlancTab);
        tabs.addTab(tspecBlanc);

        TabSpec tspecRose = tabs.newTabSpec(TAB_ROSE);
        tspecRose.setIndicator(Couleur.ROSE.getLabel());
        tspecRose.setContent(R.id.listeVinsRoseTab); 
        
	    tabs.addTab(tspecRose);
	    
	    tabs.setOnTabChangedListener(new OnTabChangeListener(){
	    	public void onTabChanged(String tabId) {
	    	    if (TAB_ROUGE.equals(tabId)) {
	    	    	if (!listRougeLoaded) loadVinsRougeList();
	    	    }
	    	    if (TAB_BLANC.equals(tabId)) {
	    	    	if (!listBlancLoaded) loadVinsBlancList();
	    	    }
	    	    if (TAB_BLANC.equals(tabId)) {
	    	    	if (!listRoseLoaded) loadVinsRoseList();
	    	    }
	    	}});
	    
	    vinsRougeListView = (ListView)findViewById(R.id.listeVinsRougeTab);	    
	    vinsBlancListView = (ListView)findViewById(R.id.listeVinsBlancTab);	    
	    vinsRoseListView = (ListView)findViewById(R.id.listeVinsRoseTab);
	}
	
	@Override
	protected void onDestroy() {
		vinDao.close();
		super.onDestroy();	
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
				showActionDialog(c);
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
				showActionDialog(c);
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
				showActionDialog(c);
			}	    
		});
	}
	
	/**
	 * Affiche la popup contenant les actions sur les vins
	 * 
	 * @param c
	 */
	private void showActionDialog(Cursor c) {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    builder.setTitle("Action");
	        
	    final CharSequence[] items = {
	    		mCtxt.getText(R.string.retirer), 
	    		mCtxt.getText(R.string.detail), 
	    		mCtxt.getText(R.string.editer), 
	    		mCtxt.getText(R.string.supprimer)
	    	};

	    builder.setItems(items, new DialogInterface.OnClickListener(){
            public void onClick(DialogInterface dialogInterface, int item) {
            	switch (item) {
            		// Retirer 1 bouteille
            		case 0 : 
            			break;
            			
            		// Détail
            		case 1 : 
            			break;
            			
            		// Editer
            		case 2 : 
            			break;
            			
            		// Supprimer
            		case 3 : 
            			break;        		
            	}
                Toast.makeText(getApplicationContext(), items[item], Toast.LENGTH_SHORT).show();
            }
        });

	    builder.setPositiveButton("Annuler", 
	    		new DialogInterface.OnClickListener() {
	    			public void onClick(DialogInterface dialog, int which) {}
	    		}
	    );
	    
	    AlertDialog alert = builder.create();
	    alert.show();
	}
}
