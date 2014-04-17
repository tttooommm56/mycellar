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
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.TabHost;
import android.widget.TabHost.OnTabChangeListener;
import android.widget.TabHost.TabSpec;
import android.widget.TextView;
import android.widget.Toast;
import fr.kougteam.myCellar.R;
import fr.kougteam.myCellar.adapter.VinCursorAdapter;
import fr.kougteam.myCellar.dao.VinDao;
import fr.kougteam.myCellar.enums.Couleur;
import fr.kougteam.myCellar.tools.FontTools;
import fr.kougteam.myCellar.ui.IconContextMenu;

public class ListeVinsActivity extends TabActivity {
	private final int CONTEXT_MENU_ID = 1;
	private IconContextMenu iconContextMenu = null;
	private final int MENU_RETIRER_ACTION = 1;
	private final int MENU_DETAIL_ACTION = 2;
	private final int MENU_EDITER_ACTION = 3;
	private final int MENU_SUPPRIMER_ACTION = 4;
	private Resources res;
	
	private TabHost tabs;
	
	private VinDao vinDao;
	private ListView vinsRougeListView;
	private ListView vinsBlancListView;
	private ListView vinsRoseListView;
	
	private Cursor selectedItem;
	private Couleur currentTab = Couleur.ROUGE;
	
	private Intent intent2View;
	private Intent intent2Edit;
	private Intent intent2Add;
	
	private boolean emptyBottlesOnly = false;
	private int filterAnneeMaturite = -1;
	private int filterAppellationId = -1;
	private int filterRegionId = -1;
	private int filterPaysId = -1;
	
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.liste_vins);
		res = getResources();
		
		vinDao = new VinDao(this);
		
		// récupération des paramétres de l'intent
		Bundle bundle = this.getIntent().getExtras();
		if (bundle!=null) {
			if (bundle.get("emptyBottlesOnly")!=null) {
				emptyBottlesOnly = (Boolean)bundle.get("emptyBottlesOnly");
			}
			if (bundle.get("tabIndex")!=null) {
				currentTab = Couleur.getFromTabIndex((Integer)bundle.get("tabIndex"));
			}
			// Filtrage par année de maturite
			if (bundle.get("anneeMaturite")!=null) {
				filterAnneeMaturite = (Integer)bundle.get("anneeMaturite");		
			}
			// Filtrage par région
			if (bundle.get("appellationId")!=null) {
				filterAppellationId = (Integer)bundle.get("appellationId");		
			}
			if (bundle.get("sousRegionId")!=null) {
				filterRegionId = (Integer)bundle.get("sousRegionId");			
			}
			if (bundle.get("regionId")!=null && filterRegionId==-1) {
				filterRegionId = (Integer)bundle.get("regionId");		
			}
			if (bundle.get("paysId")!=null) {
				filterPaysId = (Integer)bundle.get("paysId");
			}
		}
		
		// initialisation des onglets
		tabs = getTabHost();
	    tabs.setup();
	    
	    // Laisser dans cet ordre pour permettre l'affichage de l'onglet ayant des données par défaut
	    TabSpec tSpecRose = buildTabSpec(Couleur.ROSE, R.id.listeVinsRoseTab);
	    TabSpec tSpecBlanc = buildTabSpec(Couleur.BLANC, R.id.listeVinsBlancTab);
	    TabSpec tSpecRouge = buildTabSpec(Couleur.ROUGE, R.id.listeVinsRougeTab);
	    
	    tabs.addTab(tSpecRouge); 
	    TextView title = (TextView) tabs.getTabWidget().getChildAt(0).findViewById(android.R.id.title); 
	    title.setSingleLine(false);

	    tabs.addTab(tSpecBlanc);
	    title = (TextView) tabs.getTabWidget().getChildAt(1).findViewById(android.R.id.title); 
	    title.setSingleLine(false);
	       
	    tabs.addTab(tSpecRose);
	    title = (TextView) tabs.getTabWidget().getChildAt(2).findViewById(android.R.id.title); 
	    title.setSingleLine(false);


	    tabs.setOnTabChangedListener(new OnTabChangeListener(){
	    	public void onTabChanged(String tabId) {
	    		currentTab = Couleur.getFromId(tabId);
	    		loadVinsList(currentTab);
	    	}});
	    
	    vinsRougeListView = (ListView)findViewById(R.id.listeVinsRougeTab);	    
	    vinsBlancListView = (ListView)findViewById(R.id.listeVinsBlancTab);	    
	    vinsRoseListView = (ListView)findViewById(R.id.listeVinsRoseTab);
	    
	    initActionDialog();
	   
	    loadVinsList(currentTab);
	    tabs.setCurrentTab(currentTab.getTabIndex());
	    
	    intent2View = new Intent(this, DetailVinActivity.class);
	    intent2Edit = new Intent(this, EditVinFormActivity.class);
	    intent2Add = new Intent(this, EditVinFormActivity.class);
	    
	    final ViewGroup mContainer = (ViewGroup) findViewById(android.R.id.content);
        FontTools.setDefaultAppFont(mContainer, getAssets());
	}
	
	@Override
	protected void onDestroy() {
		vinDao.close();
		super.onDestroy();	
	}
	
	@Override
	protected void onResume() {
		refreshTabTitle();
		// Mise à jour des données de l'onglet courant
		loadVinsList(currentTab);	
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
	
    private TabSpec buildTabSpec(final Couleur couleur, final int contentViewId) {
    	TabSpec tspec = tabs.newTabSpec(couleur.getCode()); 
	    int nb = vinDao.getTotalBouteillesByCouleur(couleur, emptyBottlesOnly, filterPaysId, filterRegionId, filterAppellationId, filterAnneeMaturite);
	    tspec.setIndicator(couleur.getLabel(getApplicationContext()).toUpperCase()+" ["+nb+"]\r\n");    
	    tspec.setContent(contentViewId);
	    if (nb>0) {
	    	currentTab = couleur;
	    }
	    return tspec;
    }
    
    private void loadVinsList(final Couleur couleur) {
    	Cursor vinCursor = vinDao.getListVinsDisposByCouleur(couleur, emptyBottlesOnly, filterPaysId, filterRegionId, filterAppellationId, filterAnneeMaturite);
		ListView listView = null;
		switch (couleur) {
			case ROUGE : listView = vinsRougeListView; break;
			case ROSE : listView = vinsRoseListView; break;
			case BLANC : listView = vinsBlancListView; break;
		}
    	if (listView!=null) {
    		listView.setAdapter(new VinCursorAdapter(this, R.layout.liste_vins_item, vinCursor));
    		listView.setOnItemClickListener(new OnItemClickListener() {		 
			    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
			    	selectedItem = (Cursor)parent.getItemAtPosition(pos);
					showDialog(CONTEXT_MENU_ID);
				}	    
			});
    	}
    }
	
	private void initActionDialog() {
		iconContextMenu = new IconContextMenu(this, CONTEXT_MENU_ID);
		if (!emptyBottlesOnly) {
			// On ne peut pas retirer de bouteille si on est dans la liste des bouteilles vides !
			iconContextMenu.addItem(res, R.string.retirer, R.drawable.ic_retirer_red, MENU_RETIRER_ACTION);
		}
        iconContextMenu.addItem(res, R.string.detail, R.drawable.ic_loupe_red, MENU_DETAIL_ACTION);
        iconContextMenu.addItem(res, R.string.editer, R.drawable.ic_edit_red, MENU_EDITER_ACTION);
        iconContextMenu.addItem(res, R.string.supprimer, R.drawable.ic_delete_red, MENU_SUPPRIMER_ACTION);
        
        //set onclick listener for context menu
        iconContextMenu.setOnClickListener(new IconContextMenu.IconContextMenuOnClickListener() {
			public void onClick(int menuId) {
				long idVin = selectedItem.getLong(selectedItem.getColumnIndex(VinDao.COL_ID));
				
				switch(menuId) {
				
					case MENU_RETIRER_ACTION :
						int currentStock = selectedItem.getInt(selectedItem.getColumnIndex(VinDao.COL_NB_BOUTEILLES));
						if (vinDao.retire1Bouteille(idVin, currentStock) > 0) {
							loadVinsList(currentTab);
							refreshTabTitle();
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
			    				long idVin = selectedItem.getLong(selectedItem.getColumnIndex(VinDao.COL_ID));
			    				vinDao.delete(idVin);
			    				loadVinsList(currentTab);
			    				refreshTabTitle();
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
	
	private void refreshTabTitle() {
		Couleur couleur = Couleur.getFromTabIndex(tabs.getCurrentTab());
		int nb = vinDao.getTotalBouteillesByCouleur(couleur, emptyBottlesOnly, filterPaysId, filterRegionId, filterAppellationId, filterAnneeMaturite);		
		((TextView)tabs.getTabWidget().getChildAt(tabs.getCurrentTab()).findViewById(android.R.id.title)).setText(couleur.getLabel(this.getApplicationContext()).toUpperCase()+" ["+nb+"]\r\n");
	}
}
