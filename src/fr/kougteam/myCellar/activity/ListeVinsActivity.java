package fr.kougteam.myCellar.activity;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.TabActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
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
	
	private Intent intent2View;
	private Intent intent2Edit;
	private Intent intent2Add;
	
	private boolean emptyBottlesOnly = false;
	
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.liste_vins);
		res = getResources();
		
		vinDao = new VinDao(this);
		
		// récupération des paramètres de l'intent
		int tabIndex = 0;
		Bundle bundle = this.getIntent().getExtras();
		if (bundle!=null) {
			emptyBottlesOnly = (Boolean)bundle.get("emptyBottlesOnly");
			if (bundle.get("tabIndex")!=null) {
				tabIndex = (Integer)bundle.get("tabIndex");
			}
		}
		
		// initialisation des onglets
		TabHost tabs = getTabHost();
	    tabs.setup();
	    
	    TabSpec tspecRouge = tabs.newTabSpec(TAB_ROUGE); 
	    int nbRouge = vinDao.getTotalBouteillesByCouleur(Couleur.ROUGE, emptyBottlesOnly);
	    tspecRouge.setIndicator(Couleur.ROUGE.getLabel(getApplicationContext()).toUpperCase()+"\r\n\r\n"+nbRouge+" "+getResources().getString(nbRouge>1?R.string.bouteilles:R.string.bouteille).toLowerCase());    
	    tspecRouge.setContent(R.id.listeVinsRougeTab);
        tabs.addTab(tspecRouge); 
        TextView title = (TextView) tabs.getTabWidget().getChildAt(0).findViewById(android.R.id.title); 
	    title.setSingleLine(false);
	    
        TabSpec tspecBlanc = tabs.newTabSpec(TAB_BLANC);
        int nbBlanc = vinDao.getTotalBouteillesByCouleur(Couleur.BLANC, emptyBottlesOnly);
        tspecBlanc.setIndicator(Couleur.BLANC.getLabel(getApplicationContext()).toUpperCase()+"\r\n\r\n"+nbBlanc+" "+getResources().getString(nbBlanc>1?R.string.bouteilles:R.string.bouteille).toLowerCase()); 
        tspecBlanc.setContent(R.id.listeVinsBlancTab);
        tabs.addTab(tspecBlanc);
        title = (TextView) tabs.getTabWidget().getChildAt(1).findViewById(android.R.id.title); 
	    title.setSingleLine(false);
	    
        TabSpec tspecRose = tabs.newTabSpec(TAB_ROSE);
        int nbRose = vinDao.getTotalBouteillesByCouleur(Couleur.ROSE, emptyBottlesOnly);
        tspecRose.setIndicator(Couleur.ROSE.getLabel(getApplicationContext()).toUpperCase()+"\r\n\r\n"+nbRose+" "+getResources().getString(nbRose>1?R.string.bouteilles:R.string.bouteille).toLowerCase()); 
        tspecRose.setContent(R.id.listeVinsRoseTab);       
	    tabs.addTab(tspecRose);
	    title = (TextView) tabs.getTabWidget().getChildAt(2).findViewById(android.R.id.title); 
	    title.setSingleLine(false);
	       
	    tabs.setOnTabChangedListener(new OnTabChangeListener(){
	    	public void onTabChanged(String tabId) {
	    	    loadTabList(tabId);
	    	}});
	    
	    vinsRougeListView = (ListView)findViewById(R.id.listeVinsRougeTab);	    
	    vinsBlancListView = (ListView)findViewById(R.id.listeVinsBlancTab);	    
	    vinsRoseListView = (ListView)findViewById(R.id.listeVinsRoseTab);
	    
	    initActionDialog();
	    
	    
	    switch (tabIndex) {
	    	case 0 : currentTab = TAB_ROUGE; break;
	    	case 1 : currentTab = TAB_BLANC; break;
	    	case 2 : currentTab = TAB_ROSE; break;
	    	default : currentTab = TAB_ROUGE;
	    }
	    
	    loadTabList(currentTab);
	    tabs.setCurrentTab(tabIndex);
	    
	    intent2View = new Intent(this, DetailVinActivity.class);
	    intent2Edit = new Intent(this, EditVinFormActivity.class);
	    intent2Add = new Intent(this, EditVinFormActivity.class);
	    
	    
	}
	
	@Override
	protected void onDestroy() {
		vinDao.close();
		super.onDestroy();	
	}
	
	@Override
	protected void onResume() {
		// Mise à jour des données de l'onglet courant
		loadTabList(currentTab);
		super.onResume();
	}
	
	/**
	 * Méthode qui se déclenchera lorsque vous appuierez sur le bouton menu du téléphone
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
      //Création d'un MenuInflater qui va permettre d'instancier un Menu XML en un objet Menu
      MenuInflater inflater = getMenuInflater();
      //Instanciation du menu XML spécifier en un objet Menu
      inflater.inflate(R.layout.liste_vins_menu, menu); 
      return true;	
	};
	
    /**
     * Méthode qui se déclenchera au clic sur un item du menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         //On regarde quel item a été cliqué grâce à son id et on déclenche une action
         switch (item.getItemId()) {
            case R.id.listeVinAjouter:
            	startActivity(intent2Add);	
            	return true;
         }
         return false;
    }
	
	private void loadVinsRougeList(boolean emptyBottlesOnly) {
		Cursor vinCursor = vinDao.getListVinsDisposByCouleur(Couleur.ROUGE, emptyBottlesOnly);
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
	
	private void loadVinsBlancList(boolean emptyBottlesOnly) {
		Cursor vinCursor = vinDao.getListVinsDisposByCouleur(Couleur.BLANC, emptyBottlesOnly);
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
	
	private void loadVinsRoseList(boolean emptyBottlesOnly) {
		Cursor vinCursor = vinDao.getListVinsDisposByCouleur(Couleur.ROSE, emptyBottlesOnly);
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
		if (!emptyBottlesOnly) {
			// On ne peut pas retirer de bouteille si on est dans la liste des bouteilles vides !
			iconContextMenu.addItem(res, R.string.retirer, R.drawable.ic_retirer_green, MENU_RETIRER_ACTION);
		}
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
							Toast.makeText(getApplicationContext(), R.string.stock_maj_ok, Toast.LENGTH_LONG).show();
						} else {
							Toast.makeText(getApplicationContext(), R.string.stock_maj_ko, Toast.LENGTH_LONG).show();
						}					
						break;
						
					case MENU_DETAIL_ACTION :
						intent2View.putExtra("idVin", idVin);
						startActivity(intent2View);
						break;
						
					case MENU_EDITER_ACTION :
						intent2Edit.putExtra("idVin", idVin);
						startActivity(intent2Edit);
						break;
						
					case MENU_SUPPRIMER_ACTION :
						AlertDialog.Builder builder = new AlertDialog.Builder(ListeVinsActivity.this);
			    		builder.setTitle(R.string.warning)
			    		.setIcon(android.R.drawable.ic_dialog_alert)
			    		.setMessage(R.string.delete_item_msg)
			    		.setCancelable(false)
			    		.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
			    			public void onClick(DialogInterface dialog, int id) {
			    				int idVin = selectedItem.getInt(selectedItem.getColumnIndex(VinDao.COL_ID));
			    				vinDao.delete(idVin);
			    				loadTabList(currentTab);
			    				Toast.makeText(getApplicationContext(), R.string.item_deleted, Toast.LENGTH_LONG).show();
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
			return iconContextMenu.createMenu(R.string.action, android.R.drawable.ic_menu_more);
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
	    	loadVinsRougeList(emptyBottlesOnly);
	    	
	    } else if (TAB_BLANC.equals(tab)) {
	    	currentTab = TAB_BLANC;
	    	loadVinsBlancList(emptyBottlesOnly);
	    	
	    } else if (TAB_ROSE.equals(tab)) {
	    	currentTab = TAB_ROSE;
	    	loadVinsRoseList(emptyBottlesOnly);
	    }
	}
}
