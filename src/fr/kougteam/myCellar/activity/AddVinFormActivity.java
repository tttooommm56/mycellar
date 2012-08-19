package fr.kougteam.myCellar.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import fr.kougteam.myCellar.R;
import fr.kougteam.myCellar.dao.AppellationDao;
import fr.kougteam.myCellar.dao.PaysDao;
import fr.kougteam.myCellar.dao.RegionDao;
import fr.kougteam.myCellar.dao.VinDao;
import fr.kougteam.myCellar.enums.Couleur;
import fr.kougteam.myCellar.modele.Vin;
import fr.kougteam.myCellar.widget.NumberPicker;

public class AddVinFormActivity extends Activity {
	private PaysDao paysDao;
	private RegionDao regionDao;
	private AppellationDao appellationDao;
	private VinDao vinDao;
	private int mPaysId;
	private int mRegionId;
	private int mSousRegionId;
	private int mAppellationId;
	private Intent intentBack;
	private ToggleButton rougeButton;
	private ToggleButton blancButton;
    private ToggleButton roseButton;
    
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.add_vin_form);
		
		intentBack = new Intent(this, MainActivity.class);
		
		paysDao = new PaysDao(this);	
		regionDao = new RegionDao(this);
		appellationDao = new AppellationDao(this);
		vinDao = new VinDao(this);
		
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
        
        

        rougeButton = (ToggleButton)findViewById(R.id.addVinFormRouge);
        blancButton = (ToggleButton)findViewById(R.id.addVinFormBlanc);
        roseButton = (ToggleButton)findViewById(R.id.addVinFormRose);
        
        rougeButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton button, boolean isChecked) {
				if (isChecked) {
					// On déselectionne les autres boutons
					blancButton.setChecked(false);
					roseButton.setChecked(false);
				}		
			}   	
        });
        
        blancButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton button, boolean isChecked) {
				if (isChecked) {
					// On déselectionne les autres boutons
					rougeButton.setChecked(false);
					roseButton.setChecked(false);
				}		
			}   	
        });
        
        roseButton.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton button, boolean isChecked) {
				if (isChecked) {
					// On déselectionne les autres boutons
					blancButton.setChecked(false);
					rougeButton.setChecked(false);
				}		
			}   	
        });
        
        Button cancelBtn = (Button)findViewById(R.id.addVinFormCancel);
        cancelBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				startActivity(intentBack);
			}
		});
        
        Button saveBtn = (Button)findViewById(R.id.addVinFormSave);
        saveBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Vin vin = new Vin();
				vin.setIdAppellation(mAppellationId);
				vin.setIdRegion(mRegionId);
				vin.setIdPays(mPaysId);
				vin.setNom(((EditText)findViewById(R.id.addVinFormNom)).getText().toString());
				vin.setProducteur(((EditText)findViewById(R.id.addVinFormProducteur)).getText().toString());
				vin.setAnnee(((NumberPicker)findViewById(R.id.addVinFormAnnee)).getValue());
				vin.setNbBouteilles(((NumberPicker)findViewById(R.id.addVinFormBouteilles)).getValue());
				Couleur couleur = null;
				if (rougeButton.isChecked()) {
					couleur = Couleur.ROUGE;
				} else if (blancButton.isChecked()) {
					couleur = Couleur.BLANC;
				} else if (roseButton.isChecked()) {
					couleur = Couleur.ROSE;
				}
				vin.setCouleur(couleur);
				
				if (vinDao.insert(vin) != -1) {
					Toast.makeText(getApplicationContext(), R.string.save_ok, Toast.LENGTH_SHORT).show();
					startActivity(intentBack);
				} else {
					Toast.makeText(getApplicationContext(), R.string.save_error, Toast.LENGTH_SHORT).show();
				}
			}
		});
	}
}
