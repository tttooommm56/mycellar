package fr.kougteam.myCellar.modele;

import java.io.Serializable;

/**
 * Repr�sente une appelation
 * 
 * @author Thomas Cousin
 *
 */
public class Appellation implements Serializable {
	private static final long serialVersionUID = 3480816128232091740L;
	
	private long id;
	private int idRegion = -1;
	private String nom;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public int getIdRegion() {
		return idRegion;
	}
	public void setIdRegion(int idRegion) {
		this.idRegion = idRegion;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
}
