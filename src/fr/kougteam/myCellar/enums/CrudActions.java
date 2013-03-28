package fr.kougteam.myCellar.enums;

import android.util.Log;

/**
 * Enum pour les actions de type CRUD
 * 
 * @author Thomas Cousin
 *
 */
public enum CrudActions {
	ADD(1, "Ajouter"),
	EDIT(2, "Editer"),
	DELETE(3, "Supprimer"),
	VIEW(4, "Voir"),
	VOCAL(5, "Parler");
	
	private int id;
	private String label;
	
	private CrudActions(int id, String label) {
		this.id = id;
		this.label = label;
	}
	public int getId() {
		return id;
	}
	public String getLabel() {
		return label;
	}
	
	public static CrudActions getFromId(String id) {
		try {
			int i = Integer.parseInt(id);
			return getFromId(i);
			
		} catch (NumberFormatException ex) {
			Log.e(CrudActions.class.getName(), ex.getMessage());
		}
		return null;
	}
	public static CrudActions getFromId(int id) {
		for (CrudActions t : CrudActions.values()) {
			if (t.getId() == id) {
				return t;
			}
		}
		return null;
	}
}
