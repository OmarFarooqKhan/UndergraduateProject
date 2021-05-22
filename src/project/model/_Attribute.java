package project.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class _Attribute implements Serializable{
	
	/**
	 * 
	 */
	boolean primaryKey = false;
	boolean foreignKey = false;
	boolean compositeAttribute = false;
	boolean compositeIdentifier = false;
	
	private static final long serialVersionUID = 127786007711136086L;
	private String name;
	private String Type = "text";
	private String id;
	private List<_Attribute> identifiers;
	
	public _Attribute(String name,String ID) {
		this.name = name;
		this.setId(ID);
	}
	
	public String getName() {
		return name;
	}
	
	public void setType(String type) {
		Type = type;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public boolean isPrimaryKey() {
		return primaryKey;
	}
	public void setPrimaryKey(boolean status) {
		primaryKey = status;
	}
	public void setForeignKey(boolean status) {
		foreignKey = status;
	}
	
	public boolean isForeignKey() {
		return foreignKey;
	}
	public boolean isCompositeAttribute() {
		return compositeAttribute;
	}
	public boolean isCompositeIdentifier() {
		return compositeIdentifier;
	}
	public String getType() {
		return Type;
	}
	public void setCompositeIdentifier(boolean val) {
		identifiers = new ArrayList<>();
		compositeIdentifier = val;
		
	}
	public List<_Attribute> getIdentifiers(){
		return identifiers;
	}

	public String getId() {
		return id;
	}

	private void setId(String id) {
		this.id = id;
	}

}
