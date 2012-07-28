package fr.kougteam.myCellar.activity;

import java.util.ArrayList;
import java.util.HashMap;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import fr.kougteam.myCellar.R;

public class MainActivity extends Activity {
	 
		private ListView menuListView;
	 
	    /** Called when the activity is first created. */
	    @Override
	    public void onCreate(Bundle savedInstanceState) {
	        super.onCreate(savedInstanceState);
	        setContentView(R.layout.main);
	 
	        //Récupération de la listview créée dans le fichier main.xml
	        menuListView = (ListView) findViewById(R.id.mainListView);
	 
	        //Création de la ArrayList qui nous permettra de remplire la listView
	        ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
	 
	        HashMap<String, String> map = new HashMap<String, String>();
	        map.put("titre", "Ajouter des bouteilles");
	        map.put("img", String.valueOf(R.drawable.ic_menu_add));
	        listItem.add(map);
	 
	        map = new HashMap<String, String>();
	        map.put("titre", "Voir la cave");
	        map.put("img", String.valueOf(R.drawable.ic_menu_view));
	        listItem.add(map);
	 
	        //Création d'un SimpleAdapter qui se chargera de mettre les items présent dans notre list (listItem) dans la vue affichageitem
	        SimpleAdapter menuAdapter = new SimpleAdapter (this.getBaseContext(), listItem, R.layout.main_item,
	               new String[] {"img", "titre"}, new int[] {R.id.mainItemImg, R.id.mainItemTitre});
	 
	        //On attribut à notre listView l'adapter que l'on vient de créer
	        menuListView.setAdapter(menuAdapter);
	 
	        //Enfin on met un écouteur d'évènement sur notre listView
	        menuListView.setOnItemClickListener(new OnItemClickListener() {
				
	        	@SuppressWarnings("unchecked")
	         	public void onItemClick(AdapterView<?> a, View v, int position, long id) {
					//on récupère la HashMap contenant les infos de notre item (titre, description, img)
	        		HashMap<String, String> map = (HashMap<String, String>) menuListView.getItemAtPosition(position);
	        		//on créer une boite de dialogue
	        		AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
	        		//on attribut un titre à notre boite de dialogue
	        		adb.setTitle("Sélection Item");
	        		//on insère un message à notre boite de dialogue, et ici on affiche le titre de l'item cliqué
	        		adb.setMessage("Votre choix : "+map.get("titre"));
	        		//on indique que l'on veut le bouton ok à notre boite de dialogue
	        		adb.setPositiveButton("Ok", null);
	        		//on affiche la boite de dialogue
	        		adb.show();
	        	}
	         });
	 
	    }
	}
