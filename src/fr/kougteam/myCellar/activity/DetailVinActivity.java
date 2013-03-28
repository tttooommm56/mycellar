package fr.kougteam.myCellar.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.opengl.Visibility;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ImageView.ScaleType;
import android.widget.RatingBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import fr.kougteam.myCellar.R;
import fr.kougteam.myCellar.dao.AppellationDao;
import fr.kougteam.myCellar.dao.PaysDao;
import fr.kougteam.myCellar.dao.RegionDao;
import fr.kougteam.myCellar.dao.VinDao;
import fr.kougteam.myCellar.modele.Region;
import fr.kougteam.myCellar.modele.Vin;

public class DetailVinActivity extends Activity {
	private PaysDao paysDao;
	private RegionDao regionDao;
	private AppellationDao appellationDao;
	private VinDao vinDao;
	private Vin vin;
	
	private TableRow photoTableRow;
	private TableRow etiquetteTableRow;
	
	private Intent intent2Edit;
	
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.detail_vin);
		
		photoTableRow = (TableRow)findViewById(R.id.detailVinPhotoRow);
		etiquetteTableRow = (TableRow)findViewById(R.id.detailVinEtiquetteRow);
		
		paysDao = new PaysDao(this);	
		regionDao = new RegionDao(this);
		appellationDao = new AppellationDao(this);
		vinDao = new VinDao(this);
		
		Bundle extra = this.getIntent().getExtras(); 
		if (extra!=null) {
			vin = vinDao.getById(extra.getInt("idVin"));
			fillFields();
		} 
		
		intent2Edit = new Intent(this, EditVinFormActivity.class);
	}
	
	@Override
	protected void onDestroy() {
		paysDao.close();
		regionDao.close();
		appellationDao.close();
		vinDao.close();
		super.onDestroy();	
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		// Rafraichissement des données
		if (vin!=null && vin.getId()>0) {
			vin = vinDao.getById(vin.getId());
			fillFields();
		}
	}
	
	/**
	 * Méthode qui se déclenchera lorsque vous appuierez sur le bouton menu du téléphone
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
      //Création d'un MenuInflater qui va permettre d'instancier un Menu XML en un objet Menu
      MenuInflater inflater = getMenuInflater();
      //Instanciation du menu XML spécifier en un objet Menu
      inflater.inflate(R.layout.detail_vin_menu, menu); 
      return true;	
	};
	
    /**
     * Méthode qui se déclenchera au clic sur un item du menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         //On regarde quel item a été cliqué grâce à son id et on déclenche une action
         switch (item.getItemId()) {
            case R.id.detailVinRetirer:
            	int currentStock = vin.getNbBouteilles();
            	if (currentStock == 0) {
            		Toast.makeText(getApplicationContext(), "Impossible de retirer une bouteille car le stock est épuisé !", Toast.LENGTH_LONG).show();
            		
            	} else if (vinDao.retire1Bouteille(vin.getId(), currentStock) > 0) {
					vin = vinDao.getById(vin.getId()); // MAJ de l'objet
					((TextView)findViewById(R.id.detailVinBouteilles)).setText(Integer.toString(vin.getNbBouteilles()));
					Toast.makeText(getApplicationContext(), "Le stock a été mis à jour.", Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(getApplicationContext(), "Le stock n'a pas pu être mis à jour.", Toast.LENGTH_LONG).show();
				}	
               return true;
               
            case R.id.detailVinEditer:
            	intent2Edit.putExtra("idVin", vin.getId());
				startActivity(intent2Edit);
                return true;
                
            case R.id.detailVinSupprimer:
            	AlertDialog.Builder builder = new AlertDialog.Builder(DetailVinActivity.this);
	    		builder.setTitle(R.string.warning)
	    		.setIcon(android.R.drawable.ic_dialog_alert)
	    		.setMessage(R.string.delete_item_msg)
	    		.setCancelable(false)
	    		.setPositiveButton(R.string.yes, new DialogInterface.OnClickListener() {
	    			public void onClick(DialogInterface dialog, int id) {
	    				vinDao.delete(vin.getId());    				
	    				Toast.makeText(getApplicationContext(), "L'item a été supprimé.", Toast.LENGTH_LONG).show();
	    				finish();
	    			}
	    		})
	    		.setNegativeButton(R.string.no, new DialogInterface.OnClickListener() {
	    			public void onClick(DialogInterface dialog, int id) {
	    			}
	    		});
	    		AlertDialog alert = builder.create();
	    		alert.show();
                return true;
         }
         return false;
    }
    
    private void fillFields() {
    	String region = paysDao.getById(vin.getIdPays()).getNom();
		Region r = regionDao.getById(vin.getIdRegion());
		if (r!=null && r.getNom()!=null && r.getNom().trim()!="") {
			region += " / " + r.getNom();
		}
		TextView regionText = (TextView)findViewById(R.id.detailVinRegion);
		regionText.setText(region);

		((TextView)findViewById(R.id.detailVinAppellation)).setText(appellationDao.getById(vin.getIdAppellation()).getNom());
		((TextView)findViewById(R.id.detailVinCouleur)).setText(vin.getCouleur().getLabel());
		((TextView)findViewById(R.id.detailVinNom)).setText(vin.getNom());
		((TextView)findViewById(R.id.detailVinProducteur)).setText(vin.getProducteur());
		((TextView)findViewById(R.id.detailVinAnnee)).setText(Integer.toString(vin.getAnnee()));
		((TextView)findViewById(R.id.detailVinBouteilles)).setText(Integer.toString(vin.getNbBouteilles()));
		((RatingBar)findViewById(R.id.detailVinNote)).setRating((float)vin.getNote());
		
		ImageView imageView = (ImageView) findViewById(R.id.detailVinPhoto);
		if (vin.getImage()!=null && vin.getImage().length>0) {
			imageView.setImageBitmap(BitmapFactory.decodeByteArray(vin.getImage() , 0, vin.getImage().length));
		} else {
			etiquetteTableRow.setVisibility(View.GONE);
			photoTableRow.setVisibility(View.GONE);
		}
    }
}
