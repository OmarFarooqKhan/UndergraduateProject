package project.utils;

import java.util.ArrayList;
import java.util.List;

import project.model.Model;
import project.model._Attribute;
import project.model._CompositeAttribute;
import project.model._Connection;
import project.model._Relation;

public class Nary extends ObjectEvaluator {

	
	public Nary(Model model) {
		this.model = model;
	}
	public String naryCreation(String text) {
		for (_Relation relation : model.getRelations()) {
			String relationName = relation.getName();
			String relationType = relation.getTypeOfRelationship();
			if (relationType.equals(nary)) {

				text += "NARY " + '\n';

				List<_Connection> connectionList = relation.getConnection();
				String foreignKey = "";
				String primaryKey = "    PRIMARY KEY(";
				text += "CREATE TABLE " + relationName + '\n' + "( " + '\n';
				List<_Attribute> toBeIgnored = new ArrayList<>();


				for (int i = 0; i < connectionList.size(); i++) {
					
					for (_Attribute attribute : connectionList.get(i).getEntity().attributes) {
						if (attribute.isPrimaryKey() && attribute.isCompositeIdentifier()) {
							int n = 1;
							
							toBeIgnored.add(attribute);

							foreignKey += "    FOREIGN KEY (";
							for (int j = 0; j < attribute.getIdentifiers().size(); j++) {
								toBeIgnored.add(attribute.getIdentifiers().get(j));


								text += "    " + attribute.getIdentifiers().get(j).getName() + " "
										+ attribute.getIdentifiers().get(j).getType() + " " + "NOT NULL " + '\n';

								if (n + j >= attribute.getIdentifiers().size()) {
									primaryKey += attribute.getIdentifiers().get(j).getName() +" " ;
									foreignKey += identiferNames(attribute) + ")" + " REFERENCES "
											+ connectionList.get(i).getEntity().getName() + "(" + identiferNames(attribute)+ ") " + '\n';

								} else {
									primaryKey += attribute.getIdentifiers().get(j).getName() + ", ";

								}
							}

						} else {
							continue;
						}
					}
				}
				for (int i = 0; i < connectionList.size(); i++) {
					
					
					for (_Attribute attribute : connectionList.get(i).getEntity().attributes) {
						if(toBeIgnored.contains(attribute)){
							continue;
						}
						else {
							if (attribute.isPrimaryKey() && !attribute.isCompositeIdentifier()) {
							
								if(connectionList.size() -1 == i) {
									primaryKey += attribute.getName() + ") " + '\n';
								}
								else {
									primaryKey += attribute.getName() + " ";
								}
								text += "    " + attribute.getName() + " " + attribute.getType() + " " + "NOT NULL " + '\n';
								foreignKey += "    FOREIGN KEY (" + connectionList.get(i).getEntity().getPrimaryKey().getName()
									+ ")" + " REFERENCES " + connectionList.get(i).getEntity().getName() + "("
									+ connectionList.get(i).getEntity().getPrimaryKey().getName() + ") " + '\n';
								break;
							} 
						}
					}
				}				
				
				for (_Attribute attribute : relation.getAttributes()) {
					text += "    " + attribute.getName() + " " + attribute.getType() + " " + '\n';
				}

				for (_CompositeAttribute comp : relation.getCompositeAttributes().keySet()) {
					for (_Attribute attribute : comp.getAdditionalAttributes()) {
						text += "    " + attribute.getName() + " " + attribute.getType() + " " + "NOT NULL " + '\n';
					}
				}
				
				text += primaryKey;
				text += foreignKey;
				text += "); " + '\n';

			}
		}
		return text;
	}
}
