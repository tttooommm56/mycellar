package fr.kougteam.myCellar.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.res.Resources;
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
import fr.kougteam.myCellar.ui.IconContextMenu;

public class ListeVinsActivity extends TabActivity {
	private final int CONTEXT_MENU_ID = 1;
	private IconContextMenu iconContextMenu = null;
	private final int MENU_RETIRER_ACTION = 1;
	private final int MENU_DETAIL_ACTION = 2;
	private final int MENU_EDITER_ACTION = 3;
	private final int MENU_SUPPRIMER_ACTION = 4;
	private Resources res;
	
	private static final String TAB_ROUGE = "TAB_ROUGE";
	private static final String TAB_BLANC = "TAB_BLANC";
	private static final String TAB_ROSE = "TAB_ROSE";
	
	private VinDao vinDao;
	private SimpleCursorAdapter vinAdapter;
	private ListView vinsRougeListView;
	private ListView vinsBlancListView;
	private ListView vinsRoseListView;
	
	private Cursor selectedItem;
	private String currentTab;
	
	
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.liste_vins);
		res = getResources();
		
		vinDao = new VinDao(this);
		
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
	    	    loadTabList(tabId);
	    	}});
	    
	    vinsRougeListView = (ListView)findViewById(R.id.listeVinsRougeTab);	    
	    vinsBlancListView = (ListView)findViewById(R.id.listeVinsBlancTab);	    
	    vinsRoseListView = (ListView)findViewById(R.id.listeVinsRoseTab);
	    
	    initActionDialog();
	    
	    currentTab = TAB_ROUGE;
	    loadTabList(currentTab);
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
		    	selectedItem = (Cursor)parent.getItemAtPosition(pos);
				showDialog(CONTEXT_MENU_ID);
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
		    	selectedItem = (Cursor)parent.getItemAtPosition(pos);
				showDialog(CONTEXT_MENU_ID);
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
		    	selectedItem = (Cursor)parent.getItemAtPosition(pos);
				showDialog(CONTEXT_MENU_ID);
			}	    
		});
	}
	
	private void initActionDialog() {
		iconContextMenu = new IconContextMenu(this, CONTEXT_MENU_ID);
        iconContextMenu.addItem(res, R.string.retirer, R.drawable.ic_retirer_green, MENU_RETIRER_ACTION);
        iconContextMenu.addItem(res, R.string.detail, R.drawable.ic_loupe_blue, MENU_DETAIL_ACTION);
        iconContextMenu.addItem(res, R.string.editer, R.drawable.ic_edit_yellow, MENU_EDITER_ACTION);
        iconContextMenu.addItem(res, R.string.supprimer, R.drawable.ic_delete_red, MENU_SUPPRIMER_ACTION);
        
        //set onclick listener for context menu
        iconContextMenu.setOnClickListener(new IconContextMenu.IconContextMenuOnClickListener() {
			public void onClick(int menuId) {
				int idVin = selectedItem.getInt(selectedItem.getColumnIndex(VinDao.COL_ID));
				
				switch(menuId) {
				
					case MENU_RETIRER_ACTION :
						int currentStock = selectedItem.getInt(selectedItem.getColumnIndex(VinDao.COL_NB_BOUTEILLES));
						if (vinDao.retire1Bouteille(idVin, currentStock) > 0) {
							loadTabList(currentTab);
							Toast.makeText(getApplicationContext(), "Le stock a été mis à jour.", Toast.LENGTH_LONG).show();
						} else {
							Toast.makeText(getApplicationContext(), "Le stock n'a pas pu être mis à jour.", Toast.LENGTH_LONG).show();
						}					
						break;
						
					case MENU_DETAIL_ACTION :
						Toast.makeText(getApplicationContext(), "You've clicked on menu item 2", Toast.LENGTH_LONG).show();
						break;
						
					case MENU_EDITER_ACTION :
						Toast.makeText(getApplicationContext(), "You've clicked on menu item 3", Toast.LENGTH_LONG).show();
						break;
						
					case MENU_SUPPRIMER_ACTION :
						AlertDialog.Builder builder = new AlertDialog.Builder(ListeVinsActivity.this);
			    		builder.setMessage(R.string.delete_item_msg)
			    		.setCancelable(false)
			    		.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
			    			public void onClick(DialogInterface dialog, int id) {
			    				int idVin = selectedItem.getInt(selectedItem.getColumnIndex(VinDao.COL_ID));
			    				vinDao.delete(idVin);
			    				loadTabList(currentTab);
			    				Toast.makeText(getApplicationContext(), "L'item a été supprimé.", Toast.LENGTH_LONG).show();
			    			}
			    		})
			    		.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
			    			public void onClick(DialogInterface dialog, int id) {
			    			}
			    		});
			    		AlertDialog alert = builder.create();
			    		alert.show();
						break;
				}
			}
        });
	}
	
	/**
	 * create context menu
	 */
	@Override
	protected Dialog onCreateDialog(int id) {
		if (id == CONTEXT_MENU_ID) {
			return iconContextMenu.createMenu("Action");
		}
		return super.onCreateDialog(id);
	}
	
	/**
	 * Charge les données dans l'onglet concerné
	 * 
	 * @param tab l'onglet dans lequel charger les données
	 * 
	 */
	private void loadTabList(String tab) {
		if (TAB_ROUGE.equals(tab)) {
	    	currentTab = TAB_ROUGE;
	    	loadVinsRougeList();
	    	
	    } else if (TAB_BLANC.equals(tab)) {
	    	currentTab = TAB_BLANC;
	    	loadVinsBlancList();
	    	
	    } else if (TAB_ROSE.equals(tab)) {
	    	currentTab = TAB_ROSE;
	    	loadVinsRoseList();
	    }
	}
}
