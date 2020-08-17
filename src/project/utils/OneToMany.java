package project.utils;

import java.util.ArrayList;
import java.util.List;

import project.model.Model;
import project.model._Attribute;
import project.model._CompositeAttribute;
import project.model._Connection;
import project.model._Entity;
import project.model._Relation;

public class OneToMany extends ObjectEvaluator {
	SqlGenerator gen;
	public OneToMany(Model model,SqlGenerator gen) {
		this.model = model;
		this.gen = gen;
	}

	public String oneToManyCreation(String text) {
		for (_Relation relation : model.getRelations()) {
			String relationType = relation.getTypeOfRelationship();
			if (relationType.equals(onemany)) {

				List<_Connection> connectionList = relation.getConnection();
				if (connectionList.get(0).getCardinalityType().equals("TP")
						&& connectionList.get(1).getCardinalityType().equals("TPM")
						|| connectionList.get(0).getCardinalityType().equals("TPM")
								&& connectionList.get(1).getCardinalityType().equals("TP")) {

					if (connectionList.get(0).getCardinalityType().equals("TP")) {

						text += "ONE TO MANY TOTAL " + '\n';

						gen.entitysToIgnore.add(connectionList.get(0).getEntity());
						gen.otherEnts.add(connectionList.get(0).getEntity());

						text += "CREATE TABLE " + connectionList.get(0).getEntity().getName() + '\n' + "( " + '\n';

						List<_Attribute> ignoreList = new ArrayList<>();
						String primaryKey = "    " + "PRIMARY KEY (";
						_Entity entity = connectionList.get(0).getEntity();

						for (_Attribute attribute : entity.attributes) {
							if (attribute.isPrimaryKey() && attribute.isCompositeIdentifier()) {
								ignoreList.add(attribute);
								int n = 1;

								for (int j = 0; j < attribute.getIdentifiers().size(); j++) {

									text += "    " + attribute.getIdentifiers().get(j).getName() + " "
											+ attribute.getIdentifiers().get(j).getType() + " " + "NOT NULL " + '\n';

									if (n + j >= attribute.getIdentifiers().size()) {
										primaryKey += attribute.getIdentifiers().get(j).getName();
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
								primaryKey += attribute.getName();
								text += "    " + attribute.getName() + " " + attribute.getType() + " " + "NOT NULL "
										+ '\n';
							} else {
								text += "    " + attribute.getName() + " " + attribute.getType() + " " + '\n';
							}
						}
						primaryKey += ")" + '\n';
						if (entity.getCompositeAttributes().size() > 0) {
							for (_CompositeAttribute comp : entity.getCompositeAttributes().keySet()) {
								for (_Attribute attribute : comp.getAdditionalAttributes()) {
									text += "    " + attribute.getName() + " " + attribute.getType() + " " + '\n';
								}
							}
						}

						for (_Attribute attribute : relation.getAttributes()) {
							text += "    " + attribute.getName() + " " + attribute.getType() + " " + '\n';
						}
						if (relation.getCompositeAttributes().size() > 0) {
							for (_CompositeAttribute comp : relation.getCompositeAttributes().keySet()) {
								for (_Attribute attribute : comp.getAdditionalAttributes()) {
									text += "    " + attribute.getName() + " " + attribute.getType() + " " + '\n';
								}
							}
						}

						String foreignKey = "";
						_Attribute mandatory = connectionList.get(1).getEntity().getPrimaryKey();

						if (mandatory.isCompositeIdentifier()) {
							int k = 1;
							foreignKey += "    FOREIGN KEY (";
							for (int j = 0; j < mandatory.getIdentifiers().size(); j++) {
								text += "    " + mandatory.getIdentifiers().get(j).getName() + " "
										+ mandatory.getIdentifiers().get(j).getType() + " NOT NULL " + '\n';
								if (k + j >= mandatory.getIdentifiers().size()) {
									// primaryKey += ", " + mandatory.getIdentifiers().get(j).getName() + ")" +'\n';

									foreignKey += identiferNames(mandatory) + ")" + " REFERENCES "
											+ connectionList.get(1).getEntity().getName() + "("
											+ identiferNames(mandatory) + ") " + '\n';

								} else {
									// primaryKey +=", "+mandatory.getIdentifiers().get(j).getName();
								}
							}
						} else {

							text += "    " + mandatory.getName() + " " + mandatory.getType() + " " + "NOT NULL " + '\n';
							// primaryKey += ", "+mandatory.getName() + ") " +'\n';
							foreignKey += "    FOREIGN KEY (";
							foreignKey += mandatory.getName() + ")" + " REFERENCES "
									+ connectionList.get(1).getEntity().getName() + "(" + mandatory.getName() + ") "
									+ '\n';
						}
						text += primaryKey;
						text += foreignKey;
						text += "); " + '\n';

					} else {
						text += "ONE TO MANY TOTAL " + '\n';

						gen.entitysToIgnore.add(connectionList.get(1).getEntity());
						gen.otherEnts.add(connectionList.get(1).getEntity());

						text += "CREATE TABLE " + connectionList.get(1).getEntity().getName() + '\n' + "( " + '\n';

						List<_Attribute> ignoreList = new ArrayList<>();
						String primaryKey = "    " + "PRIMARY KEY (";
						_Entity entity = connectionList.get(1).getEntity();

						for (_Attribute attribute : entity.attributes) {
							if (attribute.isPrimaryKey() && attribute.isCompositeIdentifier()) {
								ignoreList.add(attribute);
								int n = 1;

								for (int j = 0; j < attribute.getIdentifiers().size(); j++) {

									text += "    " + attribute.getIdentifiers().get(j).getName() + " "
											+ attribute.getIdentifiers().get(j).getType() + " " + "NOT NULL " + '\n';

									if (n + j >= attribute.getIdentifiers().size()) {
										primaryKey += attribute.getIdentifiers().get(j).getName();
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
								primaryKey += attribute.getName();
								text += "    " + attribute.getName() + " " + attribute.getType() + " " + "NOT NULL "
										+ '\n';
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
						primaryKey += ")" + '\n';

						for (_Attribute attribute : relation.getAttributes()) {
							text += "    " + attribute.getName() + " " + attribute.getType() + " " + '\n';
						}
						if (relation.getCompositeAttributes().size() > 0) {
							for (_CompositeAttribute comp : relation.getCompositeAttributes().keySet()) {
								for (_Attribute attribute : comp.getAdditionalAttributes()) {
									text += "    " + attribute.getName() + " " + attribute.getType() + " " + '\n';
								}
							}
						}

						String foreignKey = "";
						_Attribute mandatory = connectionList.get(0).getEntity().getPrimaryKey();

						if (mandatory.isCompositeIdentifier()) {
							int k = 1;
							foreignKey += "    FOREIGN KEY (";
							for (int j = 0; j < mandatory.getIdentifiers().size(); j++) {
								text += "    " + mandatory.getIdentifiers().get(j).getName() + " "
										+ mandatory.getIdentifiers().get(j).getType() + " NOT NULL " + '\n';
								if (k + j >= mandatory.getIdentifiers().size()) {
									// primaryKey += ", " + mandatory.getIdentifiers().get(j).getName() + ")" +'\n';

									foreignKey += identiferNames(mandatory) + ")" + " REFERENCES "
											+ connectionList.get(1).getEntity().getName() + "("
											+ identiferNames(mandatory) + ") " + '\n';

								} else {
									// primaryKey +=", "+mandatory.getIdentifiers().get(j).getName();
								}
							}
						} else {

							text += "    " + mandatory.getName() + " " + mandatory.getType() + " " + "NOT NULL " + '\n';
							// primaryKey += ", "+mandatory.getName() + ") " +'\n';
							foreignKey += "    FOREIGN KEY (";
							foreignKey += mandatory.getName() + ")" + " REFERENCES "
									+ connectionList.get(0).getEntity().getName() + "(" + mandatory.getName() + ") "
									+ '\n';
						}
						text += primaryKey;
						text += foreignKey;
						text += "); " + '\n';
					}
				}

				else { // Partial participation

					text += "ONE TO MANY PARTIAL " + '\n';

					if (connectionList.get(0).getCardinalityType().equals("PPM")) {

						List<_Attribute> ignoreList = new ArrayList<>();
						String primaryKey = "    " + "PRIMARY KEY (";
						_Entity entity = connectionList.get(1).getEntity();
						String foreignKey = "";

						gen.entitysToIgnore.add(entity);
						gen.otherEnts.add(entity);

						text += "CREATE TABLE " + entity.getName() + " " + '\n' + "( " + '\n';

						for (_Attribute attribute : entity.attributes) {
							if (attribute.isPrimaryKey() && attribute.isCompositeIdentifier()) {
								ignoreList.add(attribute);
								int n = 1;

								for (int j = 0; j < attribute.getIdentifiers().size(); j++) {

									text += "    " + attribute.getIdentifiers().get(j).getName() + " "
											+ attribute.getIdentifiers().get(j).getType() + " " + "NOT NULL " + '\n';

									if (n + j >= attribute.getIdentifiers().size()) {
										primaryKey += attribute.getIdentifiers().get(j).getName() + ") " + '\n';
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
								primaryKey += attribute.getName() + ")" + '\n';
								text += "    " + attribute.getName() + " " + attribute.getType() + " " + "NOT NULL "
										+ '\n';
							} else {
								text += "    " + attribute.getName() + " " + attribute.getType() + " " + '\n';
							}
						}

						for (_CompositeAttribute comp : entity.getCompositeAttributes().keySet()) {
							for (_Attribute attribute : comp.getAdditionalAttributes()) {
								text += "    " + attribute.getName() + " " + attribute.getType() + " " + '\n';
							}
						}

						for (_Attribute attribute : relation.getAttributes()) {
							text += "    " + attribute.getName() + " " + attribute.getType() + " " + "NOT NULL " + '\n';
						}

						for (_CompositeAttribute comp : relation.getCompositeAttributes().keySet()) {
							for (_Attribute attribute : comp.getAdditionalAttributes()) {
								text += "    " + attribute.getName() + " " + attribute.getType() + " " + "NOT NULL "
										+ '\n';
							}
						}

						if (connectionList.get(0).getEntity().getPrimaryKey().isCompositeIdentifier()) {
							_Attribute attribute = connectionList.get(0).getEntity().getPrimaryKey();
							int n = 1;

							foreignKey += "    FOREIGN KEY (";
							for (int j = 0; j < attribute.getIdentifiers().size(); j++) {

								text += "    " + attribute.getIdentifiers().get(j).getName() + " "
										+ attribute.getIdentifiers().get(j).getType() + " " + "NOT NULL " + '\n';

								if (n + j >= attribute.getIdentifiers().size()) {

									foreignKey += identiferNames(attribute) + ")" + " REFERENCES "
											+ connectionList.get(0).getEntity().getName() + "("
											+ identiferNames(attribute) + ") " + '\n';
								}
							}
						}

						else {
							text += "    " + connectionList.get(0).getEntity().getPrimaryKey().getName() + " "
									+ connectionList.get(0).getEntity().getPrimaryKey().getType() + " NOT NULL" + " "
									+ '\n';
							foreignKey += "    FOREIGN KEY ("
									+ connectionList.get(0).getEntity().getPrimaryKey().getName() + ")" + " REFERENCES "
									+ connectionList.get(0).getEntity().getName() + "("
									+ connectionList.get(0).getEntity().getPrimaryKey().getName() + ") " + '\n';
						}
						text += primaryKey;
						text += foreignKey;
						text += "); " + '\n';

					} else {

						if (connectionList.get(1).getCardinalityType().equals("PPM")) {
							List<_Attribute> ignoreList = new ArrayList<>();
							String primaryKey = "    " + "PRIMARY KEY (";
							_Entity entity = connectionList.get(0).getEntity();
							String foreignKey = "";

							gen.entitysToIgnore.add(entity);
							gen.otherEnts.add(entity);

							text += "CREATE TABLE " + entity.getName() + " " + '\n' + "( " + '\n';

							for (_Attribute attribute : entity.attributes) {
								if (attribute.isPrimaryKey() && attribute.isCompositeIdentifier()) {
									ignoreList.add(attribute);
									int n = 1;

									for (int j = 0; j < attribute.getIdentifiers().size(); j++) {

										text += "    " + attribute.getIdentifiers().get(j).getName() + " "
												+ attribute.getIdentifiers().get(j).getType() + " " + "NOT NULL "
												+ '\n';

										if (n + j >= attribute.getIdentifiers().size()) {
											primaryKey += attribute.getIdentifiers().get(j).getName() + ") " + '\n';
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
									primaryKey += attribute.getName() + ")" + '\n';
									text += "    " + attribute.getName() + " " + attribute.getType() + " " + "NOT NULL "
											+ '\n';
								} else {
									text += "    " + attribute.getName() + " " + attribute.getType() + " " + '\n';
								}
							}

							for (_CompositeAttribute comp : entity.getCompositeAttributes().keySet()) {
								for (_Attribute attribute : comp.getAdditionalAttributes()) {
									text += "    " + attribute.getName() + " " + attribute.getType() + " " + '\n';
								}
							}

							for (_Attribute attribute : relation.getAttributes()) {
								text += "    " + attribute.getName() + " " + attribute.getType() + " " + "NOT NULL "
										+ '\n';
							}

							for (_CompositeAttribute comp : relation.getCompositeAttributes().keySet()) {
								for (_Attribute attribute : comp.getAdditionalAttributes()) {
									text += "    " + attribute.getName() + " " + attribute.getType() + " " + "NOT NULL "
											+ '\n';
								}
							}

							if (connectionList.get(1).getEntity().getPrimaryKey().isCompositeIdentifier()) {
								_Attribute attribute = connectionList.get(1).getEntity().getPrimaryKey();
								int n = 1;

								foreignKey += "    FOREIGN KEY (";
								for (int j = 0; j < attribute.getIdentifiers().size(); j++) {

									text += "    " + attribute.getIdentifiers().get(j).getName() + " "
											+ attribute.getIdentifiers().get(j).getType() + " " + "NOT NULL " + '\n';

									if (n + j >= attribute.getIdentifiers().size()) {

										foreignKey += identiferNames(attribute) + ")" + " REFERENCES "
												+ connectionList.get(1).getEntity().getName() + "("
												+ identiferNames(attribute) + ") " + '\n';
									}
								}
							}

							else {
								text += "    " + connectionList.get(1).getEntity().getPrimaryKey().getName() + " "
										+ connectionList.get(1).getEntity().getPrimaryKey().getType() + " NOT NULL"
										+ " " + '\n';
								foreignKey += "    FOREIGN KEY ("
										+ connectionList.get(1).getEntity().getPrimaryKey().getName() + ")"
										+ " REFERENCES " + connectionList.get(1).getEntity().getName() + "("
										+ connectionList.get(1).getEntity().getPrimaryKey().getName() + ") " + '\n';
							}
							text += primaryKey;
							text += foreignKey;
							text += "); " + '\n';
						}

					}
				}
			}
		}
		return text;
	}
}
