package fr.kougteam.myCellar.enums;

import android.content.Context;
import fr.kougteam.myCellar.R;

/**
 * Enum pour les couleurs de vin
 * 
 * @author Thomas Cousin
 *
 */
public enum Couleur {
	BLANC("BLANC", R.string.couleur_blanc, 1),
	ROSE("ROSE", R.string.couleur_rose, 2),
	ROUGE("ROUGE", R.string.couleur_rouge, 0);
	
	private String code;
	private int label;
	private int tabIndex;
	
	private Couleur(String code, int label, int tabIndex) {
		this.code = code;
		this.label = label;
		this.tabIndex = tabIndex;
	}
	public String getCode() {
		return code;
	}
	public String getLabel(Context ctx) {
		return ctx.getResources().getString(label);
	}
	public int getTabIndex() {
		return tabIndex;
	}
	public static Couleur getFromId(String code) {
		for (Couleur t : Couleur.values()) {
			if (t.getCode().equals(code)) {
				return t;
			}
		}
		return null;
	}
	public static Couleur getFromTabIndex(int index) {
		for (Couleur t : Couleur.values()) {
			if (t.getTabIndex() == index) {
				return t;
			}
		}
		return null;
	}
}
