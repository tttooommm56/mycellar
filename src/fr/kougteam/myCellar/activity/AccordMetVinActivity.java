package fr.kougteam.myCellar.activity;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.TextUtils;
import android.webkit.WebView;
import android.widget.TextView;
import fr.kougteam.myCellar.R;
import fr.kougteam.myCellar.dao.MetDao;
import fr.kougteam.myCellar.dao.MetVinDao;
import fr.kougteam.myCellar.modele.Met;
import fr.kougteam.myCellar.modele.MetVin;
import fr.kougteam.myCellar.tools.StringTools;

public class AccordMetVinActivity extends Activity {
	private MetDao metDao;
	private MetVinDao metVinDao;
	private Met met;

	private Intent intent2DetailVin;
	
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.accord_met_vin);
		
		metDao = new MetDao(this);	
		metVinDao = new MetVinDao(this);
		
		Bundle extra = this.getIntent().getExtras(); 
		if (extra!=null) {
			met = metDao.getById(extra.getInt("idMet"));
			fillFields();
		} 
		
		intent2DetailVin = new Intent(this, DetailVinActivity.class);
	}
	
	@Override
	protected void onDestroy() {
		metDao.close();
		metVinDao.close();
		super.onDestroy();	
	}
	
	@Override
	public void onResume() {
		super.onResume();
		
		// Rafraichissement des données
		if (met!=null && met.getId()>0) {
			met = metDao.getById(met.getId());
			fillFields();
		}
	}

    private void fillFields() {
		((TextView)findViewById(R.id.accordMetVinSelectectedMet)).setText(met.getNom());
		
		Cursor c = metVinDao.getListByIdMet(met.getId());
    
		StringBuilder html = new StringBuilder("<ul style='color:#eee;'>");
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
			MetVin mv = new MetVin(met.getId(), c.getString(c.getColumnIndex(MetVinDao.COL_NOM_VIN)), c.getString(c.getColumnIndex(MetVinDao.COL_TYPE)));
			html.append("<li>");
			html.append(StringTools.escapeHTML(mv.getNomVin()));
			if (mv.getType()!=null && !"".equals(mv.getType())) {
				html.append(" ("+StringTools.escapeHTML(mv.getType())+")");
			}
			html.append("</li>");
		}
		html.append("</ul>");
		WebView web = ((WebView)findViewById(R.id.accordMetVinListeVinHtml));
		web.loadData(html.toString(), "text/html", "utf-8");
		web.setBackgroundColor(getResources().getColor(android.R.color.black));

    }
}
