package project.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
//import java.util.HashMap;
import java.util.List;

public class _Relation implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 5203923290707457814L;
	public List<_Attribute> attributes;
	public List<_Entity> entities;
	public List<_Connection> connections;
	public HashMap<_CompositeAttribute,List<_Attribute>> compositeAttributes;
	
	private String name;
	private String TypeOfRelationship = "noType"; 

	public _Relation(String name){
		this.name = name;
		attributes = new ArrayList<>();
		connections = new ArrayList<>();
		entities = new ArrayList<>();
		compositeAttributes = new  HashMap<>();
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public void addAtr(_Attribute attr) {
		attributes.add(attr);
	}
	public void removeAttribute(_Attribute attribute) {
		attributes.remove(attribute);
	}
	
	public void addCompositeAttribute(_CompositeAttribute compositeAttribute) {
		compositeAttributes.put(compositeAttribute, new ArrayList<_Attribute> ());
	}
	public void addAttributeToComp(_CompositeAttribute compositeAttribute,_Attribute attribute) {
		compositeAttributes.get(compositeAttribute).add(attribute);
	}
	public void removeCompositeAttribute(_CompositeAttribute compositeAttribute) {
		compositeAttributes.remove(compositeAttribute);
	}
	public void removeAttributeFromComp(_CompositeAttribute compositeAttribute,_Attribute attribute) {
		compositeAttributes.get(compositeAttribute).remove(attribute);
	}
	public void addEntity(_Entity ent) {
		entities.add(ent);
	}
	public void removeEntity(_Entity ent) {
		entities.remove(ent);
	}
	public void addConnection(_Connection connection) {
		connections.add(connection);
	}
	public void removeConnections(_Connection connection) {
		if(connections.contains(connection)) {
			connections.remove(connection);
		}
	}
	public List<_Connection> getConnection() {
		return connections;
	}
	public List<_Entity> getEntities() {
		return entities;
	}
	public List<_Attribute> getAttributes() {
		return attributes;
	}
	public HashMap<_CompositeAttribute, List<_Attribute>> getCompositeAttributes (){
		return compositeAttributes;
	}
	public List<_Attribute> getCompositeAttributesList(_Attribute _Attribute){
		return this.compositeAttributes.get(_Attribute);
	}

	public String getTypeOfRelationship() {
		return TypeOfRelationship;
	}

	public void setTypeOfRelationship(String typeOfRelationship) {
		TypeOfRelationship = typeOfRelationship;
	}
}
