package fr.kougteam.myCellar.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import fr.kougteam.myCellar.R;
import fr.kougteam.myCellar.dao.VinDao;
import fr.kougteam.myCellar.enums.Couleur;

public class MainActivity extends Activity {
	 
		private ListView menuListView;	
		private VinDao vinDao;
		
	    /** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.main);
	        
	        vinDao = new VinDao(this);
	        
	        // Création des items
	        menuListView = (ListView) findViewById(R.id.mainListView);
	        ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
	 
	        // Item "Voir la cave"
	        HashMap<String, String> map = new HashMap<String, String>();
	        map.put("action", "LIST");
	        map.put("titre", getString(R.string.main_list_vin));
	        map.put("img", String.valueOf(R.drawable.ic_loupe_blue));
	        listItem.add(map);       
	        
	        // Item "Ajouter bouteilles"
	        map = new HashMap<String, String>();
	        map.put("action", "ADD");
	        map.put("titre", getString(R.string.main_add_vin));
	        map.put("img", String.valueOf(R.drawable.ic_plus_green));
	        listItem.add(map);
	         
	        // Item "Envoi liste par mail"
	        map = new HashMap<String, String>();
	        map.put("action", "MAIL");
	        map.put("titre", getString(R.string.main_send_list));
	        map.put("img", String.valueOf(R.drawable.ic_mail_send_yellow));
	        listItem.add(map);
	        
	        // Item "Historique"
	        map = new HashMap<String, String>();
	        map.put("action", "HISTO");
	        map.put("titre", getString(R.string.main_histo_vin));
	        map.put("img", String.valueOf(R.drawable.ic_retirer_red));
	        listItem.add(map);     
	 
	        SimpleAdapter menuAdapter = new SimpleAdapter (this.getBaseContext(), listItem, R.layout.main_item,
	               new String[] {"img", "titre", "action"}, new int[] {R.id.mainItemImg, R.id.mainItemTitre, R.id.mainItemAction});

	        menuListView.setAdapter(menuAdapter);
	        menuListView.setOnItemClickListener(new OnItemClickListener() {
				
	        	@SuppressWarnings("unchecked")
	         	public void onItemClick(AdapterView<?> a, View v, int position, long id) {
					// Récupération de l'item sélectionné
	        		HashMap<String, String> map = (HashMap<String, String>) menuListView.getItemAtPosition(position);
	        		String action = map.get("action");
	        		Intent intent = new Intent();
	        		
	        		if ("ADD".equals(action)) {
        				intent.setClass(MainActivity.this.getBaseContext(), EditVinFormActivity.class);
        				startActivity(intent);
        				
	        		} else if ("LIST".equals(action)) {
        				intent.setClass(MainActivity.this.getBaseContext(), ListeVinsActivity.class);
        				intent.putExtra("emptyBottlesOnly", false);
        				startActivity(intent);
        				
	        		} else if ("MAIL".equals(action)) {
	        			sendListeVinsByMail();
	        			
	        		} else if ("HISTO".equals(action)) {
        				intent.setClass(MainActivity.this.getBaseContext(), ListeVinsActivity.class);
        				intent.putExtra("emptyBottlesOnly", true);
        				startActivity(intent);
	
	        		} else {
        				//on créer une boite de dialogue
    	        		AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
    	        		//on attribut un titre à notre boite de dialogue
    	        		adb.setTitle("Erreur : l'item sélectionné n'a pas été reconnu !");
    	        		//on insère un message à notre boite de dialogue, et ici on affiche le titre de l'item cliqué
    	        		adb.setMessage("Code item : "+action);
    	        		//on indique que l'on veut le bouton ok à notre boite de dialogue
    	        		adb.setPositiveButton("Ok", null);
    	        		//on affiche la boite de dialogue
    	        		adb.show();
	        		}
	        	}
	         });
	 
	    }
	    
	    @Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
	    	// Message de confirmation (TCO : plus nécessaire...)
//	    	if (keyCode == KeyEvent.KEYCODE_BACK) {
//	    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
//	    		builder.setTitle(R.string.app_quit_title)
//	    		.setIcon(android.R.drawable.ic_dialog_alert)
//	    		.setMessage(R.string.app_quit_msg)
//	    		.setCancelable(false)
//	    		.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
//	    			public void onClick(DialogInterface dialog, int id) {
//	    				MainActivity.this.finish();
//	    			}
//	    		})
//	    		.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
//	    			public void onClick(DialogInterface dialog, int id) {
//	    			}
//	    		});
//	    		AlertDialog alert = builder.create();
//	    		alert.show();
//	    		return true;
//	    		
//	    	} else {
	    		return super.onKeyDown(keyCode, event);
//	    	}
	    }
	    
	    private void fillFromCursor(Cursor c, StringBuilder sb, String titreListe) {
	    	if (c.getCount()>0) {
	    		sb.append(titreListe + " :\r\n");
	    		while (c.moveToNext()) {
	    			sb.append("- "+c.getString(c.getColumnIndex("nom_appellation")));
	    			String producteur = c.getString(c.getColumnIndex(VinDao.COL_PRODUCTEUR));
	    			if (producteur!=null && !"".equals(producteur)) {
	    				sb.append(", "+producteur);
	    			}
//	    			int regionId = c.getInt(c.getColumnIndex(VinDao.COL_REGION));
//	    			if (regionId > 0) {
//	    				sb.append(", "+regionDao.getById(regionId).getNom());
//	    			}
	    			sb.append(", "+c.getString(c.getColumnIndex(VinDao.COL_ANNEE))+" : ");
	    			int nbBouteilles = c.getInt(c.getColumnIndex(VinDao.COL_NB_BOUTEILLES));
	    			sb.append(nbBouteilles + " bouteille" + (nbBouteilles>1 ? "s" : "") + "\r\n");
	    		}
	    		sb.append("\r\n");
	    	}
	    	c.close();
	    }
	    
	    private void sendListeVinsByMail() {	
	    	StringBuilder sb = new StringBuilder();
	    	fillFromCursor(vinDao.getListVinsDisposByCouleur(Couleur.ROUGE, false), sb, "LISTE DES VINS ROUGES");
	    	fillFromCursor(vinDao.getListVinsDisposByCouleur(Couleur.ROSE, false), sb, "LISTE DES VINS ROSÉS");
	    	fillFromCursor(vinDao.getListVinsDisposByCouleur(Couleur.BLANC, false), sb, "LISTE DES VINS BLANCS");
	    	
	    	final Intent emailIntent = new Intent(android.content.Intent.ACTION_SEND);

	    	emailIntent.setType("text/plain");
	    	emailIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Mon Cellier : liste des vins disponibles.");
	    	emailIntent.putExtra(android.content.Intent.EXTRA_TEXT, sb.toString());
	    	
	    	this.startActivity(Intent.createChooser(emailIntent, getString(R.string.send)));
	    }
	}
