package fr.kougteam.myCellar.modele;

import java.io.Serializable;

/**
 * Représente une région
 * 
 * @author Thomas Cousin
 *
 */
public class Region implements Serializable {
	private static final long serialVersionUID = 8034923637426557685L;
	
	private int id;
	private int idPays = 1; // France par défaut
	private String nom;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public int getIdPays() {
		return idPays;
	}
	public void setIdPays(int idPays) {
		this.idPays = idPays;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
}