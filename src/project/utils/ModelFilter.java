package project.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import project.model.Model;
import project.model._Attribute;
import project.model._CompositeAttribute;
import project.model._Connection;
import project.model._Entity;
import project.model._Relation;

public class ModelFilter {
	private Model model;
	private ArrayList<Object> thingsToFix;
	private HashMap<_Connection, String> cardinalitys;
	
	
	private final String oneone = "OneToOne";
	private final String onemany = "OneToMany";
	private final String manymany = "ManyToMany";
	private final String nary = "Nary";

	
	public ModelFilter(Model model) {
		this.model = model;
		thingsToFix = new ArrayList<>();
		cardinalitys = new HashMap<>();
	}
	
	
	public boolean anythingToFix() {
		if (thingsToFix.size() > 0) {
			System.out.println(thingsToFix.size() +" things need to be fixed");
			return true;
		}
		return false;
	}
	public void relationChecker() {
		
		 boolean number_1 = false;
		 boolean character_2 = false;
		 boolean number_2 = false;
		
		for (_Relation rel : model.getRelations()) {
			if (rel.getConnection().size() < 2) {
				System.out.println("Relations Connection too low ");
				thingsToFix.add(rel);
			} else {
				cardinalitys.clear();
				
				if(rel.getCompositeAttributes().size() >0) {
					for(_CompositeAttribute attr :rel.getCompositeAttributes().keySet()) {
						if(rel.getCompositeAttributesList(attr).size()>0) {
						}
						
						else {
							System.out.println("Composite attribute has no attributes ");
							thingsToFix.add(attr);
						}
					}
				}
				
				for (_Connection connection : rel.getConnection()) {
					String cardinality = connection.getCard();
					cardinality = cardinality.toLowerCase();
					if (cardinality.matches("\\([a-z0-9],[a-z0-9]\\)")) {
						number_1 = false;
						number_2 = false;
						character_2 = false;
						// System.out.println(cardinality);
						String[] cardArray = cardinality.split(",");
						String part1 = cardArray[0]; // 004
						String part2 = cardArray[1]; // 034556
						//System.out.println(part1 + " Part one");
						//System.out.println(part2 + " Part two");
						Pattern pattern = Pattern.compile("[a-z0-9]");
						Matcher matcher = pattern.matcher(part1);
						if (matcher.find()) {
							part1 = matcher.group(0);
							//System.out.println(part1 + " part1");
						} else {
							thingsToFix.add(connection);
							System.out.println("card not appropriate ");
							continue;
						}

						matcher = pattern.matcher(part2);
						if (matcher.find()) {
							part2 = matcher.group(0);
							//System.out.println(part2 + " part2");
						} else {
							System.out.println("card not appropriate ");
							thingsToFix.add(connection);
							continue;
						}

						pattern = Pattern.compile("[0-9]");
						matcher = pattern.matcher(part1);
						if (matcher.find()) {
							number_1 = true;
						} else {
							System.out.println("Character not appropriate (partone)");
							thingsToFix.add(connection);
							continue;
						}

						matcher = pattern.matcher(part2);
						if (matcher.find()) {
							number_2 = true;
						} else {
							character_2 = true;
						}

						if (number_1 && number_2) {
							int number1 = Integer.parseInt(part1);
							int number2 = Integer.parseInt(part2);

							if (number2 >= number1) {
								if (number2 == 1 && number1 == 0) {
									cardinalitys.put(connection, "PP");
									connection.setCardinalityType("PP");
									continue;

								} else if (number2 == 1 && number1 == 1) {
									cardinalitys.put(connection, "TP");
									connection.setCardinalityType("TP");
									continue;
								}

							} else {
								System.out.println("Cardinality incorrect ");
								thingsToFix.add(connection);
								continue;
							}

						} else if (number_1 && character_2) {
							int number1 = Integer.parseInt(part1);
							if (number1 == 0) {
								cardinalitys.put(connection, "PPM");
								connection.setCardinalityType("PPM");
								continue;
							} else {
								cardinalitys.put(connection, "TPM");
								connection.setCardinalityType("TPM");
								continue;
							}

						}
					}

					else {
						System.out.println("card not appropriate");
						thingsToFix.add(connection);
						continue;
					}

				}

				if (cardinalitys.size() == rel.getConnection().size()) {
					int typeValue = 0;
					int multipleTypeValue = 0;

					for (_Connection c : cardinalitys.keySet()) {
						String cardType = c.getCardinalityType();
						if (cardType.equals("TP") || cardType.equals("PP")) {
							typeValue += 1;
						} else if (cardType.equals("TPM") || cardType.equals("PPM")) {
							multipleTypeValue += 1;
						}
					}

					if (typeValue > 0 && multipleTypeValue == 0 && rel.getConnection().size() < 3) {
						rel.setTypeOfRelationship(oneone);
					} else if (rel.getConnection().size() > 2) {
						rel.setTypeOfRelationship(nary);
					}

					else if (typeValue == 0 && multipleTypeValue > 0 && rel.getConnection().size() < 3) {
						rel.setTypeOfRelationship(manymany);
					}

					else if (typeValue > 0 && multipleTypeValue > 0 && rel.getConnection().size() < 3) {
						rel.setTypeOfRelationship(onemany);
					}
				}
			}
		}
	}
	
	public void entityChecker() {
		for (_Entity entity : model.getEnts()) {
			if (entity.attributes.size() == 0 && entity.getCompositeAttributes().size() == 0) {
				System.out.println(entity.getName()+" Entity missing attributes ");
				thingsToFix.add(entity);
				continue;
			} else {
				if (entity.isWeakEnt()) { // Seeing if weak ent conditions are met
					boolean itsFine = false;
					for (_Attribute attribute : entity.attributes) {
						if (attribute.isPrimaryKey()) {
							itsFine = true;
							break;
						}
					}
					if(entity.connections.size() == 0) {
						System.out.println(" WeakEnt missing owner ");
						thingsToFix.add(entity);
						continue;
					}

					if (!itsFine) {
						System.out.println(" WeakEnt has no composite primary key ");
						thingsToFix.add(entity);
						continue;
					}
				} else {
					int itsFin = 0;
					if (entity.getCompositeAttributes().size() > 0) { // Checking our composite attributes
						for (_CompositeAttribute comp : entity.getCompositeAttributes().keySet()) {
							if (comp.getAdditionalAttributes().size() > 0) {
							} else {
								itsFin += 1;
								System.out.println("Composite attribute has no attributes ");
								thingsToFix.add(comp);
								break;
							}
						}

						if (itsFin > 0) {
							System.out.println("Composite attribute has no attributes ");
							System.out.println(entity.getCompositeAttributes().size());
							thingsToFix.add(entity);
							continue;
						}

					}
					boolean itsFine = false;
					for (_Attribute attribute : entity.attributes) { // Checking if we have a primary key
						if (attribute.isPrimaryKey()) {
							itsFine = true;
							break;
						}
					}
					if (!itsFine) {
						System.out.println("PrimaryKey missing Ent ");

						thingsToFix.add(entity);
						continue;
					}
				}
			}
		}
	}
	public List<Object> getThingsToFix() {
		return thingsToFix;
	}

}
