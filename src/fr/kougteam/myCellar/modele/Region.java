package fr.kougteam.myCellar.modele;

import java.io.Serializable;

/**
 * Repr�sente une r�gion
 * 
 * @author Thomas Cousin
 *
 */
public class Region implements Serializable {
	private static final long serialVersionUID = 8034923637426557685L;
	
	private long id;
	private long idPays = 1; // France par d�faut
	private long idRegionParent;
	private String nom;
	
	public long getId() {
		return id;
	}
	public void setId(long id) {
		this.id = id;
	}
	public long getIdPays() {
		return idPays;
	}
	public void setIdPays(long idPays) {
		this.idPays = idPays;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public long getIdRegionParent() {
		return idRegionParent;
	}
	public void setIdRegionParent(long idRegionParent) {
		this.idRegionParent = idRegionParent;
	}
	public boolean isSousRegion() {
		return this.idRegionParent>0;
	}
}