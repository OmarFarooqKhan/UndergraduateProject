package project.model;

import java.util.ArrayList;

public class _CompositeAttribute extends _Attribute {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3051725794550421716L;
	private ArrayList<_Attribute> additionalAttributes = new ArrayList<>();
	private String ID;
	public _CompositeAttribute(String name,String ID) {
		super(name, ID);
		// TODO Auto-generated constructor stub
	}
	
	
	public ArrayList<_Attribute> getAdditionalAttributes() {
		return additionalAttributes;
	}
	
	public void removeAttribute(_Attribute attributeToRemove) {
		additionalAttributes.remove(attributeToRemove);
	}
	public void addAdditionalAttribute(_Attribute attributeToAdd) {
		additionalAttributes.add(attributeToAdd);
	}


	public String getID() {
		return ID;
	}

}
