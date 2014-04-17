package fr.kougteam.myCellar.modele;

import java.io.Serializable;

/**
 * Repr�sente un pays
 * 
 * @author Thomas Cousin
 *
 */
public class Pays implements Serializable {

	private static final long serialVersionUID = -4336017170678429293L;
	private long id;
	private String nom;
		
	public Pays() {
		super();
	}
	public Pays(long id, String nom) {
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
