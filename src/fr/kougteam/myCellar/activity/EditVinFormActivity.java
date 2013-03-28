package fr.kougteam.myCellar.activity;

import java.io.ByteArrayOutputStream;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.CursorToStringConverter;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.Toast;
import fr.kougteam.myCellar.R;
import fr.kougteam.myCellar.dao.AppellationDao;
import fr.kougteam.myCellar.dao.PaysDao;
import fr.kougteam.myCellar.dao.RegionDao;
import fr.kougteam.myCellar.dao.VinDao;
import fr.kougteam.myCellar.enums.Couleur;
import fr.kougteam.myCellar.modele.Appellation;
import fr.kougteam.myCellar.modele.Region;
import fr.kougteam.myCellar.modele.Vin;
import fr.kougteam.myCellar.ui.NumberPicker;

public class EditVinFormActivity extends Activity {
	
	private static final int CAMERA_PIC_REQUEST = 1337;

	private PaysDao paysDao;
	private RegionDao regionDao;
	private AppellationDao appellationDao;
	private VinDao vinDao;
	private Vin vin;
	private int mVinId;
	private RadioButton rougeButton;
	private RadioButton blancButton;
	private RadioButton roseButton;
	private Spinner paysSpinner;
	private Spinner regionSpinner;
	private Spinner territoireSpinner;
	private Spinner appellationSpinner;
	private AutoCompleteTextView nomInput;
	private AutoCompleteTextView producteurInput;
	private NumberPicker anneePicker;
	private NumberPicker nbBouteillesPicker;
	private RatingBar noteRatingBar;
	private TableRow regionTableRow;
	private TableRow territoireTableRow;
	private TableRow appellationTableRow;
	private ImageView etiquetteView;
	
	private int mPaysSpinnerId = -1;
    private int mRegionSpinnerId = -1;
    private int mTerritoireSpinnerId = -1;
    private int mAppellationSpinnerId = -1;

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
		
		paysSpinner = (Spinner)findViewById(R.id.editVinFormPays);
		regionSpinner = (Spinner)findViewById(R.id.editVinFormRegion);
		territoireSpinner = (Spinner)findViewById(R.id.editVinFormTerritoire);
		appellationSpinner = (Spinner)findViewById(R.id.editVinFormAppellation);
		
		initAutocompleteNom();
		initAutotcompleteProducteur();

		anneePicker = (NumberPicker)findViewById(R.id.editVinFormAnnee);
		nbBouteillesPicker = (NumberPicker)findViewById(R.id.editVinFormBouteilles);
		
		noteRatingBar = (RatingBar)findViewById(R.id.editVinFormNote);
		
		regionTableRow = (TableRow)findViewById(R.id.editVinFormRegionRow);
		territoireTableRow = (TableRow)findViewById(R.id.editVinFormTerritoireRow);
		appellationTableRow = (TableRow)findViewById(R.id.editVinFormAppellationRow);
		
		etiquetteView = (ImageView) findViewById(R.id.editVinResultPhoto); 
		
		paysDao = new PaysDao(this);	
		regionDao = new RegionDao(this);
		appellationDao = new AppellationDao(this);
		vinDao = new VinDao(this);

		Bundle extra = this.getIntent().getExtras(); 
		if (extra!=null) {
			setTitle(R.string.title_activity_edit_vin);
			mVinId =  extra.getInt("idVin");
			vin = vinDao.getById(mVinId);
			
		} else {
			setTitle(R.string.title_activity_add_vin);
			vin = new Vin();
			vin.setIdPays(1);   // France par défaut  
			vin.setCouleur(Couleur.ROUGE); // Rouge par défaut
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());		
			vin.setAnnee(cal.get(Calendar.YEAR)-1); // Année précédente par défaut
			vin.setNbBouteilles(1);
		}
		
		fillFields();
		
		loadPaysSpinner();
		
		initCancelButton();
		initSaveButton();	
		initPhotoButton();
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
//		String region = paysDao.getById(vin.getIdPays()).getNom();
//		region += " / " + regionDao.getById(vin.getIdRegion()).getNom();
//		TextView regionText = (TextView)findViewById(R.id.editVinFormRegion);
//		regionText.setText(region);

		// Appellation
//		TextView appellationText = (TextView)findViewById(R.id.editVinFormAppellation);
//		appellationText.setText(appellationDao.getById(vin.getIdAppellation()).getNom());
		
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
		
		// Note
		noteRatingBar.setRating((float)vin.getNote());
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
				return vinDao.getMatchingNoms((constraint != null ? constraint.toString() : ""));
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
				return vinDao.getMatchingProducteurs((constraint != null ? constraint.toString() : ""));
			}
		});
	}
	
	private void loadPaysSpinner() {
        Cursor paysCursor = paysDao.getAll();   
        String[] from = new String[] { PaysDao.COL_NOM };
        int[] to = new int[] { android.R.id.text1 };
        SimpleCursorAdapter paysSpinnerAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, paysCursor, from, to);

        paysSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        paysSpinner.setAdapter(paysSpinnerAdapter);
        
        if (vin.getIdPays()>0) {
        	paysSpinner.setSelection(getSpinnerPostition(paysCursor, PaysDao.COL_ID, vin.getIdPays()));
        }
        
        paysSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {              
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Cursor c = (Cursor)parent.getItemAtPosition(pos);
                mPaysSpinnerId = c.getInt(c.getColumnIndexOrThrow(PaysDao.COL_ID));
                if (mPaysSpinnerId!=1) {
                	// Si le pays est différent de "France", on masque les choix Region, Territoire, Appellation
                	regionTableRow.setVisibility(View.GONE);
                	territoireTableRow.setVisibility(View.GONE);
                	appellationTableRow.setVisibility(View.GONE);
                } else {
                	regionTableRow.setVisibility(View.VISIBLE);
                	territoireTableRow.setVisibility(View.VISIBLE);
                	appellationTableRow.setVisibility(View.VISIBLE);
                	loadRegionSpinner(); 
                }
                            
            }       
            public void onNothingSelected(AdapterView<?> parent) {}
        });
	}
	
	private void loadRegionSpinner() {
        Cursor regionCursor = regionDao.getRegionsByPays(mPaysSpinnerId);   
        String[] from = new String[] { RegionDao.COL_NOM };
        int[] to = new int[] { android.R.id.text1 };
        SimpleCursorAdapter regionSpinnerAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, regionCursor, from, to);

        regionSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        regionSpinner.setAdapter(regionSpinnerAdapter);
        
        if (vin.getIdRegion()>0) {
        	Region r = regionDao.getById(vin.getIdRegion());
        	int idRegion = -1;
        	if (r.isSousRegion()) {
        		idRegion = r.getIdRegionParent();
        	} else {
        		idRegion = r.getId();
        	}
        	regionSpinner.setSelection(getSpinnerPostition(regionCursor, RegionDao.COL_ID, idRegion));
        }
        regionSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {              
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Cursor c = (Cursor)parent.getItemAtPosition(pos);
                mRegionSpinnerId = c.getInt(c.getColumnIndexOrThrow(RegionDao.COL_ID));
                loadAppellationSpinner(mRegionSpinnerId);
                loadTerritoireSpinner();                
            }       
            public void onNothingSelected(AdapterView<?> parent) {}
        });
	}
	
	private void loadTerritoireSpinner() {
		Cursor territoireCursor = regionDao.getSousRegionsByRegion(mRegionSpinnerId);     
        String[] from = new String[] { RegionDao.COL_NOM };
        int[] to = new int[] { android.R.id.text1 };
        SimpleCursorAdapter territoireSpinnerAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, territoireCursor, from, to);
        territoireSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        territoireSpinner.setAdapter(territoireSpinnerAdapter);
        
        if (territoireSpinnerAdapter.getCount()>1) {
        	territoireTableRow.setVisibility(View.VISIBLE);
        	if (vin.getIdAppellation()>0) {
        		Appellation a = appellationDao.getById(vin.getIdAppellation());
        		territoireSpinner.setSelection(getSpinnerPostition(territoireCursor, RegionDao.COL_ID, a.getIdRegion()));
            }       	
        	
        } else {
        	territoireTableRow.setVisibility(View.GONE);
        }
        
        territoireSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {                  
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Cursor c = (Cursor)parent.getItemAtPosition(pos);
                mTerritoireSpinnerId = c.getInt(c.getColumnIndexOrThrow(RegionDao.COL_ID));
                loadAppellationSpinner(mTerritoireSpinnerId);
            }       
            public void onNothingSelected(AdapterView<?> parent) {}
        });
	}
	
	private void loadAppellationSpinner(int idRegion) {
		Cursor appellationCursor = appellationDao.getListByRegion(idRegion);    
        String[] from = new String[] { AppellationDao.COL_NOM };
        int[] to = new int[] { android.R.id.text1 };
        SimpleCursorAdapter appellationSpinnerAdapter = new SimpleCursorAdapter(this, android.R.layout.simple_spinner_item, appellationCursor, from, to);
        appellationSpinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        appellationSpinner.setAdapter(appellationSpinnerAdapter);
        if (vin.getIdAppellation()>0) {
        	appellationSpinner.setSelection(getSpinnerPostition(appellationCursor, AppellationDao.COL_ID, vin.getIdAppellation()));
        }
        appellationSpinner.setOnItemSelectedListener(new OnItemSelectedListener() {                 
            public void onItemSelected(AdapterView<?> parent, View view, int pos, long id) {
                Cursor c = (Cursor)parent.getItemAtPosition(pos);
                mAppellationSpinnerId = c.getInt(c.getColumnIndexOrThrow(AppellationDao.COL_ID));
            }       
            public void onNothingSelected(AdapterView<?> parent) {}
        });
	}

	private int getSpinnerPostition(Cursor c, String idColName, int searchId) {
	    c.moveToFirst(); 
	    for (int i=0; i<c.getCount()-1; i++) {  
	    	c.moveToNext();  
	        int id = c.getInt(c.getColumnIndex(idColName));  
	        if (searchId == id) { 
	            return i+1;  
	        }
	    } 
	    return 0;
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
				vin.setIdPays(mPaysSpinnerId);
				vin.setIdAppellation(mAppellationSpinnerId);
				if (mTerritoireSpinnerId>0) {
					vin.setIdRegion(mTerritoireSpinnerId);
				} else {
					vin.setIdRegion(mRegionSpinnerId);
				}				
				vin.setNom(nomInput.getText().toString());
				vin.setProducteur(producteurInput.getText().toString());
				vin.setAnnee(anneePicker.getValue());
				vin.setNbBouteilles(nbBouteillesPicker.getValue());
				vin.setNote(noteRatingBar.getRating());
				Couleur couleur = null;
				if (rougeButton.isChecked()) {
					couleur = Couleur.ROUGE;
				} else if (blancButton.isChecked()) {
					couleur = Couleur.BLANC;
				} else if (roseButton.isChecked()) {
					couleur = Couleur.ROSE;
				}
				vin.setCouleur(couleur);

				boolean isOk = false;
				if (vin.getId()>0) {
					if (vinDao.update(vin) != -1) {
						isOk = true;
					} else {
						isOk = false;
					}
					
				} else {
					if (vinDao.insert(vin) != -1) {
						isOk = true;
					} else {
						isOk = false;
					}
				}
				
				if (isOk) {
					Toast.makeText(getApplicationContext(), R.string.save_ok, Toast.LENGTH_SHORT).show();				
					Intent intent = new Intent();
					intent.setClass(EditVinFormActivity.this.getBaseContext(), ListeVinsActivity.class);
    				intent.putExtra("emptyBottlesOnly", vin.getNbBouteilles()==0);
    				intent.putExtra("tabIndex", vin.getCouleur().getTabIndex());
    				startActivity(intent);
    				EditVinFormActivity.this.finish();
    				
				} else {
					Toast.makeText(getApplicationContext(), R.string.save_error, Toast.LENGTH_SHORT).show();
				}
				vinDao.close();
			}
		});
	}
	
	private void initPhotoButton() {
		Button photoBtn = (Button)findViewById(R.id.editVinFormPhoto);
		photoBtn.setOnClickListener(new OnClickListener() {
		      public void onClick(View v) {
		    	  //cameraView.getCamera().takePicture(shutterCallback, rawCallback, jpegCallback);
		    	  Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);  
		    	  startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);  
		      }
		});
	}
	
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
	    if (requestCode == CAMERA_PIC_REQUEST) {
	    	if (resultCode == RESULT_OK) {
		    	Bitmap imageBmp = (Bitmap) data.getExtras().get("data");  
//		    	int maxWidth = 340;
//		    	if (imageBmp.getWidth() > maxWidth) {
//		    		float percent = imageBmp.getWidth() / maxWidth;
//		    		imageBmp = getResizedBitmap(imageBmp, (int)(imageBmp.getHeight()/percent), (int)(imageBmp.getWidth()/percent));
//		    	}	
		    	etiquetteView.setImageBitmap(imageBmp);  
		    	
		    	ByteArrayOutputStream stream = new ByteArrayOutputStream();
		    	imageBmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
		    	byte[] byteArray = stream.toByteArray();
		    	vin.setImage(byteArray);
	    	}
	    }
	}
	
	private Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
	    int width = bm.getWidth();
	    int height = bm.getHeight();
	    float scaleWidth = ((float) newWidth) / width;
	    float scaleHeight = ((float) newHeight) / height;
	    // CREATE A MATRIX FOR THE MANIPULATION
	    Matrix matrix = new Matrix();
	    // RESIZE THE BIT MAP
	    matrix.postScale(scaleWidth, scaleHeight);

	    // "RECREATE" THE NEW BITMAP
	    Bitmap resizedBitmap = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
	    return resizedBitmap;
	}
}
