package fr.kougteam.myCellar.enums;

/**
 * Enum pour les couleurs de vin
 * 
 * @author Thomas Cousin
 *
 */
public enum Couleur {
	BLANC("BLANC", "Blanc"),
	ROSE("ROSE", "Rosé"),
	ROUGE("ROUGE", "Rouge");
	
	private String code;
	private String label;
	
	private Couleur(String code, String label) {
		this.code = code;
		this.label = label;
	}
	public String getCode() {
		return code;
	}
	public String getLabel() {
		return label;
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
