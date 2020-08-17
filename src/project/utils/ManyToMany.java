package project.utils;

import java.util.List;

import project.model.Model;
import project.model._Attribute;
import project.model._CompositeAttribute;
import project.model._Connection;
import project.model._Relation;

public class ManyToMany extends ObjectEvaluator {

	public ManyToMany(Model model) {
		this.model = model;
	}
	public String manyToManyCreation(String text) {
		for (_Relation relation : model.getRelations()) {
			String relationName = relation.getName();
			String relationType = relation.getTypeOfRelationship();
			if (relationType.equals(manymany)) {
				text += "MANY TO MANY " + '\n';

				List<_Connection> connectionList = relation.getConnection();
				String foreignKey = "";
				String pK = "    PRIMARY KEY(";

				_Attribute pk1 = connectionList.get(0).getEntity().getPrimaryKey();
				_Attribute pk2 = connectionList.get(1).getEntity().getPrimaryKey();

				text += "CREATE TABLE " + relationName + '\n' + "( " + '\n';

				if (pk1.isCompositeIdentifier()) {
					int k = 1;
					foreignKey += "    FOREIGN KEY (";
					for (int j = 0; j < pk1.getIdentifiers().size(); j++) {

						text += "    " + pk1.getIdentifiers().get(0).getName() + " "
								+ pk1.getIdentifiers().get(0).getType() + " NOT NULL" + " " + '\n';

						if (k + j >= pk1.getIdentifiers().size()) {
							pK += pk1.getIdentifiers().get(j).getName() + " ";

							foreignKey += identiferNames(pk1) + ")" + " REFERENCES "
									+ connectionList.get(0).getEntity().getName() + "(" + identiferNames(pk1) + ") "
									+ '\n';

						} else {
							pK += pk1.getIdentifiers().get(j).getName() + ", ";
						}
					}
				}

				else {

					foreignKey += "    FOREIGN KEY (";
					text += "    " + connectionList.get(0).getEntity().getPrimaryKey().getName() + " "
							+ connectionList.get(0).getEntity().getPrimaryKey().getType() + " NOT NULL" + " " + '\n';

					pK += connectionList.get(0).getEntity().getPrimaryKey().getName() + " "; //
					foreignKey += connectionList.get(0).getEntity().getPrimaryKey().getName() + ") REFERENCES "
							+ connectionList.get(0).getEntity().getName() + "("
							+ connectionList.get(0).getEntity().getPrimaryKey().getName() + ") " + '\n';
				}

				if (pk2.isCompositeIdentifier()) {
					int k = 1;
					foreignKey += "    FOREIGN KEY (";
					for (int j = 0; j < pk2.getIdentifiers().size(); j++) {

						if (k + j >= pk2.getIdentifiers().size()) {
							pK += pk2.getIdentifiers().get(j).getName() + ") " + '\n';

							foreignKey += identiferNames(pk2) + ")" + " REFERENCES "
									+ connectionList.get(1).getEntity().getName() + "(" + identiferNames(pk2) + ") "
									+ '\n';

						} else {
							pK += "," + pk2.getIdentifiers().get(j).getName() + ", ";
						}
					}
				} else {
					text += "    " + connectionList.get(1).getEntity().getPrimaryKey().getName() + " "
							+ connectionList.get(1).getEntity().getPrimaryKey().getType() + " NOT NULL" + " " + '\n';

					foreignKey += "    FOREIGN KEY (";
					pK += "," + connectionList.get(1).getEntity().getPrimaryKey().getName() + ") " + '\n'; //
					foreignKey += connectionList.get(1).getEntity().getPrimaryKey().getName() + ") REFERENCES "
							+ connectionList.get(1).getEntity().getName() + "("
							+ connectionList.get(1).getEntity().getPrimaryKey().getName() + ") " + '\n';
				}

				for (_Attribute attribute : relation.getAttributes()) {
					text += "    " + attribute.getName() + " " + attribute.getType() + " " + "NOT NULL " + '\n';
				}

				for (_CompositeAttribute comp : relation.getCompositeAttributes().keySet()) {
					for (_Attribute attribute : comp.getAdditionalAttributes()) {
						text += "    " + attribute.getName() + " " + attribute.getType() + " " + "NOT NULL " + '\n';

					}
				}
				text += pK;
				text += foreignKey;
				text += "); " + '\n';

			}
		}
		return text;
	}
}
