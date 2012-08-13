package fr.kougteam.myCellar.activity;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.SimpleCursorAdapter;
import android.widget.Spinner;
import fr.kougteam.myCellar.R;
import fr.kougteam.myCellar.dao.AppellationDao;
import fr.kougteam.myCellar.dao.PaysDao;
import fr.kougteam.myCellar.dao.RegionDao;

public class AddVinActivity extends Activity {
	PaysDao paysDao;
	RegionDao regionDao;
	AppellationDao appellationDao;
	private int mPaysSpinnerId;
	private int mRegionSpinnerId;
	private int mSousRegionSpinnerId;
	private int mAppellationSpinnerId;
	
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_vin);
		
		// Liste déroulante des pays
		paysDao = new PaysDao(this);
		loadPaysSpinner();
		
		regionDao = new RegionDao(this);
		appellationDao = new AppellationDao(this);
		
	}
	
	private void loadPaysSpinner() {
		Cursor paysCursor = paysDao.getAll();	
		Spinner paysSpinner = (Spinner)findViewById(R.id.addVinPays);
		String[] fromPays = new String[] { PaysDao.COL_NOM };
		int[] toPays = new int[] { android.R.id.text1 };
		SimpleCursorAdapter paysSpinnerAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, paysCursor, fromPays, toPays);
		paysSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		paysSpinner.setAdapter(paysSpinnerAdapter);
		
		paysSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {		    
		    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		        Cursor c = (Cursor)parent.getItemAtPosition(pos);
		        mPaysSpinnerId = c.getInt(c.getColumnIndexOrThrow(PaysDao.COL_ID));
		        
		        // Rafraichissement des régions
		        loadRegionSpinner();
		    }	    
		    public void onNothingSelected(AdapterView<?> parent) {}
		});
	}
	
	private void loadRegionSpinner() {
		Cursor regionCursor = regionDao.getRegionsByPays(mPaysSpinnerId);	
		Spinner regionSpinner = (Spinner)findViewById(R.id.addVinRegion);
		String[] from = new String[] { RegionDao.COL_NOM };
		int[] to = new int[] { android.R.id.text1 };
		SimpleCursorAdapter regionSpinnerAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, regionCursor, from, to);
		regionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		regionSpinner.setAdapter(regionSpinnerAdapter);
		
		regionSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {		    
		    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		        Cursor c = (Cursor)parent.getItemAtPosition(pos);
		        mRegionSpinnerId = c.getInt(c.getColumnIndexOrThrow(RegionDao.COL_ID));
		        loadAppellationSpinner(mRegionSpinnerId);
		        loadSousRegionSpinner();	        
		    }	    
		    public void onNothingSelected(AdapterView<?> parent) {}
		});
	}
	
	private void loadSousRegionSpinner() {
		Cursor regionCursor = regionDao.getSousRegionsByRegion(mRegionSpinnerId);	
		Spinner sousRegionSpinner = (Spinner)findViewById(R.id.addVinSousRegion);
		String[] from = new String[] { RegionDao.COL_NOM };
		int[] to = new int[] { android.R.id.text1 };
		SimpleCursorAdapter sousRegionSpinnerAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, regionCursor, from, to);
		sousRegionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		sousRegionSpinner.setAdapter(sousRegionSpinnerAdapter);
		
		sousRegionSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {		    
		    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		        Cursor c = (Cursor)parent.getItemAtPosition(pos);
		        mSousRegionSpinnerId = c.getInt(c.getColumnIndexOrThrow(RegionDao.COL_ID));
		        loadAppellationSpinner(mSousRegionSpinnerId);
		    }	    
		    public void onNothingSelected(AdapterView<?> parent) {}
		});
	}
	
	private void loadAppellationSpinner(int idRegion) {
		Cursor appellationCursor = appellationDao.getListByRegion(idRegion);	
		Spinner appellationSpinner = (Spinner)findViewById(R.id.addVinAppellation);
		String[] from = new String[] { AppellationDao.COL_NOM };
		int[] to = new int[] { android.R.id.text1 };
		SimpleCursorAdapter appellationSpinnerAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, appellationCursor, from, to);
		appellationSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
		appellationSpinner.setAdapter(appellationSpinnerAdapter);
		
		appellationSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {		    
		    public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
		        Cursor c = (Cursor)parent.getItemAtPosition(pos);
		        mAppellationSpinnerId = c.getInt(c.getColumnIndexOrThrow(AppellationDao.COL_ID));
		    }	    
		    public void onNothingSelected(AdapterView<?> parent) {}
		});
	}
}
