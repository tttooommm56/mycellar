package fr.kougteam.myCellar.activity;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FilterQueryProvider;
import android.widget.RadioButton;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.CursorToStringConverter;
import android.widget.TextView;
import android.widget.Toast;
import fr.kougteam.myCellar.R;
import fr.kougteam.myCellar.dao.AppellationDao;
import fr.kougteam.myCellar.dao.PaysDao;
import fr.kougteam.myCellar.dao.RegionDao;
import fr.kougteam.myCellar.dao.VinDao;
import fr.kougteam.myCellar.enums.Couleur;
import fr.kougteam.myCellar.modele.Vin;
import fr.kougteam.myCellar.ui.NumberPicker;

public class EditVinFormActivity extends Activity {
	private PaysDao paysDao;
	private RegionDao regionDao;
	private AppellationDao appellationDao;
	private VinDao vinDao;
	private Vin vin;
	private int mVinId;
	private RadioButton rougeButton;
	private RadioButton blancButton;
	private RadioButton roseButton;
	private AutoCompleteTextView nomInput;
	private AutoCompleteTextView producteurInput;
	private NumberPicker anneePicker;
	private NumberPicker nbBouteillesPicker;

	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.edit_vin_form);

		rougeButton = (RadioButton)findViewById(R.id.editVinFormRouge);
		blancButton = (RadioButton)findViewById(R.id.editVinFormBlanc);
		roseButton = (RadioButton)findViewById(R.id.editVinFormRose);
		
		initAutocompleteNom();
		initAutotcompleteProducteur();

		anneePicker = (NumberPicker)findViewById(R.id.editVinFormAnnee);
		nbBouteillesPicker = (NumberPicker)findViewById(R.id.editVinFormBouteilles);
		
		paysDao = new PaysDao(this);	
		regionDao = new RegionDao(this);
		appellationDao = new AppellationDao(this);
		vinDao = new VinDao(this);

		Bundle extra = this.getIntent().getExtras(); 
		if (extra!=null) {
			mVinId =  extra.getInt("idVin");
			vin = vinDao.getById(mVinId);
			fillFields();
		} else {
			vin = new Vin();
		}
		
		initCancelButton();
		initSaveButton();		
	}
	
	@Override
	protected void onStop() {
	    setResult(RESULT_CANCELED);
	    super.onStop();
	}
	
	@Override
	protected void onDestroy() {
		paysDao.close();
		regionDao.close();
		appellationDao.close();
		vinDao.close();
		setResult(RESULT_CANCELED);
		super.onDestroy();	
	}

	private void fillFields() {
		// Region
		String region = paysDao.getById(vin.getIdPays()).getNom();
		region += " / " + regionDao.getById(vin.getIdRegion()).getNom();
		TextView regionText = (TextView)findViewById(R.id.editVinFormRegion);
		regionText.setText(region);

		// Appellation
		TextView appellationText = (TextView)findViewById(R.id.editVinFormAppellation);
		appellationText.setText(appellationDao.getById(vin.getIdAppellation()).getNom());
		
		// Couleur
		switch (vin.getCouleur()) {
			case ROUGE : rougeButton.setChecked(true); break;
			case ROSE : roseButton.setChecked(true); break;
			case BLANC : blancButton.setChecked(true); break;
		}
		
		// Nom
		nomInput.setText(vin.getNom());
		
		// Producteur
		producteurInput.setText(vin.getProducteur());
		
		// Année
		anneePicker.setValue(vin.getAnnee());
		
		// Nb bouteilles
		nbBouteillesPicker.setValue(vin.getNbBouteilles());
	}

	private void initAutocompleteNom() {
		nomInput = (AutoCompleteTextView)findViewById(R.id.editVinFormNom);
		String[] fromNom = new String[] { VinDao.COL_NOM };
		int[] to = new int[] { android.R.id.text1 };
		SimpleCursorAdapter nomsAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_dropdown_item_1line, null, fromNom, to);
		nomInput.setAdapter(nomsAdapter);

		nomsAdapter.setCursorToStringConverter(new CursorToStringConverter() {
			public String convertToString(android.database.Cursor cursor) {
				final int columnIndex = cursor.getColumnIndexOrThrow(VinDao.COL_NOM);
				return cursor.getString(columnIndex);
			}
		});

		nomsAdapter.setFilterQueryProvider(new FilterQueryProvider() {
			public Cursor runQuery(CharSequence constraint) {
				return vinDao.getMatchingsNoms((constraint != null ? constraint.toString() : ""));
			}
		});
	}

	private void initAutotcompleteProducteur() {
		producteurInput = (AutoCompleteTextView)findViewById(R.id.editVinFormProducteur);
		String[] fromProducteur = new String[] { VinDao.COL_PRODUCTEUR };
		int[] to = new int[] { android.R.id.text1 };
		SimpleCursorAdapter producteurAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_dropdown_item_1line, null, fromProducteur, to);
		producteurInput.setAdapter(producteurAdapter);

		producteurAdapter.setCursorToStringConverter(new CursorToStringConverter() {
			public String convertToString(android.database.Cursor cursor) {
				final int columnIndex = cursor.getColumnIndexOrThrow(VinDao.COL_PRODUCTEUR);
				return cursor.getString(columnIndex);
			}
		});

		producteurAdapter.setFilterQueryProvider(new FilterQueryProvider() {
			public Cursor runQuery(CharSequence constraint) {
				return vinDao.getMatchingsProducteurs((constraint != null ? constraint.toString() : ""));
			}
		});
	}
	
	private void initCancelButton() {
		Button cancelBtn = (Button)findViewById(R.id.editVinFormCancel);
		cancelBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				EditVinFormActivity.this.finish();
			}
		});
	}
	
	private void initSaveButton() {
		Button saveBtn = (Button)findViewById(R.id.editVinFormSave);
		saveBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
//				vin.setIdAppellation(mAppellationId);
//				vin.setIdRegion(mRegionId);
//				vin.setIdPays(mPaysId);
				vin.setNom(nomInput.getText().toString());
				vin.setProducteur(producteurInput.getText().toString());
				vin.setAnnee(anneePicker.getValue());
				vin.setNbBouteilles(nbBouteillesPicker.getValue());
				Couleur couleur = null;
				if (rougeButton.isChecked()) {
					couleur = Couleur.ROUGE;
				} else if (blancButton.isChecked()) {
					couleur = Couleur.BLANC;
				} else if (roseButton.isChecked()) {
					couleur = Couleur.ROSE;
				}
				vin.setCouleur(couleur);

				if (vinDao.update(vin) != -1) {
					Toast.makeText(getApplicationContext(), R.string.save_ok, Toast.LENGTH_SHORT).show();
					EditVinFormActivity.this.finish();
				} else {
					Toast.makeText(getApplicationContext(), R.string.save_error, Toast.LENGTH_SHORT).show();
				}
				vinDao.close();
			}
		});
	}
}
