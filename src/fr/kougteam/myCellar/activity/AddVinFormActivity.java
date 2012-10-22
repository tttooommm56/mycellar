package fr.kougteam.myCellar.activity;

import android.app.Activity;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.RadioButton;
import android.widget.RatingBar;
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

public class AddVinFormActivity extends Activity {
	private PaysDao paysDao;
	private RegionDao regionDao;
	private AppellationDao appellationDao;
	private VinDao vinDao;
	private int mPaysId;
	private int mRegionId;
	private int mSousRegionId;
	private int mAppellationId;
	private RadioButton rougeButton;
	private RadioButton blancButton;
	private RadioButton roseButton;
	private AutoCompleteTextView nomInput;
	private AutoCompleteTextView producteurInput;

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
		vinDao = new VinDao(this);

		Bundle extra = this.getIntent().getExtras(); 
		if (extra!=null) {
			mAppellationId =  extra.getInt("appellationId");
			mSousRegionId =  extra.getInt("sousRegionId");
			mRegionId =  extra.getInt("regionId");
			mPaysId =  extra.getInt("paysId");

			fillRegionAppellationFields();
		} 
		
		rougeButton = (RadioButton)findViewById(R.id.addVinFormRouge);
		blancButton = (RadioButton)findViewById(R.id.addVinFormBlanc);
		roseButton = (RadioButton)findViewById(R.id.addVinFormRose);
		
		initAutocompleteNom();
		initAutotcompleteProducteur();
		
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

	private void fillRegionAppellationFields() {
		String region = paysDao.getById(mPaysId).getNom();
		region += " / " + regionDao.getById(mRegionId).getNom();
		TextView regionText = (TextView)findViewById(R.id.addVinFormRegion);
		regionText.setText(region);

		TextView appellationText = (TextView)findViewById(R.id.addVinFormAppellation);
		appellationText.setText(appellationDao.getById(mAppellationId).getNom());
	}

	private void initAutocompleteNom() {
		nomInput = (AutoCompleteTextView)findViewById(R.id.addVinFormNom);
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
		producteurInput = (AutoCompleteTextView)findViewById(R.id.addVinFormProducteur);
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
		Button cancelBtn = (Button)findViewById(R.id.addVinFormCancel);
		cancelBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				AddVinFormActivity.this.finish();
			}
		});
	}
	
	private void initSaveButton() {
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
				vin.setNote(((RatingBar) findViewById(R.id.addVinFormNote)).getRating());
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
					AddVinFormActivity.this.finish();
				} else {
					Toast.makeText(getApplicationContext(), R.string.save_error, Toast.LENGTH_SHORT).show();
				}
				vinDao.close();
			}
		});
	}
}
