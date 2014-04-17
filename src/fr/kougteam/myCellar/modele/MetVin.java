package fr.kougteam.myCellar.modele;

import java.io.Serializable;

/**
 * Repr�sente une association met / vin
 * 
 * @author Thomas Cousin
 *
 */
public class MetVin implements Serializable {

	private static final long serialVersionUID = 3428679995842594922L;
	private long idMet;
	private String nomVin;
	private String type;
		
	public MetVin() {
		super();
	}
	public MetVin(long idMet, String nomVin, String type) {
		super();
		this.idMet = idMet;
		this.nomVin = nomVin;
		this.type = type;
	}
	public long getIdMet() {
		return idMet;
	}
	public void setIdMet(long idMet) {
		this.idMet = idMet;
	}
	public String getNomVin() {
		return nomVin;
	}
	public void setNomVin(String nomVin) {
		this.nomVin = nomVin;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}	
}
