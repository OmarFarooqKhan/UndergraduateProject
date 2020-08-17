package project.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
//import java.util.HashMap;
import java.util.List;

public class _Entity implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3641043906422787185L;
	/**
	 * 
	 */
	private String name;
	//private boolean weakEnt = false;
	@SuppressWarnings("unused")
	private String ID;
	public ArrayList<_Attribute> attributes;
	public HashMap<_CompositeAttribute,List<_Attribute>> compositeAttributes;
	public List<_Connection> connections;
	private boolean weakEnt = false;
	

	public _Entity(String name,String ID) {
		attributes = new ArrayList<>();
		connections = new ArrayList<>(); 
		compositeAttributes = new  HashMap<>();
		this.name = name;
		this.ID = ID;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public void addAttribute(_Attribute attribute) {
		attributes.add(attribute);
	}
	public void addConnection(_Connection connection) {
		connections.add(connection);
	}
	public void addCompositeAttribute(_CompositeAttribute compositeAttribute) {
		if(compositeAttribute.getAdditionalAttributes().size() > 0 ) {
			compositeAttributes.put(compositeAttribute,compositeAttribute.getAdditionalAttributes());
			return;
		}
		else {
			compositeAttributes.put(compositeAttribute, new ArrayList<_Attribute> ());
		}
	}

	public void addAttributeToComp(_CompositeAttribute compositeAttribute,_Attribute attribute) {
		compositeAttributes.get(compositeAttribute).add(attribute);
	}

	public void removeAttribute(_Attribute attribute) {
		attributes.remove(attribute);
	}
	
	public void removeCompositeAttribute(_CompositeAttribute compositeAttribute) {
		compositeAttributes.remove(compositeAttribute);
	}
	public void removeAttributeFromComp(_CompositeAttribute compositeAttribute,_Attribute attribute) {
		compositeAttributes.get(compositeAttribute).remove(attribute);
	}
	
	public void removeConnection(_Connection connection) {
		if(connections.contains(connection)) {
			connections.remove(connection);
		}
	}
	
	public List<_Attribute> getAttributes() {
		return attributes;
	}
	public List<_Connection> getConnections() {
		return connections;
	}
	public HashMap<_CompositeAttribute, List<_Attribute>> getCompositeAttributes (){
		return compositeAttributes;
	}
	public List<_Attribute> getCompositeAttributesList(_Attribute _Attribute){
		return this.compositeAttributes.get(_Attribute);
	}
	public void setWeakEnt(boolean value) {
		weakEnt = value;
	}
	public boolean isWeakEnt() {
		return weakEnt;
	}
	
	public _Attribute getPrimaryKey() {
		for(_Attribute attribute :attributes) {
			if (attribute.isPrimaryKey()) {
				return attribute;
			}
		}
		return null;
	}
	
//	public boolean primaryKeyCount() {
//		int primaryKeyNumber = 0;
//		for(_Attribute attribute: attributes) {
//			if(attribute.isPrimaryKey()) {
//				primaryKeyNumber++;
//			}
//		}
//		if(primaryKeyNumber == 1 || weakEnt) {
//			return true;
//		}
//		return false;
//	}
	

}
