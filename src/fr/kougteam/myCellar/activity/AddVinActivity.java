package fr.kougteam.myCellar.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
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
		
		//loadPaysSpinner();	
		loadPaysList();
		
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
				startActivity(intent2Form);
			}	    
		});
	}
//	private void loadPaysSpinner() {
//		Cursor paysCursor = paysDao.getAll();	
//		Spinner paysSpinner = (Spinner)findViewById(R.id.addVinPays);
//		String[] fromPays = new String[] { PaysDao.COL_NOM };
//		int[] toPays = new int[] { android.R.id.text1 };
//		SimpleCursorAdapter paysSpinnerAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, paysCursor, fromPays, toPays);
//		paysSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		paysSpinner.setAdapter(paysSpinnerAdapter);
//		
//		paysSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {		    
//		    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//		        Cursor c = (Cursor)parent.getItemAtPosition(pos);
//		        mPaysId = c.getInt(c.getColumnIndexOrThrow(PaysDao.COL_ID));
//		        
//		        // Rafraichissement des régions
//		        loadRegionSpinner();
//		    }	    
//		    public void onNothingSelected(AdapterView<?> parent) {}
//		});
//	}
//	
//	private void loadRegionSpinner() {
//		Cursor regionCursor = regionDao.getRegionsByPays(mPaysId);	
//		Spinner regionSpinner = (Spinner)findViewById(R.id.addVinRegion);
//		String[] from = new String[] { RegionDao.COL_NOM };
//		int[] to = new int[] { android.R.id.text1 };
//		SimpleCursorAdapter regionSpinnerAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, regionCursor, from, to);
//		regionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		regionSpinner.setAdapter(regionSpinnerAdapter);
//		
//		regionSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {		    
//		    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//		        Cursor c = (Cursor)parent.getItemAtPosition(pos);
//		        mRegionId = c.getInt(c.getColumnIndexOrThrow(RegionDao.COL_ID));
//		        loadAppellationSpinner(mRegionId);
//		        loadSousRegionSpinner();	        
//		    }	    
//		    public void onNothingSelected(AdapterView<?> parent) {}
//		});
//	}
//	
//	private void loadSousRegionSpinner() {
//		Cursor regionCursor = regionDao.getSousRegionsByRegion(mRegionId);	
//		Spinner sousRegionSpinner = (Spinner)findViewById(R.id.addVinSousRegion);
//		String[] from = new String[] { RegionDao.COL_NOM };
//		int[] to = new int[] { android.R.id.text1 };
//		SimpleCursorAdapter sousRegionSpinnerAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, regionCursor, from, to);
//		sousRegionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		sousRegionSpinner.setAdapter(sousRegionSpinnerAdapter);
//		
//		sousRegionSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {		    
//		    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//		        Cursor c = (Cursor)parent.getItemAtPosition(pos);
//		        mSousRegionId = c.getInt(c.getColumnIndexOrThrow(RegionDao.COL_ID));
//		        loadAppellationSpinner(mSousRegionId);
//		    }	    
//		    public void onNothingSelected(AdapterView<?> parent) {}
//		});
//	}
//	
//	private void loadAppellationSpinner(int idRegion) {
//		Cursor appellationCursor = appellationDao.getListByRegion(idRegion);	
//		Spinner appellationSpinner = (Spinner)findViewById(R.id.addVinAppellation);
//		String[] from = new String[] { AppellationDao.COL_NOM };
//		int[] to = new int[] { android.R.id.text1 };
//		SimpleCursorAdapter appellationSpinnerAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, appellationCursor, from, to);
//		appellationSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
//		appellationSpinner.setAdapter(appellationSpinnerAdapter);
//		
//		appellationSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {		    
//		    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
//		        Cursor c = (Cursor)parent.getItemAtPosition(pos);
//		        mAppellationId = c.getInt(c.getColumnIndexOrThrow(AppellationDao.COL_ID));
//		    }	    
//		    public void onNothingSelected(AdapterView<?> parent) {}
//		});
//	}
}
