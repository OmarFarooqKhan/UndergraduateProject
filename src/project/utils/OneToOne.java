package project.utils;

import java.util.ArrayList;
import java.util.List;

import project.model.Model;
import project.model._Attribute;
import project.model._CompositeAttribute;
import project.model._Connection;
import project.model._Relation;

public class OneToOne extends ObjectEvaluator {
	
	
	public OneToOne(Model model, SqlGenerator sqlGenerator) {
		this.model = model;
		this.gen = sqlGenerator;
	}
	
	public String oneToOneCreation(String text) {
		for (_Relation relation : model.getRelations()) {
			String relationName = relation.getName();
			String relationType = relation.getTypeOfRelationship();
			if (gen.relationsToIgnore.contains(relation)) {
				continue;
			}

			if (relationType.equals(oneone)) {

				List<_Connection> connectionList = relation.getConnection();
				if (connectionList.get(0).getCardinalityType().equals("TP")
						&& connectionList.get(1).getCardinalityType().equals("TP")) {
					text += "ONE TO ONE TOTAL " + '\n';
					
					_Attribute pk1 = connectionList.get(0).getEntity().getPrimaryKey();
					_Attribute pk2 = connectionList.get(1).getEntity().getPrimaryKey();
					List<_Attribute> ignoreMe = new ArrayList<>();

					String pK = "    PRIMARY KEY (";
					text += "CREATE TABLE " + relationName + '\n' + "( " + '\n';
					
					
					if(pk1.isCompositeIdentifier()) {
						
						int k = 1;
						for (int j = 0; j < pk1.getIdentifiers().size(); j++) {

							if (k + j >= pk1.getIdentifiers().size()) {
								text += "    " + pk1.getIdentifiers().get(j).getName().trim() + " "
										+ pk1.getIdentifiers().get(j).getType() + " " + "NOT NULL" + '\n';
								ignoreMe.add(pk1.getIdentifiers().get(j));
								
							} else {
								text += "    " + pk1.getIdentifiers().get(j).getName() + " "
										+ pk1.getIdentifiers().get(j).getType() + " " + "NOT NULL" + '\n';
								ignoreMe.add(pk1.getIdentifiers().get(j));
							}
						}
					}
					else {
						text += "    " + pk1.getName() + " "
								+ pk1.getType() + " " + "NOT NULL" + '\n';
					}
					
					if(pk2.isCompositeIdentifier()) {
						
						int k = 1;
						for (int j = 0; j < pk2.getIdentifiers().size(); j++) {

							if (k + j >= pk2.getIdentifiers().size()) {
								text += "    " + pk2.getIdentifiers().get(j).getName() + " "
										+ pk2.getIdentifiers().get(j).getType() + " " + "NOT NULL" + '\n';
								
								ignoreMe.add(pk2.getIdentifiers().get(j));
								
							} else {
								text += "    " + pk2.getIdentifiers().get(j).getName() + " "
										+ pk2.getIdentifiers().get(j).getType() + " " + "NOT NULL" + '\n';
								ignoreMe.add(pk2.getIdentifiers().get(j));
								
							}
						}
					}
					else {
						text += "    " + pk2.getName() + " "
								+ pk2.getType() + " " + "NOT NULL " + '\n';
					}
					

					for (_Attribute attribute : relation.getAttributes()) {
						text += "    " + attribute.getName() + " " + attribute.getType() + " "  + '\n';
					}
					if (relation.getCompositeAttributes().size() > 0) {
						for (_CompositeAttribute comp : relation.getCompositeAttributes().keySet()) {
							for (_Attribute attribute : comp.getAdditionalAttributes()) {
								text += "    " + attribute.getName() + " " + attribute.getType() + " "
										+ '\n';
							}
						}
					}

					for (_Attribute attribute : connectionList.get(0).getEntity().attributes) {
						if (!attribute.isPrimaryKey() && !ignoreMe.contains(attribute)) {
							text += "    " + attribute.getName() + " " + attribute.getType() + " "  + '\n';
						}
					}

					for (_CompositeAttribute comp : connectionList.get(0).getEntity().getCompositeAttributes()
							.keySet()) {
						for (_Attribute attribute : comp.getAdditionalAttributes()) {
							text += "    " + attribute.getName() + " " + attribute.getType() + " "  + '\n';
						}
					}

					for (_Attribute attribute : connectionList.get(1).getEntity().attributes) {
						if (!attribute.isPrimaryKey() && !ignoreMe.contains(attribute)) {
							text += "    " + attribute.getName() + " " + attribute.getType() + " "  + '\n';
						}
					}

					for (_CompositeAttribute comp : connectionList.get(1).getEntity().getCompositeAttributes()
							.keySet()) {
						for (_Attribute attribute : comp.getAdditionalAttributes()) {
							text += "    " + attribute.getName() + " " + attribute.getType() + " " + '\n';
						}
					}
					if(pk1.isCompositeIdentifier()) {
						int k = 1;
						for (int j = 0; j < pk1.getIdentifiers().size(); j++) {

							if (k + j >= pk1.getIdentifiers().size()) {
								pK += pk1.getIdentifiers().get(j).getName() + ") "+ '\n';
								
							} else {
								pK += pk1.getIdentifiers().get(j).getName() + ", ";
							}
						}
					}
					
					else {
						pK += connectionList.get(0).getEntity().getPrimaryKey().getName() + ") " + '\n';
					}
					text += pK;
					text += "); " + '\n';

					ignoreMe.clear();
					gen.entitysToIgnore.add(connectionList.get(0).getEntity());
					gen.otherEnts.add(connectionList.get(0).getEntity());

					gen.entitysToIgnore.add(connectionList.get(1).getEntity());
					gen.otherEnts.add(connectionList.get(1).getEntity());

				} else if (connectionList.get(0).getCardinalityType().equals("PP")
						&& connectionList.get(1).getCardinalityType().equals("PP")) {
					text += "ONE TO ONE BOTH PARTIAL " + '\n';

					String foreignKey = "";
					String pK = "    PRIMARY KEY (";
					
					
					_Attribute pk1 = connectionList.get(0).getEntity().getPrimaryKey();
					_Attribute pk2 = connectionList.get(1).getEntity().getPrimaryKey();
					List<_Attribute> toBeIgnored = new ArrayList<>();
					
					text += "CREATE TABLE " + relationName + '\n' + "( " + '\n';
					
					
					if(pk1.isCompositeIdentifier()) {
						
						int k = 1;
						for (int j = 0; j < pk1.getIdentifiers().size(); j++) {

							if (k + j >= pk1.getIdentifiers().size()) {
								text += "    " + pk1.getIdentifiers().get(j).getName() + " "
										+ pk1.getIdentifiers().get(j).getType() + " " + "NOT NULL" + '\n';
								toBeIgnored.add(pk1.getIdentifiers().get(j));
								
							} else {
								text += "    " + pk1.getIdentifiers().get(j).getName() + " "
										+ pk1.getIdentifiers().get(j).getType() + " " + "NOT NULL" + '\n';
								toBeIgnored.add(pk1.getIdentifiers().get(j));
							}
						}
					}
					else {
						text += "    " + pk1.getName() + " "
								+ pk1.getType() + " " + "NOT NULL" + '\n';
					}
					
					if(pk2.isCompositeIdentifier()) {
						
						int k = 1;
						for (int j = 0; j < pk2.getIdentifiers().size(); j++) {

							if (k + j >= pk2.getIdentifiers().size()) {
								text += "    " + pk2.getIdentifiers().get(j).getName() + " "
										+ pk2.getIdentifiers().get(j).getType() + " " + "NOT NULL" + '\n';
								
								toBeIgnored.add(pk2.getIdentifiers().get(j));
								
							} else {
								text += "    " + pk2.getIdentifiers().get(j).getName() + " "
										+ pk2.getIdentifiers().get(j).getType() + " " + "NOT NULL" + '\n';
								toBeIgnored.add(pk2.getIdentifiers().get(j));
								
							}
						}
					}
					else {
						text += "    " + pk2.getName() + " "
								+ pk2.getType() + " " + "NOT NULL " + '\n';
					}

					for (_Attribute attribute : relation.getAttributes()) {
						text += "    " + attribute.getName() + " " + attribute.getType() + " " + "NOT NULL " + '\n';
					}
					if (relation.getCompositeAttributes().size() > 0) {
						for (_CompositeAttribute comp : relation.getCompositeAttributes().keySet()) {
							for (_Attribute attribute : comp.getAdditionalAttributes()) {
								text += "    " + attribute.getName() + " " + attribute.getType() + " " + "NOT NULL "
										+ '\n';
							}
						}
					}
				
					
				
					if(pk1.isCompositeIdentifier()) {
						int k = 1;
						foreignKey += "    FOREIGN KEY (";
						for (int j = 0; j < pk1.getIdentifiers().size(); j++) {

							if (k + j >= pk1.getIdentifiers().size()) {
								pK += pk1.getIdentifiers().get(j).getName() + " ";
								
								foreignKey += identiferNames(pk1) + ")" + " REFERENCES "
										+ connectionList.get(0).getEntity().getName() + "(" + identiferNames(pk1)+ ") " + '\n';
								
								
							} else {
								pK += pk1.getIdentifiers().get(j).getName() + ", ";
							}
						}
					}
					else {
						foreignKey += "    FOREIGN KEY (";
						pK += connectionList.get(0).getEntity().getPrimaryKey().getName() + " ";
						foreignKey += connectionList.get(0).getEntity().getPrimaryKey().getName() + " REFERENCES "
								+ connectionList.get(0).getEntity().getName() + "("
								+ connectionList.get(0).getEntity().getPrimaryKey().getName() + ") " + '\n';
						
					}
					
					if(pk2.isCompositeIdentifier()) {
						int k = 1;
						foreignKey += "    FOREIGN KEY (";
						for (int j = 0; j < pk2.getIdentifiers().size(); j++) {

							if (k + j >= pk2.getIdentifiers().size()) {
								pK += pk2.getIdentifiers().get(j).getName() + ") "+ '\n';
								
								foreignKey += identiferNames(pk2) + ")" + " REFERENCES "
										+ connectionList.get(1).getEntity().getName() + "(" + identiferNames(pk2)+ ") " + '\n';
								
								
							} else {
								pK += ","+ pk2.getIdentifiers().get(j).getName() + ", ";
							}
						}
					}
					else {
						foreignKey += "    FOREIGN KEY (";
						pK += "," + connectionList.get(1).getEntity().getPrimaryKey().getName() + ") " + '\n'; // 
						foreignKey += connectionList.get(1).getEntity().getPrimaryKey().getName() + ") REFERENCES "
								+ connectionList.get(1).getEntity().getName() + "("
								+ connectionList.get(1).getEntity().getPrimaryKey().getName() + ") " + '\n';
					}
					
					text += pK;
					text += foreignKey;

					text += "); " + '\n';
					
					gen.entitysToIgnore.add(connectionList.get(0).getEntity());
					gen.entitysToIgnore.add(connectionList.get(1).getEntity());
					toBeIgnored.clear();
					
					
					
				} else {
					text += "ONE TO ONE 1 PARTIAL " + '\n';

					String pK = "    PRIMARY KEY (";
					String foreignKey = "";
					text += "CREATE TABLE " + relationName + '\n' + "( " + '\n';
					
					
					_Attribute pk1 = connectionList.get(0).getEntity().getPrimaryKey();
					_Attribute pk2 = connectionList.get(1).getEntity().getPrimaryKey();
					List<_Attribute> toBeIgnored = new ArrayList<>();
					
					
					
					if(pk1.isCompositeIdentifier()) {
						int k = 1;
						for (int j = 0; j < pk1.getIdentifiers().size(); j++) {

							if (k + j >= pk1.getIdentifiers().size()) {
								text += "    " + pk1.getIdentifiers().get(j).getName() + " "
										+ pk1.getIdentifiers().get(j).getType() + " " + "NOT NULL" + '\n';
								toBeIgnored.add(pk1.getIdentifiers().get(j));
								
							} else {
								text += "    " + pk1.getIdentifiers().get(j).getName() + " "
										+ pk1.getIdentifiers().get(j).getType() + " " + "NOT NULL" + '\n';
								toBeIgnored.add(pk1.getIdentifiers().get(j));
							}
						}
					}
					else {
						text += "    " + pk1.getName() + " "
								+ pk1.getType() + " " + "NOT NULL" + '\n';
					}
					
					if(pk2.isCompositeIdentifier()) {
						
						int k = 1;
						for (int j = 0; j < pk2.getIdentifiers().size(); j++) {

							if (k + j >= pk2.getIdentifiers().size()) {
								text += "    " + pk2.getIdentifiers().get(j).getName() + " "
										+ pk2.getIdentifiers().get(j).getType() + " " + "NOT NULL" + '\n';
								
								toBeIgnored.add(pk2.getIdentifiers().get(j));
								
							} else {
								text += "    " + pk2.getIdentifiers().get(j).getName() + " "
										+ pk2.getIdentifiers().get(j).getType() + " " + "NOT NULL" + '\n';
								toBeIgnored.add(pk2.getIdentifiers().get(j));
								
							}
						}
					}
					else {
						text += "    " + pk2.getName() + " "
								+ pk2.getType() + " " + "NOT NULL " + '\n';
					}
					

					for (_Attribute attribute : relation.getAttributes()) {
						text += "    " + attribute.getName() + " " + attribute.getType() + " " + "NOT NULL " + '\n';
					}
					if (relation.getCompositeAttributes().size() > 0) {
						for (_CompositeAttribute comp : relation.getCompositeAttributes().keySet()) {
							for (_Attribute attribute : comp.getAdditionalAttributes()) {
								text += "    " + attribute.getName() + " " + attribute.getType() + " " + "NOT NULL "
										+ '\n';
							}
						}
					}
					
					if(pk1.isCompositeIdentifier()) {
						int k = 1;
						foreignKey += "    FOREIGN KEY (";
						for (int j = 0; j < pk1.getIdentifiers().size(); j++) {

							if (k + j >= pk1.getIdentifiers().size()) {
								pK += pk1.getIdentifiers().get(j).getName() + " ";
								
								foreignKey += identiferNames(pk1) + ")" + " REFERENCES "
										+ connectionList.get(0).getEntity().getName() + "(" + identiferNames(pk1)+ ") " + '\n';
								
							} else {
								pK += pk1.getIdentifiers().get(j).getName() + ", ";
							}
						}
					}
					
					else {
						
						foreignKey += "    FOREIGN KEY (";
						pK += connectionList.get(0).getEntity().getPrimaryKey().getName() + " "; // 
						foreignKey += connectionList.get(0).getEntity().getPrimaryKey().getName() + ") REFERENCES "
								+ connectionList.get(0).getEntity().getName() + "("
								+ connectionList.get(0).getEntity().getPrimaryKey().getName() + ") " + '\n';
					}
					
					if(pk2.isCompositeIdentifier()) {
						int k = 1;
						foreignKey += "    FOREIGN KEY (";
						for (int j = 0; j < pk2.getIdentifiers().size(); j++) {

							if (k + j >= pk2.getIdentifiers().size()) {
								pK += pk2.getIdentifiers().get(j).getName() + ") "+ '\n';
								
								foreignKey += identiferNames(pk2) + ")" + " REFERENCES "
										+ connectionList.get(1).getEntity().getName() + "(" + identiferNames(pk2)+ ") " + '\n';
								
								
							} else {
								pK += ","+ pk2.getIdentifiers().get(j).getName() + ", ";
							}
						}
					}
					else {
						foreignKey += "    FOREIGN KEY (";
						pK += "," + connectionList.get(1).getEntity().getPrimaryKey().getName() + ") " + '\n'; // 
						foreignKey += connectionList.get(1).getEntity().getPrimaryKey().getName() + ") REFERENCES "
								+ connectionList.get(1).getEntity().getName() + "("
								+ connectionList.get(1).getEntity().getPrimaryKey().getName() + ") " + '\n';
					}
					
					text += pK;
					text += foreignKey;
					text += "); " + '\n';
					
					gen.entitysToIgnore.add(connectionList.get(0).getEntity());
					gen.entitysToIgnore.add(connectionList.get(1).getEntity());
					toBeIgnored.clear();	
				}
			} 
		}
		return text;
	}
}
