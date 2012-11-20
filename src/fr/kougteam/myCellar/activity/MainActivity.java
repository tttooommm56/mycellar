package fr.kougteam.myCellar.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import fr.kougteam.myCellar.R;
import fr.kougteam.myCellar.enums.CrudActions;

public class MainActivity extends Activity {
	 
		private ListView menuListView;		
	 
	    /** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.main);
	 
	        // Cr�ation des items
	        menuListView = (ListView) findViewById(R.id.mainListView);
	        ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
	 
	        // Item "Ajouter bouteilles"
	        HashMap<String, String> map = new HashMap<String, String>();
	        map.put("action", Integer.toString(CrudActions.ADD.getId()));
	        map.put("titre", "Ajouter des bouteilles");
	        map.put("img", String.valueOf(R.drawable.ic_plus_green));
	        listItem.add(map);
	 
	        // Item "Voir la cave"
	        map = new HashMap<String, String>();
	        map.put("action", Integer.toString(CrudActions.VIEW.getId()));
	        map.put("titre", "Voir la cave");
	        map.put("img", String.valueOf(R.drawable.ic_loupe_blue));
	        listItem.add(map);
	        
	        // Item "Bazar Baptiste"
	        map = new HashMap<String, String>();
	        map.put("action", Integer.toString(CrudActions.VOCAL.getId()));
	        map.put("titre", "Reconnaissance vocal");
	        map.put("img", String.valueOf(R.drawable.ic_retirer_green));
	        listItem.add(map);
	        
	 
	        SimpleAdapter menuAdapter = new SimpleAdapter (this.getBaseContext(), listItem, R.layout.main_item,
	               new String[] {"img", "titre", "action"}, new int[] {R.id.mainItemImg, R.id.mainItemTitre, R.id.mainItemAction});

	        menuListView.setAdapter(menuAdapter);
	        menuListView.setOnItemClickListener(new OnItemClickListener() {
				
	        	@SuppressWarnings("unchecked")
	         	public void onItemClick(AdapterView<?> a, View v, int position, long id) {
					// R�cup�ration de l'item s�lectionn�
	        		HashMap<String, String> map = (HashMap<String, String>) menuListView.getItemAtPosition(position);
	        		String action = map.get("action");
	        		Intent intent = new Intent();
	        		switch (CrudActions.getFromId(action)) {
	        			case ADD : 
	        				intent.setClass(MainActivity.this.getBaseContext(), AddVinActivity.class);
	        				startActivity(intent);
	        				break;
	        			case VIEW : 
	        				intent.setClass(MainActivity.this.getBaseContext(), ListeVinsActivity.class);
	        				startActivity(intent);
	        				break;
	        			case VOCAL : 
	        				intent.setClass(MainActivity.this.getBaseContext(), VocalActivity.class);
	        				startActivity(intent);
	        				break;
	        			default : 
	        				//on cr�er une boite de dialogue
	    	        		AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
	    	        		//on attribut un titre � notre boite de dialogue
	    	        		adb.setTitle("Erreur : l'item s�lectionn� n'a pas �t� reconnu !");
	    	        		//on ins�re un message � notre boite de dialogue, et ici on affiche le titre de l'item cliqu�
	    	        		adb.setMessage("Code item : "+action);
	    	        		//on indique que l'on veut le bouton ok � notre boite de dialogue
	    	        		adb.setPositiveButton("Ok", null);
	    	        		//on affiche la boite de dialogue
	    	        		adb.show();
	        		}
	        	}
	         });
	 
	    }
	    
	    @Override
		public boolean onKeyDown(int keyCode, KeyEvent event) {
	    	if (keyCode == KeyEvent.KEYCODE_BACK) {
	    		AlertDialog.Builder builder = new AlertDialog.Builder(this);
	    		builder.setMessage(R.string.app_quit_msg)
	    		.setCancelable(false)
	    		.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
	    			public void onClick(DialogInterface dialog, int id) {
	    				MainActivity.this.finish();
	    			}
	    		})
	    		.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
	    			public void onClick(DialogInterface dialog, int id) {
	    			}
	    		});
	    		AlertDialog alert = builder.create();
	    		alert.show();
	    		return true;
	    		
	    	} else {
	    		return super.onKeyDown(keyCode, event);
	    	}
	    }
	}
