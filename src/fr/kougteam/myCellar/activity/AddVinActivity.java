package fr.kougteam.myCellar.activity;

import android.app.Activity;
import android.os.Bundle;
import fr.kougteam.myCellar.R;
import fr.kougteam.myCellar.dao.PaysDao;

public class AddVinActivity extends Activity {
	PaysDao paysDao;
	
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_vin);
		paysDao = new PaysDao(this);
		paysDao.openForRead();
		
	}
}
