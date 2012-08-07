package fr.kougteam.myCellar.activity;

import android.app.TabActivity;
import android.os.Bundle;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;
import fr.kougteam.myCellar.R;
import fr.kougteam.myCellar.enums.Couleur;

public class ListeVinsActivity extends TabActivity {
	/**
	 * @see android.app.Activity#onCreate(Bundle)
	 */
	@Override 
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.liste_vins);
		
		// initialisation des onglets
		TabHost tabs = getTabHost();
	    tabs.setup();
	    
	    TabSpec tspecRouge = tabs.newTabSpec("Tab1");       
	    tspecRouge.setIndicator(Couleur.ROUGE.getLabel());
	    tspecRouge.setContent(R.id.listeVinsRougeTab);
        tabs.addTab(tspecRouge); 

        TabSpec tspecBlanc = tabs.newTabSpec("Tab2");
        tspecBlanc.setIndicator(Couleur.BLANC.getLabel());
        tspecBlanc.setContent(R.id.listeVinsBlancTab);
        tabs.addTab(tspecBlanc);

        TabSpec tspecRose = tabs.newTabSpec("Tab3");
        tspecRose.setIndicator(Couleur.ROSE.getLabel());
        tspecRose.setContent(R.id.listeVinsRoseTab); 
	    tabs.addTab(tspecRose);

	}
}
