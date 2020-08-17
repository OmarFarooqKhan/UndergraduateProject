package project.model;

import java.io.Serializable;

public class _Connection implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -7317035845664884201L;
	private _Entity entity;
	private _Relation relation;
	private String cardinalityType = "";
	public String card = "";
	
	public _Connection(_Entity e,_Relation r) {
		this.entity =e;
		this.relation = r;
	}
	
	public void setCard(String val) {
		this.card = val;
		card = card.trim();
		
	}
	
	public _Entity getEntity() {
		return entity;
	}
	public void setEntity(_Entity entity) {
		this.entity = entity;
		
	}
	public void setRelation(_Relation relation) {
		this.relation = relation;
	}
	public _Relation getRelation() {
		return relation;
	}

	public String getCard() {
		return card;
	}
	
	public String getCardinalityType() {
		return cardinalityType;
	}
	
	public void setCardinalityType(String val) {
		cardinalityType = val;
	}

}
