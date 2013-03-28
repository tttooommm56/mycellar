package fr.kougteam.myCellar.enums;

/**
 * Enum pour les couleurs de vin
 * 
 * @author Thomas Cousin
 *
 */
public enum Couleur {
	BLANC("BLANC", "Blanc", 1),
	ROSE("ROSE", "Rosé", 2),
	ROUGE("ROUGE", "Rouge", 0);
	
	private String code;
	private String label;
	private int tabIndex;
	
	private Couleur(String code, String label, int tabIndex) {
		this.code = code;
		this.label = label;
		this.tabIndex = tabIndex;
	}
	public String getCode() {
		return code;
	}
	public String getLabel() {
		return label;
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
}
