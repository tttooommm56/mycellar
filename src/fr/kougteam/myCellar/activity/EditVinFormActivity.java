package fr.kougteam.myCellar.activity;

import java.io.File;
import java.io.IOException;
import java.util.Calendar;
import java.util.Currency;
import java.util.Date;
import java.util.Locale;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FilterQueryProvider;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RatingBar;
import android.widget.SimpleCursorAdapter;
import android.widget.SimpleCursorAdapter.CursorToStringConverter;
import android.widget.Spinner;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import fr.kougteam.myCellar.R;
import fr.kougteam.myCellar.dao.AppellationDao;
import fr.kougteam.myCellar.dao.PaysDao;
import fr.kougteam.myCellar.dao.RegionDao;
import fr.kougteam.myCellar.dao.VinDao;
import fr.kougteam.myCellar.enums.Couleur;
import fr.kougteam.myCellar.helper.FileHelper;
import fr.kougteam.myCellar.modele.Appellation;
import fr.kougteam.myCellar.modele.Region;
import fr.kougteam.myCellar.modele.Vin;
import fr.kougteam.myCellar.provider.ImageContentProvider;
import fr.kougteam.myCellar.tools.FontTools;
import fr.kougteam.myCellar.ui.NumberPicker;

public class EditVinFormActivity extends Activity {
	
	private static final int CAMERA_PIC_REQUEST = 1337;
	private static final int GALLERY_PIC_REQUEST = 1338;

	private PaysDao paysDao;
	private RegionDao regionDao;
	private AppellationDao appellationDao;
	private VinDao vinDao;
	private Vin vin;
	private long mVinId;
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
	private NumberPicker anneeMaturitePicker;
	private NumberPicker nbBouteillesPicker;
	private RatingBar noteRatingBar;
	private TableRow regionTableRow;
	private TableRow territoireTableRow;
	private TableRow appellationTableRow;
	private ImageView etiquetteView;
	private TextView commentaireInput;
	private TextView etagereInput;
	private TextView prixInput;
	
	private int mPaysSpinnerId = -1;
    private int mRegionSpinnerId = -1;
    private int mTerritoireSpinnerId = -1;
    private int mAppellationSpinnerId = -1;
    
    private File etiquetteFile = null;
    private Uri mEtiquetteFileUri = null;

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
		anneeMaturitePicker = (NumberPicker)findViewById(R.id.editVinFormAnneeMaturite);
		nbBouteillesPicker = (NumberPicker)findViewById(R.id.editVinFormBouteilles);
		
		noteRatingBar = (RatingBar)findViewById(R.id.editVinFormNote);
		
		regionTableRow = (TableRow)findViewById(R.id.editVinFormRegionRow);
		territoireTableRow = (TableRow)findViewById(R.id.editVinFormTerritoireRow);
		appellationTableRow = (TableRow)findViewById(R.id.editVinFormAppellationRow);
		
		etiquetteView = (ImageView) findViewById(R.id.editVinResultPhoto); 
		
		etagereInput = (EditText) findViewById(R.id.editVinFormEtagere);
		commentaireInput = (EditText) findViewById(R.id.editVinFormCommentaire);
		prixInput = (EditText) findViewById(R.id.editVinFormPrix);
		
		paysDao = new PaysDao(this);	
		regionDao = new RegionDao(this);
		appellationDao = new AppellationDao(this);
		vinDao = new VinDao(this);

		Bundle extra = this.getIntent().getExtras(); 
		if (extra!=null) {
			setTitle(R.string.title_activity_edit_vin);
			mVinId =  extra.getLong("idVin");
			vin = vinDao.getById(mVinId);
			
			// Si l'année de maturité n'est pas renseignée, on met l'année de la bouteille par défaut
			if (vin.getAnneeMaturite()<=0) {
				vin.setAnneeMaturite(vin.getAnnee());
			}
			
		} else {
			setTitle(R.string.title_activity_add_vin);
			vin = new Vin();
			vin.setIdPays(1);   // France par défaut  
			vin.setCouleur(Couleur.ROUGE); // Rouge par défaut
			Calendar cal = Calendar.getInstance();
			cal.setTime(new Date());		
			vin.setAnnee(cal.get(Calendar.YEAR)-1); // Année précédente par défaut
			vin.setAnneeMaturite(cal.get(Calendar.YEAR)-1); // Année précédente par défaut
			vin.setNbBouteilles(1);
		}
		
		fillFields();
		
		loadPaysSpinner();
		
		initCancelButton();
		initSaveButton();	
		initPhotoButton();
		initImportGalleryButton();
		
		final ViewGroup mContainer = (ViewGroup) findViewById(android.R.id.content);
        FontTools.setDefaultAppFont(mContainer, getAssets());
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
		// Symbole de la monnaie 
		((TextView)findViewById(R.id.editVinFormPrixMonnaieText)).setText(Currency.getInstance(Locale.getDefault()).getSymbol());
		
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
		
		// Année de maturité
		anneeMaturitePicker.setValue(vin.getAnneeMaturite());
		
		// Nb bouteilles
		nbBouteillesPicker.setValue(vin.getNbBouteilles());
		
		// Note
		noteRatingBar.setRating((float)vin.getNote());
		
		// Prix
		prixInput.setText(Float.toString(vin.getPrix()));
		
		// Etagere
		etagereInput.setText(vin.getEtagere());
		
		// Commentaire
		commentaireInput.setText(vin.getCommentaire());
		
		// Etiquette
		File etiquetteFile = new File(ImageContentProvider.IMAGE_DIRECTORY, "etq_"+vin.getId()+".jpg");
		if (etiquetteFile.exists()) {
			ImageContentProvider.fillImageViewWithFile(etiquetteView, etiquetteFile);	
		}
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
        	long idRegion = -1;
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

	private int getSpinnerPostition(Cursor c, String idColName, long searchId) {
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
				vin.setAnneeMaturite(anneeMaturitePicker.getValue());
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
				String prixStr = prixInput.getText().toString();
				if (prixStr!=null && !"".equals(prixStr)) {
					try {
						vin.setPrix(Float.parseFloat(prixStr));
					} catch (Exception e) {
						Log.e("Error during parse 'prix' !", e.getMessage());
					}
				}
				vin.setEtagere(etagereInput.getText().toString());
				vin.setCommentaire(commentaireInput.getText().toString());

				boolean isOk = false;
				if (vin.getId()>0) {
					if (vinDao.update(vin) != -1) {
						isOk = true;
					} else {
						isOk = false;
					}
					
				} else {
					vin.setDateAjout(new Date()); // date du jour par défaut
					vin.setId(vinDao.insert(vin));
					if (vin.getId() != -1) {
						isOk = true;
					} else {
						isOk = false;
					}
				}
				
				// Sauvegarde de l'image sur la carte SD
				if (FileHelper.isSDPresent() && FileHelper.canWriteOnSD() && etiquetteFile!=null && etiquetteFile.exists()) {
					try {
						File destDir= new File(ImageContentProvider.IMAGE_DIRECTORY);
						if (!destDir.exists()) {
							destDir.mkdirs();
						}
						FileHelper.copyFile(etiquetteFile, new File(destDir, "etq_"+vin.getId()+".jpg"));
						etiquetteFile.delete();
					} catch (IOException ioe) {
						ioe.printStackTrace();
						Toast.makeText(getApplicationContext(), "Erreur lors de l'enregistrement de la photo !", Toast.LENGTH_SHORT).show();
					}
				}
				
				if (isOk) {
					Toast.makeText(getApplicationContext(), R.string.save_ok, Toast.LENGTH_SHORT).show();				
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
				PackageManager pm = getPackageManager();
				if (pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
					try {
						etiquetteFile = new File(ImageContentProvider.IMAGE_DIRECTORY, "tmp.jpg");	
						etiquetteFile.createNewFile();
						Intent i = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
						i.putExtra(MediaStore.EXTRA_OUTPUT,	Uri.fromFile(etiquetteFile));
						startActivityForResult(i, CAMERA_PIC_REQUEST);
						
					} catch (IOException e) {
						e.printStackTrace();
					}

				} else {
					Toast.makeText(getBaseContext(), "Camera is not available", Toast.LENGTH_LONG).show();
				}
			}
		});
	}
	
	private void initImportGalleryButton() {
		etiquetteFile = null;
		Button photoBtn = (Button)findViewById(R.id.editVinFormGallery);
		photoBtn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);                
                startActivityForResult(i, GALLERY_PIC_REQUEST);
			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		boolean showPicture = false;
		super.onActivityResult(requestCode, resultCode, data);
		Log.i(getClass().getName(), "Receive the camera result");
			
		if (resultCode == RESULT_OK) {
			
			switch (requestCode) {
			
				case CAMERA_PIC_REQUEST :					
					showPicture = true;
					break;
					
				case GALLERY_PIC_REQUEST :
					if (data!=null) {
						Uri selectedImage = data.getData();
			            String[] filePathColumn = { MediaStore.Images.Media.DATA };
			            Cursor cursor = getContentResolver().query(selectedImage, filePathColumn, null, null, null);
			            cursor.moveToFirst();
			            int columnIndex = cursor.getColumnIndex(filePathColumn[0]);	            
			            etiquetteFile = new File(cursor.getString(columnIndex));
			            cursor.close();
			            showPicture = true;
					}
					break;
			}
			
			if (etiquetteFile!=null) {
				if (showPicture) {
					ImageContentProvider.fillImageViewWithFileResized(etiquetteView, etiquetteFile, getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight());	
				}
			}
		}
	}
		 
//	private void initPhotoButton() {
//		Button photoBtn = (Button)findViewById(R.id.editVinFormPhoto);
//		photoBtn.setOnClickListener(new OnClickListener() {
//		      public void onClick(View v) {
//		    	  //cameraView.getCamera().takePicture(shutterCallback, rawCallback, jpegCallback);
//		    	  Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);  
//		    	  startActivityForResult(cameraIntent, CAMERA_PIC_REQUEST);  
//		      }
//		});
//	}
//	
//	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//	    if (requestCode == CAMERA_PIC_REQUEST) {
//	    	if (resultCode == RESULT_OK) {
//		    	Bitmap imageBmp = (Bitmap) data.getExtras().get("data");  
////		    	int maxWidth = 340;
////		    	if (imageBmp.getWidth() > maxWidth) {
////		    		float percent = imageBmp.getWidth() / maxWidth;
////		    		imageBmp = getResizedBitmap(imageBmp, (int)(imageBmp.getHeight()/percent), (int)(imageBmp.getWidth()/percent));
////		    	}	
//		    	etiquetteView.setImageBitmap(imageBmp);  
//		    	
//		    	ByteArrayOutputStream stream = new ByteArrayOutputStream();
//		    	imageBmp.compress(Bitmap.CompressFormat.PNG, 100, stream);
//		    	byte[] byteArray = stream.toByteArray();
//		    	vin.setImage(byteArray);
//	    	}
//	    }
//	}
	
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
