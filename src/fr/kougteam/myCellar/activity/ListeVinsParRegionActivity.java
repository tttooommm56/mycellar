package fr.kougteam.myCellar.activity;

import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import fr.kougteam.myCellar.R;
import fr.kougteam.myCellar.dao.AppellationDao;
import fr.kougteam.myCellar.dao.PaysDao;
import fr.kougteam.myCellar.dao.RegionDao;
import fr.kougteam.myCellar.dao.VinDao;
import fr.kougteam.myCellar.tools.FontTools;

public class ListeVinsParRegionActivity extends Activity {
	private Intent intent2ListeVins;
	private ListView regionsListView;
	private SimpleCursorAdapter regionAdapter;
	private RegionDao regionDao;
	private VinDao vinDao;
	private int mPaysId = -1;
	private int mRegionId = -1;
	private int mSousRegionId = -1;
	private int mAppellationId = -1;
      
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.liste_vins_par_region);
		
		regionsListView = (ListView) findViewById(R.id.vinsParRegionListView);
		intent2ListeVins = new Intent(this, ListeVinsActivity.class);
		
		vinDao = new VinDao(this);
		regionDao = new RegionDao(this);
			
		List<Integer> paysList = vinDao.getDistinctPays(false);
		if (paysList.size()>1) {
			loadPaysList(vinDao.getPaysWithNbBouteilles());
			
		} else if (paysList.size()==1 && paysList.get(0)==1){
			mPaysId = paysList.get(0); // France
			Cursor regionCursor = vinDao.getRegionsWithNbBouteillesByPays(mPaysId);	
			if (regionCursor.getCount()==0) {
				goToListeVins();
			} else {
				loadRegionList(regionCursor);
			}
			
		} else {
			goToListeVins();
		}
		
		final ViewGroup mContainer = (ViewGroup) findViewById(android.R.id.content);
        FontTools.setDefaultAppFont(mContainer, getAssets());
	}
	
	@Override
	protected void onDestroy() {
		vinDao.close();
		regionDao.close();
		super.onDestroy();
	}

    
    @Override
	public boolean dispatchKeyEvent(KeyEvent event) {
    	//Cette méthode est appelée 2 fois : sur le key-down et sur le key-up, donc on skip le 1er appel
        if (event.getAction()!=KeyEvent.ACTION_DOWN)
            return true;
        
		if (event.getKeyCode() == KeyEvent.KEYCODE_BACK) {
			if (mAppellationId != -1) {
				mAppellationId = -1;
				if (regionDao.hasSousRegion(mRegionId)) {
					loadSousRegionList(vinDao.getSousRegionsWithNbBouteillesByRegion(mRegionId));						
				} else {
					loadRegionList(vinDao.getRegionsWithNbBouteillesByPays(mPaysId));
				} 
				return true;
				
			} else if (mSousRegionId != -1) {
				mSousRegionId = -1;
				loadSousRegionList(vinDao.getSousRegionsWithNbBouteillesByRegion(mRegionId));
				return true;
				
			} else if (mRegionId != -1) {
				mRegionId = -1;
				loadRegionList(vinDao.getRegionsWithNbBouteillesByPays(mPaysId));	
				return true;
			} 
			finish();
		}
		
		return super.dispatchKeyEvent(event);
	}
		
	private void loadPaysList(Cursor paysCursor) {
		String[] fromPays = new String[] { PaysDao.COL_NOM, VinDao.COL_NB_BOUTEILLES };
		int[] toPays = new int[] { R.id.listeVinsParRegionItemName, R.id.listeVinsParRegionItemBouteilles };
		regionAdapter = new SimpleCursorAdapter(this, R.layout.liste_vins_par_region_item, paysCursor, fromPays, toPays) {
			@Override
            public View getView(int position, View convertView, ViewGroup parent){
            	ViewGroup view = (ViewGroup) super.getView(position, convertView, parent);
                if(convertView == null) FontTools.setDefaultAppFont(view, getAssets());
                return view;
            }
		};
		regionsListView.setAdapter(regionAdapter);

		regionsListView.setOnItemClickListener(new OnItemClickListener() {		 
		    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		        Cursor c = (Cursor)parent.getItemAtPosition(pos);
		        mPaysId = c.getInt(c.getColumnIndexOrThrow(PaysDao.COL_ID));

		        if (mPaysId == 1) {
		        	// Affichage des régions si Pays = France
		    		setTitle(getResources().getString(R.string.pays) + " : " + c.getString(c.getColumnIndexOrThrow(PaysDao.COL_NOM)));
		    		Cursor regionCursor = vinDao.getRegionsWithNbBouteillesByPays(mPaysId);	
		    		if (regionCursor.getCount()==0) {
		    			goToListeVins();
		    		} else {
		    			loadRegionList(regionCursor);
		    		}
		        	
		        } else {
		        	goToListeVins();
		        }
		    }	    
		});
	}
	
	private void loadRegionList(Cursor regionCursor) {
		String[] from = new String[] { RegionDao.COL_NOM, VinDao.COL_NB_BOUTEILLES };
		int[] to = new int[] { R.id.listeVinsParRegionItemName, R.id.listeVinsParRegionItemBouteilles };
		regionAdapter = new SimpleCursorAdapter(this, R.layout.liste_vins_par_region_item, regionCursor, from, to) {
			@Override
            public View getView(int position, View convertView, ViewGroup parent){
            	ViewGroup view = (ViewGroup) super.getView(position, convertView, parent);
                if(convertView == null) FontTools.setDefaultAppFont(view, getAssets());
                return view;
            }
		};
		regionAdapter.notifyDataSetChanged();
		regionsListView.setAdapter(regionAdapter);
		regionsListView.invalidateViews();
		
		regionsListView.setOnItemClickListener(new OnItemClickListener() {		 
		    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				Cursor c = (Cursor)parent.getItemAtPosition(pos);
				mRegionId = c.getInt(c.getColumnIndexOrThrow(RegionDao.COL_ID));
				
				if (mRegionId==-1) {
					goToListeVins();
				} else {
					setTitle(getResources().getString(R.string.region) + " : " + c.getString(c.getColumnIndexOrThrow(RegionDao.COL_NOM)));
					if (regionDao.hasSousRegion(mRegionId)) {
						Cursor regionCursor = vinDao.getSousRegionsWithNbBouteillesByRegion(mRegionId);	
						if (regionCursor.getCount()==0) {
							goToListeVins();
						} else {
							loadSousRegionList(regionCursor);	
						}
					} else {
						loadAppellationList(mRegionId);
					}        
				}
			}	    
		});
	}
	
	private void loadSousRegionList(Cursor regionCursor) {
		String[] from = new String[] { RegionDao.COL_NOM, VinDao.COL_NB_BOUTEILLES };
		int[] to = new int[] { R.id.listeVinsParRegionItemName, R.id.listeVinsParRegionItemBouteilles };
		regionAdapter = new SimpleCursorAdapter(this, R.layout.liste_vins_par_region_item, regionCursor, from, to) {
			@Override
            public View getView(int position, View convertView, ViewGroup parent){
            	ViewGroup view = (ViewGroup) super.getView(position, convertView, parent);
                if(convertView == null) FontTools.setDefaultAppFont(view, getAssets());
                return view;
            }
		};
		regionAdapter.notifyDataSetChanged();
		regionsListView.setAdapter(regionAdapter);
		regionsListView.invalidateViews();

		regionsListView.setOnItemClickListener(new OnItemClickListener() {		 
		    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				Cursor c = (Cursor)parent.getItemAtPosition(pos);
				mSousRegionId = c.getInt(c.getColumnIndexOrThrow(RegionDao.COL_ID));
				if (mSousRegionId==-1) {
					goToListeVins();
				} else {
					setTitle(getResources().getString(R.string.territoire) + " : " + c.getString(c.getColumnIndexOrThrow(RegionDao.COL_NOM)));
					loadAppellationList(mSousRegionId);
				}
			}	    
		});
	}
	
	private void loadAppellationList(int idRegion) {
//		setTitle(R.string.title_activity_add_vin_appellation_choix);
		Cursor appellationCursor = vinDao.getAppellationsWithNbBouteillesByRegion(idRegion);	
		String[] from = new String[] { RegionDao.COL_NOM, VinDao.COL_NB_BOUTEILLES };
		int[] to = new int[] { R.id.listeVinsParRegionItemName, R.id.listeVinsParRegionItemBouteilles };
		regionAdapter = new SimpleCursorAdapter(this, R.layout.liste_vins_par_region_item, appellationCursor, from, to) {
			@Override
            public View getView(int position, View convertView, ViewGroup parent){
            	ViewGroup view = (ViewGroup) super.getView(position, convertView, parent);
                if(convertView == null) FontTools.setDefaultAppFont(view, getAssets());
                return view;
            }
		};
		regionAdapter.notifyDataSetChanged();
		regionsListView.setAdapter(regionAdapter);
		regionsListView.invalidateViews();

		regionsListView.setOnItemClickListener(new OnItemClickListener() {		 
		    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				Cursor c = (Cursor)parent.getItemAtPosition(pos);
				mAppellationId = c.getInt(c.getColumnIndexOrThrow(AppellationDao.COL_ID));

				goToListeVins();
			}	    
		});
	}
	
	/**
	 * Redirection vers le formulaire
	 */
	private void goToListeVins() {
		intent2ListeVins.putExtra("appellationId", mAppellationId);
		intent2ListeVins.putExtra("sousRegionId", mSousRegionId);
		intent2ListeVins.putExtra("regionId", mRegionId);
		intent2ListeVins.putExtra("paysId", mPaysId);
		startActivity(intent2ListeVins);
	}
}
