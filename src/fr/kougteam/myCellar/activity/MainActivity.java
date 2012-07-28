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
	 
	        //R�cup�ration de la listview cr��e dans le fichier main.xml
	        menuListView = (ListView) findViewById(R.id.mainListView);
	 
	        //Cr�ation de la ArrayList qui nous permettra de remplire la listView
	        ArrayList<HashMap<String, String>> listItem = new ArrayList<HashMap<String, String>>();
	 
	        HashMap<String, String> map = new HashMap<String, String>();
	        map.put("titre", "Ajouter des bouteilles");
	        map.put("img", String.valueOf(R.drawable.ic_menu_add));
	        listItem.add(map);
	 
	        map = new HashMap<String, String>();
	        map.put("titre", "Voir la cave");
	        map.put("img", String.valueOf(R.drawable.ic_menu_view));
	        listItem.add(map);
	 
	        //Cr�ation d'un SimpleAdapter qui se chargera de mettre les items pr�sent dans notre list (listItem) dans la vue affichageitem
	        SimpleAdapter menuAdapter = new SimpleAdapter (this.getBaseContext(), listItem, R.layout.main_item,
	               new String[] {"img", "titre"}, new int[] {R.id.mainItemImg, R.id.mainItemTitre});
	 
	        //On attribut � notre listView l'adapter que l'on vient de cr�er
	        menuListView.setAdapter(menuAdapter);
	 
	        //Enfin on met un �couteur d'�v�nement sur notre listView
	        menuListView.setOnItemClickListener(new OnItemClickListener() {
				
	        	@SuppressWarnings("unchecked")
	         	public void onItemClick(AdapterView<?> a, View v, int position, long id) {
					//on r�cup�re la HashMap contenant les infos de notre item (titre, description, img)
	        		HashMap<String, String> map = (HashMap<String, String>) menuListView.getItemAtPosition(position);
	        		//on cr�er une boite de dialogue
	        		AlertDialog.Builder adb = new AlertDialog.Builder(MainActivity.this);
	        		//on attribut un titre � notre boite de dialogue
	        		adb.setTitle("S�lection Item");
	        		//on ins�re un message � notre boite de dialogue, et ici on affiche le titre de l'item cliqu�
	        		adb.setMessage("Votre choix : "+map.get("titre"));
	        		//on indique que l'on veut le bouton ok � notre boite de dialogue
	        		adb.setPositiveButton("Ok", null);
	        		//on affiche la boite de dialogue
	        		adb.show();
	        	}
	         });
	 
	    }
	}
