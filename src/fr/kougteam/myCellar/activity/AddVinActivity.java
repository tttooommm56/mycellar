package fr.kougteam.myCellar.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import fr.kougteam.myCellar.R;
import fr.kougteam.myCellar.dao.AppellationDao;
import fr.kougteam.myCellar.dao.PaysDao;
import fr.kougteam.myCellar.dao.RegionDao;

public class AddVinActivity extends Activity {
	Intent intent2Form;
	private ListView regionsListView;
	private SimpleCursorAdapter regionAdapter;
	private PaysDao paysDao;
	private RegionDao regionDao;
	private AppellationDao appellationDao;
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
		setContentView(R.layout.add_vin);
		regionsListView = (ListView) findViewById(R.id.addVinListView);
		intent2Form = new Intent(this, AddVinFormActivity.class);
		
		paysDao = new PaysDao(this);	
		regionDao = new RegionDao(this);
		appellationDao = new AppellationDao(this);
			
		//loadPaysList(); //TODO uniquement si option international activée dans les paramètres
		mPaysId = 1; // France par défaut
		loadRegionList();
	}
	
	@Override
	protected void onDestroy() {
		paysDao.close();
		regionDao.close();
		appellationDao.close();
		super.onDestroy();
	}
	
	private void loadPaysList() {
		Cursor paysCursor = paysDao.getAll();	
		String[] fromPays = new String[] { PaysDao.COL_NOM };
		int[] toPays = new int[] { R.id.addVinItemText };
		regionAdapter = new SimpleCursorAdapter(this, R.layout.add_vin_item, paysCursor, fromPays, toPays);
		regionsListView.setAdapter(regionAdapter);

		regionsListView.setOnItemClickListener(new OnItemClickListener() {		 
		    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
		        Cursor c = (Cursor)parent.getItemAtPosition(pos);
		        mPaysId = c.getInt(c.getColumnIndexOrThrow(PaysDao.COL_ID));
		        
		        // Affichage des régions
		        loadRegionList();
		    }	    
		});
	}
	
	private void loadRegionList() {
		Cursor regionCursor = regionDao.getRegionsByPays(mPaysId);	
		String[] from = new String[] { RegionDao.COL_NOM };
		int[] to = new int[] { R.id.addVinItemText };
		regionAdapter = new SimpleCursorAdapter(this, R.layout.add_vin_item, regionCursor, from, to);
		regionAdapter.notifyDataSetChanged();
		regionsListView.setAdapter(regionAdapter);
		regionsListView.invalidateViews();
		
		regionsListView.setOnItemClickListener(new OnItemClickListener() {		 
		    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				Cursor c = (Cursor)parent.getItemAtPosition(pos);
				mRegionId = c.getInt(c.getColumnIndexOrThrow(RegionDao.COL_ID));
				
				if (regionDao.hasSousRegion(mRegionId)) {
					loadSousRegionList();	 
				} else {
					loadAppellationList(mRegionId);
				}        
			}	    
		});
	}
	
	private void loadSousRegionList() {
		Cursor regionCursor = regionDao.getSousRegionsByRegion(mRegionId);	
		String[] from = new String[] { RegionDao.COL_NOM };
		int[] to = new int[] { R.id.addVinItemText };
		regionAdapter = new SimpleCursorAdapter(this, R.layout.add_vin_item, regionCursor, from, to);
		regionAdapter.notifyDataSetChanged();
		regionsListView.setAdapter(regionAdapter);
		regionsListView.invalidateViews();

		regionsListView.setOnItemClickListener(new OnItemClickListener() {		 
		    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				Cursor c = (Cursor)parent.getItemAtPosition(pos);
				mSousRegionId = c.getInt(c.getColumnIndexOrThrow(RegionDao.COL_ID));
				loadAppellationList(mSousRegionId);
			}	    
		});
	}
	
	private void loadAppellationList(int idRegion) {
		Cursor appellationCursor = appellationDao.getListByRegion(idRegion);	
		String[] from = new String[] { AppellationDao.COL_NOM };
		int[] to = new int[] { R.id.addVinItemText };
		regionAdapter = new SimpleCursorAdapter(this, R.layout.add_vin_item, appellationCursor, from, to);
		regionAdapter.notifyDataSetChanged();
		regionsListView.setAdapter(regionAdapter);
		regionsListView.invalidateViews();

		regionsListView.setOnItemClickListener(new OnItemClickListener() {		 
		    public void onItemClick(AdapterView<?> parent, View view, int pos, long id) {
				Cursor c = (Cursor)parent.getItemAtPosition(pos);
				mAppellationId = c.getInt(c.getColumnIndexOrThrow(AppellationDao.COL_ID));

				// Redirection vers le formulaire
				intent2Form.putExtra("appellationId", mAppellationId);
				intent2Form.putExtra("sousRegionId", mSousRegionId);
				intent2Form.putExtra("regionId", mRegionId);
				intent2Form.putExtra("paysId", mPaysId);
				startActivityForResult(intent2Form, 1);
			}	    
		});
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		if (keyCode == KeyEvent.KEYCODE_BACK) {
			if (mAppellationId != -1) {
				mAppellationId = -1;
				if (regionDao.hasSousRegion(mRegionId)) {
					loadSousRegionList();						
				} else {
					loadRegionList();
				} 
				
			} else if (mSousRegionId != -1) {
				mSousRegionId = -1;
				loadSousRegionList();
				
			} else if (mRegionId != -1) {
				mRegionId = -1;
				loadRegionList();
				
			} else {
				return super.onKeyDown(keyCode, event);
			}
		}
		return true;
	}
	
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (resultCode == RESULT_CANCELED) {
	        finish();
	    }
	}
}
