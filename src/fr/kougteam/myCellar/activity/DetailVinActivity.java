package fr.kougteam.myCellar.activity;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;
import fr.kougteam.myCellar.R;
import fr.kougteam.myCellar.dao.AppellationDao;
import fr.kougteam.myCellar.dao.PaysDao;
import fr.kougteam.myCellar.dao.RegionDao;
import fr.kougteam.myCellar.dao.VinDao;
import fr.kougteam.myCellar.modele.Vin;

public class DetailVinActivity extends Activity {
	private PaysDao paysDao;
	private RegionDao regionDao;
	private AppellationDao appellationDao;
	private VinDao vinDao;
	
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_vin);
		
		paysDao = new PaysDao(this);	
		regionDao = new RegionDao(this);
		appellationDao = new AppellationDao(this);
		vinDao = new VinDao(this);
		
		Bundle extra = this.getIntent().getExtras(); 
		if (extra!=null) {
			Vin vin = vinDao.getById(extra.getInt("idVin"));
			String region = paysDao.getById(vin.getIdPays()).getNom();
			region += " / " + regionDao.getById(vin.getIdRegion()).getNom();
			TextView regionText = (TextView)findViewById(R.id.detailVinRegion);
			regionText.setText(region);

			((TextView)findViewById(R.id.detailVinAppellation)).setText(appellationDao.getById(vin.getIdAppellation()).getNom());
			((TextView)findViewById(R.id.detailVinCouleur)).setText(vin.getCouleur().getLabel());
			((TextView)findViewById(R.id.detailVinNom)).setText(vin.getNom());
			((TextView)findViewById(R.id.detailVinProducteur)).setText(vin.getProducteur());
			((TextView)findViewById(R.id.detailVinAnnee)).setText(Integer.toString(vin.getAnnee()));
			((TextView)findViewById(R.id.detailVinBouteilles)).setText(Integer.toString(vin.getNbBouteilles()));
		} 
	}
	
	@Override
	protected void onDestroy() {
		paysDao.close();
		regionDao.close();
		appellationDao.close();
		vinDao.close();
		super.onDestroy();	
	}
}
