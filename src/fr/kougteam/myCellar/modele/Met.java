package fr.kougteam.myCellar.modele;

import java.io.Serializable;

/**
 * Représente un met
 * 
 * @author Thomas Cousin
 *
 */
public class Met implements Serializable {

	private static final long serialVersionUID = 3428679995842594922L;
	private int id;
	private String nom;
		
	public Met() {
		super();
	}
	public Met(int id, String nom) {
		super();
		this.id = id;
		this.nom = nom;
	}
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	
	
}
