package fr.kougteam.myCellar.modele;

import java.io.Serializable;

/**
 * Repr�sente un met
 * 
 * @author Thomas Cousin
 *
 */
public class Met implements Serializable {

	private static final long serialVersionUID = 3428679995842594922L;
	private long id;
	private String nom;
		
	public Met() {
		super();
	}
	public Met(long id, String nom) {
		super();
		this.id = id;
		this.nom = nom;
	}
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	
}
