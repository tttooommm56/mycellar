package fr.kougteam.myCellar.activity;

import java.io.File;
import java.util.ArrayList;
import java.util.Currency;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;
import fr.kougteam.myCellar.R;
import fr.kougteam.myCellar.dao.AppellationDao;
import fr.kougteam.myCellar.dao.MetDao;
import fr.kougteam.myCellar.dao.MetVinDao;
import fr.kougteam.myCellar.dao.PaysDao;
import fr.kougteam.myCellar.dao.RegionDao;
import fr.kougteam.myCellar.dao.VinDao;
import fr.kougteam.myCellar.modele.Met;
import fr.kougteam.myCellar.modele.Region;
import fr.kougteam.myCellar.modele.Vin;
import fr.kougteam.myCellar.provider.ImageContentProvider;
import fr.kougteam.myCellar.tools.FileTools;
import fr.kougteam.myCellar.tools.FontTools;
import fr.kougteam.myCellar.tools.StringTools;

public class DetailVinActivity extends Activity {
	private PaysDao paysDao;
	private RegionDao regionDao;
	private AppellationDao appellationDao;
	private VinDao vinDao;
	private MetVinDao metVinDao;
	private Vin vin;
	
	private TableRow photoTableRow;
	private TableRow etiquetteTableRow;
	private TableRow accordVinMetTextTableRow;
	private TableRow accordVinMetTableRow;
	
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
		accordVinMetTextTableRow = (TableRow)findViewById(R.id.detailVinAccordVinMetTextRow);
		accordVinMetTableRow = (TableRow)findViewById(R.id.detailVinAccordVinMetRow);
		
		paysDao = new PaysDao(this);	
		regionDao = new RegionDao(this);
		appellationDao = new AppellationDao(this);
		vinDao = new VinDao(this);
		metVinDao = new MetVinDao(this);
		
		Context context = getBaseContext();
		FileTools.copyFile(context, FontTools.DEFAULT_FONT_NAME);
		
		Bundle extra = this.getIntent().getExtras(); 
		if (extra!=null) {
			vin = vinDao.getById(extra.getLong("idVin"));
			fillFields(context);
		} 
		
		intent2Edit = new Intent(this, EditVinFormActivity.class);
		
		final ViewGroup mContainer = (ViewGroup) findViewById(android.R.id.content);
        FontTools.setDefaultAppFont(mContainer, getAssets());
	}
	
	@Override
	protected void onDestroy() {
		paysDao.close();
		regionDao.close();
		appellationDao.close();
		vinDao.close();
		metVinDao.close();
		super.onDestroy();	
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		// Rafraichissement des donn�es
		if (vin!=null && vin.getId()>0) {
			vin = vinDao.getById(vin.getId());
			Context context = getBaseContext();
			FileTools.copyFile(context, FontTools.DEFAULT_FONT_NAME);
			fillFields(context);
		}
	}
	
	/**
	 * M�thode qui se d�clenchera lorsque vous appuierez sur le bouton menu du t�l�phone
	 */
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
      //Cr�ation d'un MenuInflater qui va permettre d'instancier un Menu XML en un objet Menu
      MenuInflater inflater = getMenuInflater();
      //Instanciation du menu XML sp�cifier en un objet Menu
      inflater.inflate(R.layout.detail_vin_menu, menu); 
      return true;	
	};
	
    /**
     * M�thode qui se d�clenchera au clic sur un item du menu
     */
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
         //On regarde quel item a �t� cliqu� gr�ce � son id et on d�clenche une action
         switch (item.getItemId()) {
            case R.id.detailVinRetirer:
            	int currentStock = vin.getNbBouteilles();
            	if (currentStock == 0) {
            		Toast.makeText(getApplicationContext(), R.string.retirer_impossible, Toast.LENGTH_LONG).show();
            		
            	} else if (vinDao.retire1Bouteille(vin.getId(), currentStock) > 0) {
					vin = vinDao.getById(vin.getId()); // MAJ de l'objet
					((TextView)findViewById(R.id.detailVinBouteilles)).setText(Integer.toString(vin.getNbBouteilles()));
					Toast.makeText(getApplicationContext(), R.string.stock_maj_ok, Toast.LENGTH_LONG).show();
				} else {
					Toast.makeText(getApplicationContext(), R.string.stock_maj_ko, Toast.LENGTH_LONG).show();
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
	    				Toast.makeText(getApplicationContext(), R.string.item_deleted, Toast.LENGTH_LONG).show();
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
    
    private void fillFields(Context context) {
    	String region = paysDao.getById(vin.getIdPays()).getNom();
		Region r = regionDao.getById(vin.getIdRegion());
		if (r!=null && r.getNom()!=null && !"".equals(r.getNom().trim())) {
			region += " / " + r.getNom();
		}
		TextView regionText = (TextView)findViewById(R.id.detailVinRegion);
		regionText.setText(region);

		((TextView)findViewById(R.id.detailVinAppellation)).setText(appellationDao.getById(vin.getIdAppellation()).getNom());
		((TextView)findViewById(R.id.detailVinCouleur)).setText(vin.getCouleur().getLabel(getApplicationContext()));
		((TextView)findViewById(R.id.detailVinNom)).setText(vin.getNom());
		((TextView)findViewById(R.id.detailVinProducteur)).setText(vin.getProducteur());
		((TextView)findViewById(R.id.detailVinAnnee)).setText(Integer.toString(vin.getAnnee()));
		((TextView)findViewById(R.id.detailVinBouteilles)).setText(Integer.toString(vin.getNbBouteilles()));
		if (vin.getAnneeMaturite()>0) {
			((TextView)findViewById(R.id.detailVinAnneeMaturite)).setText(Integer.toString(vin.getAnneeMaturite()));
		}
		((TextView)findViewById(R.id.detailVinEtagere)).setText(vin.getEtagere());
		((TextView)findViewById(R.id.detailVinCommentaire)).setText(vin.getCommentaire());
		if (vin.getPrix()>0) {
			((TextView)findViewById(R.id.detailVinPrix)).setText(String.format("%.2f", vin.getPrix()) + " " + Currency.getInstance(Locale.getDefault()).getSymbol());
		}
		if (vin.getDateAjout()!=null) {
			((TextView)findViewById(R.id.detailVinDateAjout)).setText(DateFormat.format("dd/MM/yyyy", vin.getDateAjout()));
		}
		((RatingBar)findViewById(R.id.detailVinNote)).setRating((float)vin.getNote());
		
		ImageView imageView = (ImageView) findViewById(R.id.detailVinPhoto);
		File etiquetteFile = new File(ImageContentProvider.IMAGE_DIRECTORY, "etq_"+vin.getId()+".jpg");
		if (etiquetteFile.exists()) {	
			ImageContentProvider.fillImageViewWithFile(imageView, etiquetteFile, getWindowManager().getDefaultDisplay().getWidth(), getWindowManager().getDefaultDisplay().getHeight());		
		} else {
			etiquetteTableRow.setVisibility(View.GONE);
			photoTableRow.setVisibility(View.GONE);
		}
		
		String nom = vin.getNom();
		if (vin.getIdAppellation()>0) {
			nom = appellationDao.getById(vin.getIdAppellation()).getNom();
		}
		Cursor c = metVinDao.getMetsByNomVin(nom);	
		if (c.getCount()>0) {
			StringBuilder html = new StringBuilder();
			html.append("<html>"+
							"<head>"+
								"<style type='text/css'>"+
								"@font-face {"+
								"    font-family: MyFont;"+
								"    src: url('file://"+context.getFilesDir().getAbsolutePath()+"/"+FontTools.DEFAULT_FONT_NAME+"')"+
								"}"+
								"body {"+
								"    font-family: MyFont;"+
								"    color:#eee;"+
								"}"+
								"</style>"+
							"</head>"+
						"<body>");
			html.append("<ul>");
			for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				Met m = new Met(c.getInt(c.getColumnIndex(MetDao.COL_ID)), c.getString(c.getColumnIndex(MetDao.COL_NOM)));
				html.append("<li>");
				html.append(StringTools.escapeHTML(m.getNom()));
				html.append("</li>");
			}
			html.append("</ul></body></html>");
			WebView web = ((WebView)findViewById(R.id.accordVinMetListeVinHtml));
			web.loadDataWithBaseURL(null, html.toString(), "text/html", "utf-8", "");
			web.setBackgroundColor(getResources().getColor(android.R.color.black));
		} else {
			accordVinMetTextTableRow.setVisibility(View.GONE);
			accordVinMetTableRow.setVisibility(View.GONE);
		}
    }
}
