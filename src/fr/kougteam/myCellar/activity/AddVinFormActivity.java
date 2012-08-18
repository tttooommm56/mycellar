package fr.kougteam.myCellar.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import fr.kougteam.myCellar.R;
import fr.kougteam.myCellar.dao.AppellationDao;
import fr.kougteam.myCellar.dao.PaysDao;
import fr.kougteam.myCellar.dao.RegionDao;
import fr.kougteam.myCellar.widget.NumberPicker;

public class AddVinFormActivity extends Activity {
	private PaysDao paysDao;
	private RegionDao regionDao;
	private AppellationDao appellationDao;
	private int mPaysId;
	private int mRegionId;
	private int mSousRegionId;
	private int mAppellationId;
	
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_vin_form);
		
		paysDao = new PaysDao(this);	
		regionDao = new RegionDao(this);
		appellationDao = new AppellationDao(this);
		
        Bundle extra = this.getIntent().getExtras(); 
        if (extra!=null) {
        	mAppellationId =  extra.getInt("appellationId");
        	mSousRegionId =  extra.getInt("sousRegionId");
        	mRegionId =  extra.getInt("regionId");
        	mPaysId =  extra.getInt("paysId");
        	
        	String region = paysDao.getById(mPaysId).getNom();
        	region += " / " + regionDao.getById(mRegionId).getNom();
        	TextView regionText = (TextView)findViewById(R.id.addVinFormRegion);
        	regionText.setText(region);
        	
        	TextView appellationText = (TextView)findViewById(R.id.addVinFormAppellation);
        	appellationText.setText(appellationDao.getById(mAppellationId).getNom());
        } 
        

        NumberPicker yearPicker = (NumberPicker)findViewById(R.id.yearPicker);
	}
}
