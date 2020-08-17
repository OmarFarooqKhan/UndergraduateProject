package project.utils;

import java.util.ArrayList;
import java.util.List;

import project.model.Model;
import project.model._Attribute;
import project.model._CompositeAttribute;
import project.model._Connection;
import project.model._Entity;
import project.model._Relation;

public class EntityEvaluator extends ObjectEvaluator{
	Model model;

	public EntityEvaluator(Model model, SqlGenerator gen) {
		this.model = model;
		this.gen = gen;
	}
	
	public String evaluateGeneralEntitys(String text) {
		for (_Entity entity : model.getEnts()) {
			if (gen.otherEnts.contains(entity)) {
				continue;
			} else {
				String primaryKey = "";
				List<_Attribute> ignoreList = new ArrayList<>();
				text += "CREATE TABLE " + entity.getName() + '\n' + "( " + '\n';
				System.out.println("Entered");
				for (_Attribute attribute : entity.attributes) {

					if (attribute.isPrimaryKey() && attribute.isCompositeIdentifier()) {
						
						primaryKey += "    " + "PRIMARY KEY (";
						ignoreList.add(attribute);
						int n = 1;

						for (int j = 0; j < attribute.getIdentifiers().size(); j++) {

							text += "    " + attribute.getIdentifiers().get(j).getName() + " "
									+ attribute.getIdentifiers().get(j).getType() + " " + "NOT NULL " + '\n';

							if (n + j >= attribute.getIdentifiers().size()) {
								primaryKey += attribute.getIdentifiers().get(j).getName() + ")";
								ignoreList.add(attribute.getIdentifiers().get(j));

							} else {
								primaryKey += attribute.getIdentifiers().get(j).getName() + ", ";
								ignoreList.add(attribute.getIdentifiers().get(j));

							}
						}

					} else {
						continue;
					}

				}
				for (_Attribute attribute : entity.attributes) {
					if (ignoreList.contains(attribute)) {
						continue;
					}
					if (attribute.isPrimaryKey() && !attribute.isCompositeIdentifier()) {
						primaryKey += "    " + "PRIMARY KEY (";
						primaryKey += attribute.getName() + ")" + '\n';
						text += "    " + attribute.getName() + " " + attribute.getType() + " " + "NOT NULL " + '\n';
					} else {
						text += "    " + attribute.getName() + " " + attribute.getType() + " " + '\n';
					}
				}
				if (entity.getCompositeAttributes().size() > 0) {
					for (_CompositeAttribute comp : entity.getCompositeAttributes().keySet()) {
						for (_Attribute attribute : comp.getAdditionalAttributes()) {
							text += "    " + attribute.getName() + " " + attribute.getType() + " " + '\n';
						}
					}
				}

				text += primaryKey + '\n';
				text += "); " + '\n';
			}
		}
		return text;
	}

	public String evaluateWeakEntitys(String weakEntText) {
		String pk = "";
		String foreignkey = "";
		String cascade = "";
		for (_Entity entity : model.getEnts()) {
			if (entity.isWeakEnt()) {
				weakEntText += "CREATE TABLE " + entity.getName() + '\n' + "( " + '\n';

				for (_Attribute att : entity.getAttributes()) {
					if (att.isPrimaryKey()) {
						pk += "    " + "PRIMARY KEY(" + att.getName() + ", ";
						weakEntText += "    " + att.getName() + " " + att.getType() + " " + "NOT NULL " + '\n';

					} else {
						weakEntText += "    " + att.getName() + " " + att.getType() + " " + '\n';
					}
				}

				List<_Connection> connectionList = entity.getConnections();
				for (_Connection conn : connectionList) {
					_Relation relation = conn.getRelation();
					List<_Connection> entityList = relation.getConnection();
					int n = 1;

					for (int i = 0; i < entityList.size(); i++) {
						if (entityList.get(i).getEntity().isWeakEnt()) {
							n += 1;
							continue;
						}
					}
					for (int i = 0; i < entityList.size(); i++) {
						if (entityList.get(i).getEntity().isWeakEnt()) {
							continue;
						} else {
							foreignkey += "    FOREIGN KEY (";

							_Attribute attr = entityList.get(i).getEntity().getPrimaryKey();
							// Works for regular
							// entities with more than 1
							// connection
							if (attr.isCompositeIdentifier()) {
								int k = 1;
								for (int j = 0; j < attr.getIdentifiers().size(); j++) {

									if (k + j >= attr.getIdentifiers().size()) {
										weakEntText += "    " + attr.getIdentifiers().get(j).getName() + " "
												+ attr.getIdentifiers().get(j).getType() + " " + "NOT NULL" + '\n';
										pk += attr.getIdentifiers().get(j).getName() + ") " + '\n';
										foreignkey += attr.getIdentifiers().get(j).getName() + ")" + " REFERENCES "
												+ entityList.get(i).getEntity().getName() + "(" + attr.getName() + ") "
												+ '\n';
										cascade += "        ON DELETE CASCADE" + '\n';
										foreignkey += cascade;
									} else {
										weakEntText += "    " + attr.getIdentifiers().get(j).getName() + " "
												+ attr.getIdentifiers().get(j).getType() + " " + "NOT NULL" + '\n';
										pk += attr.getIdentifiers().get(j).getName() + ", ";
										foreignkey += attr.getIdentifiers().get(j).getName() + ")" + " REFERENCES "
												+ entityList.get(i).getEntity().getName() + "("
												+ attr.getIdentifiers().get(j).getName() + ") " + '\n';
										cascade += "        ON DELETE CASCADE" + '\n';
										foreignkey += cascade;
										foreignkey += "    FOREIGN KEY (";
									}
								}
							} else {

								weakEntText += "    " + attr.getName() + " " + attr.getType() + " " + "NOT NULL" + '\n';
								if (entityList.size() <= (i + n)) {
									pk += attr.getName() + ") " + '\n';
									foreignkey += attr.getName() + ")" + " REFERENCES "
											+ entityList.get(i).getEntity().getName() + "(" + attr.getName() + ") "
											+ '\n';
									cascade += "        ON DELETE CASCADE" + '\n';
									foreignkey += cascade;
								} else {
									pk += attr.getName() + ", ";
									foreignkey += attr.getName() + ")" + " REFERENCES "
											+ entityList.get(i).getEntity().getName() + "(" + attr.getName() + ") "
											+ '\n';
									cascade += "        ON DELETE CASCADE" + '\n';
									foreignkey += cascade;
								}
							}
						}

					}
					if (relation.getTypeOfRelationship().equals(oneone)
							|| relation.getTypeOfRelationship().equals(onemany)) {
						gen.relationsToIgnore.add(relation);
					}
				}
				weakEntText += pk;
				weakEntText += foreignkey;
				weakEntText += "); " + '\n';
				gen.otherEnts.add(entity);
			}
		}
		return weakEntText;
	}
}
