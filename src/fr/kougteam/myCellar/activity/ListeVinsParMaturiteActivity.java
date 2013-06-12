package fr.kougteam.myCellar.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import fr.kougteam.myCellar.R;
import fr.kougteam.myCellar.dao.VinDao;
import fr.kougteam.myCellar.tools.FontTools;

public class ListeVinsParMaturiteActivity extends Activity {
	private Intent intent2ListeVins;
	private ListView anneesListView;
	private SimpleCursorAdapter anneeAdapter;
	private VinDao vinDao;
	private int mAnnee = -1;
      
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.liste_vins_par_region);
		
		anneesListView = (ListView) findViewById(R.id.vinsParRegionListView);
		intent2ListeVins = new Intent(this, ListeVinsActivity.class);
		
		vinDao = new VinDao(this);
			
		loadList(vinDao.getAnneeMaturiteWithNbBouteilles());
		
		final ViewGroup mContainer = (ViewGroup) findViewById(android.R.id.content);
        FontTools.setDefaultAppFont(mContainer, getAssets());
	}
	
	@Override
	protected void onDestroy() {
		vinDao.close();
		super.onDestroy();
	}

		
	private void loadList(Cursor cursor) {
		String[] from = new String[] { VinDao.COL_ID, VinDao.COL_NB_BOUTEILLES };
		int[] to = new int[] { R.id.listeVinsParRegionItemName, R.id.listeVinsParRegionItemBouteilles };
		anneeAdapter = new SimpleCursorAdapter(this, R.layout.liste_vins_par_region_item, cursor, from, to) {
			@Override
            public View getView(int position, View convertView, ViewGroup parent){
            	ViewGroup view = (ViewGroup) super.getView(position, convertView, parent);
                if(convertView == null) FontTools.setDefaultAppFont(view, getAssets());
                return view;
            }
		};
		anneesListView.setAdapter(anneeAdapter);

		anneesListView.setOnItemClickListener(new OnItemClickListener() {		 
		    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		        Cursor c = (Cursor)parent.getItemAtPosition(pos);
		        mAnnee = c.getInt(c.getColumnIndexOrThrow(VinDao.COL_ID));
		        goToListeVins();
		    }	    
		});
	}
	
	
	/**
	 * Redirection vers le formulaire
	 */
	private void goToListeVins() {
		intent2ListeVins.putExtra("anneeMaturite", mAnnee);
		startActivity(intent2ListeVins);
	}
}
