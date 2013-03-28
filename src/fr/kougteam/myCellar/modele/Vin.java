package fr.kougteam.myCellar.modele;

import java.io.Serializable;

import fr.kougteam.myCellar.enums.Couleur;

/**
 * Représente un vin
 * 
 * @author Thomas Cousin
 *
 */
public class Vin implements Serializable {
	private static final long serialVersionUID = -6310943971300680714L;
	
	private int id;
	private Couleur couleur = Couleur.BLANC;
	private int idPays = 1; // France par défaut
	private int idRegion = -1;
	private int idAppellation = -1;
	private int annee;
	private String nom;
	private String producteur;
	private String commentaire;
	private int nbBouteilles;	
	private double note;
	private byte[] image;
	
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	public Couleur getCouleur() {
		return couleur;
	}
	public void setCouleur(Couleur couleur) {
		this.couleur = couleur;
	}
	public int getIdPays() {
		return idPays;
	}
	public void setIdPays(int idPays) {
		this.idPays = idPays;
	}
	public int getIdRegion() {
		return idRegion;
	}
	public void setIdRegion(int idRegion) {
		this.idRegion = idRegion;
	}
	public int getIdAppellation() {
		return idAppellation;
	}
	public void setIdAppellation(int idAppellation) {
		this.idAppellation = idAppellation;
	}
	public int getAnnee() {
		return annee;
	}
	public void setAnnee(int annee) {
		this.annee = annee;
	}
	public String getNom() {
		return nom;
	}
	public void setNom(String nom) {
		this.nom = nom;
	}
	public String getProducteur() {
		return producteur;
	}
	public void setProducteur(String producteur) {
		this.producteur = producteur;
	}
	public String getCommentaire() {
		return commentaire;
	}
	public void setCommentaire(String commentaire) {
		this.commentaire = commentaire;
	}
	public int getNbBouteilles() {
		return nbBouteilles;
	}
	public void setNbBouteilles(int nbBouteilles) {
		this.nbBouteilles = nbBouteilles;
	}
	public double getNote() {
		return note;
	}
	public void setNote(double note) {
		this.note = note;
	}
	public byte[] getImage() {
		return image;
	}
	public void setImage(byte[] image) {
		this.image = image;
	}	
}
