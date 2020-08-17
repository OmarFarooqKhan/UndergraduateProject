package project.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;

import project.controller.Controller;
import project.model._Entity;
import project.model._Generalise;
import project.model._Subset;

public class BottomUpApproach {

	View view;
	Controller controller;
	JPanel jp;
	JLabel bottomUp;
	JButton reconnect = new JButton("Reconnect");
	private _DrawEnt mainEnt;
	private _DrawRelation mainRel;
	int tracker = 0;
	private boolean compAttributeMode = false;
	private boolean structuredDesign = false;
	boolean modeEntered = false;
	public ArrayList<_DrawAttribute> attributesToAggregate;
	public ArrayList<_DrawEnt> entsToGen;
	private JComboBox<_DrawRelation> relationsToConnectTo;
	private JComboBox<_DrawEnt> potEntities;
	private int stage = 0;
	protected JComboBox<_DrawAttribute> attrToSwitch;

	public BottomUpApproach(JPanel jp, View view, Controller controller) {
		this.jp = jp;
		this.view = view;
		this.controller = controller;
		attributesToAggregate = new ArrayList<>();
		potEntities = new JComboBox<_DrawEnt>();
		entsToGen = new ArrayList<>();
		attrToSwitch = new JComboBox<_DrawAttribute>();
		relationsToConnectTo = new JComboBox<>();
	}

	public void listAllOptions() {

		for (_DrawAttribute attribute : attributesToAggregate) {
			attribute.setSelected(false);
		}
		for (_DrawEnt entity : entsToGen) {
			entity.setEntToGeneralise(false);
			entity.resetColor();
		}
		view.getCanvasMenu().setEnterAggregation(false);
		view.getCanvasMenu().setEnterGeneralisation(false);

		potEntities.removeAllItems();
		entsToGen.clear();
		attributesToAggregate.clear();
		jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
		jp.removeAll();

		if (structuredDesign) {
			enterStages();
			exitStructuredDesign();
			jp.revalidate();
			jp.repaint();
			view.repaint();
			return;

		}
		bottomUp = new JLabel("Bottom up approach");
		jp.add(Box.createRigidArea(new Dimension(70, 0)));
		jp.add(bottomUp);
		jp.add(Box.createRigidArea(new Dimension(0, 20)));
		relationShipGen();
		jp.add(Box.createRigidArea(new Dimension(0, 20)));
		attributeAggregation();
		jp.add(Box.createRigidArea(new Dimension(0, 20)));
		addAnAttribute();
		jp.add(Box.createRigidArea(new Dimension(0, 20)));
		compAttributeAggregation();
		jp.add(Box.createRigidArea(new Dimension(0, 20)));
		entityGeneration();
		jp.add(Box.createRigidArea(new Dimension(0, 20)));
		generalise();
		jp.add(Box.createRigidArea(new Dimension(0, 20)));
		createCompositePrimaryKey(mainEnt);
		jp.add(Box.createRigidArea(new Dimension(0, 20)));
		enterStructuredDesign();

		jp.revalidate();
		jp.repaint();
		view.repaint();

	}

	public void setUp(_DrawEnt mainEnt, _DrawRelation mainRel) {
		this.mainEnt = mainEnt;
		this.mainRel = mainRel;
		listAllOptions();
	}

	private void goBack() {
		JButton returnToMenu = new JButton("Return");
		returnToMenu.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (_DrawAttribute attribute : attributesToAggregate) {
					attribute.setSelected(false);
				}
				for (_DrawEnt entity : entsToGen) {
					entity.setEntToGeneralise(false);
					entity.resetColor();
				}
				view.getCanvasMenu().setEnterAggregation(false);
				view.getCanvasMenu().setEnterGeneralisation(false);

				listAllOptions();
			}
		});
		jp.add(returnToMenu);

	}

	private void subsetAggregation() {
		JButton confirm = new JButton("Confirm");
		jp.add(Box.createRigidArea(new Dimension(15, 0)));

		jp.add(Box.createRigidArea(new Dimension(0, 20)));

		JLabel label = new JLabel("<html>Click the entities <br> you want to Generalise</html>");
		label.setBackground(Color.white);
		confirm.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (entsToGen.size() > 0) {
					potEntities.removeAllItems();
					_DrawEnt ent = controller.createADrawEnt();
					_Generalise gen = new _Generalise(ent.getEntity());
					_DrawGeneralisation generalisation = new _DrawGeneralisation(ent,gen);
					controller.addGeneralisation(generalisation);
					ent.setLocation(new Point(400, 250));
					for (_DrawEnt entity : entsToGen) {
						entity.setEntToGeneralise(false);
						entity.resetColor();
						_Subset sub = new _Subset(entity.getEntity(), gen);
						_DrawSubset subset = new _DrawSubset(entity, generalisation,sub);
						generalisation.getGen().addSubset(sub);
						generalisation.getSubsets().add(subset);
					}
					jp.remove(confirm);
					jp.repaint();
					transferAttribute(entsToGen, generalisation);
					view.getCanvasMenu().setEnterGeneralisation(false);
					reconnectConnections(ent);
					view.getCanvas().repaint();
					//
				}

			}
		});
		jp.add(label);

		confirm.setEnabled(true);

		jp.add(Box.createRigidArea(new Dimension(0, 20)));

		jp.add(confirm);
		jp.add(Box.createRigidArea(new Dimension(0, 20)));

		goBack();

	}

	private void generalise() {
		JButton generalise = new JButton("Generalise");
		JButton transform = new JButton("Transform hierarchy");
		
		transform.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				controller.transformGeneralisationHeirachys();
				listAllOptions();
			}
		});

		generalise.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (controller.getDrawEnts().size() > 0) {
					jp.removeAll();
					entsToGen.clear();
					view.getCanvasMenu().setEnterGeneralisation(true);
					subsetAggregation();

					jp.revalidate();
					jp.repaint();
					view.getCanvas().repaint();
				}

			}
		});
		generalise.setEnabled(false);
		if (controller.getDrawEnts().size() > 0) {
			generalise.setEnabled(true);
		}
		
		transform.setEnabled(false);

		if(controller.getGeneralisations().size()>0) {
			transform.setEnabled(true);
		}
		jp.add(generalise);
		jp.add(Box.createRigidArea(new Dimension(0, 20)));

		jp.add(transform);
		jp.repaint();
	}

	private void generate() {
		JButton generate = new JButton("Generate Entity");
		generate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (attributesToAggregate.size() > 0) {
					_DrawEnt entity = controller.createADrawEnt();
					entity.setLocation(400, 450);
					for (_DrawAttribute attribute : attributesToAggregate) {
						attribute.setSelected(false);
						if (controller.getAttributes().contains(attribute)) {
							attribute.unhide();
							entity.addAttribute(attribute, entity.getBox());

						}
						controller.getAttributes().remove(attribute);
					}
					view.getCanvas().repaint();
					listAllOptions();
				}

			}
		});
		generate.setEnabled(false);
		if (attributesToAggregate.size() > 0) {
			generate.setEnabled(true);
		}
		jp.add(generate);
		jp.add(Box.createRigidArea(new Dimension(0, 20)));

		goBack();
		jp.repaint();
	}

	private void confirmButton() {
		jp.removeAll();
		jp.setLayout(new FlowLayout());
		JButton confirm = new JButton("Confirm");
		JLabel note = new JLabel("Click on attributes");

		confirm.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (attributesToAggregate.size() > 0) {

					chooseBetween();
					generate();
				}
			}
		});
		jp.add(note);
		jp.add(Box.createRigidArea(new Dimension(0, 20)));
		jp.add(confirm);
		jp.add(Box.createRigidArea(new Dimension(0, 20)));
		addAnAttribute();
		jp.add(Box.createRigidArea(new Dimension(0, 20)));
		goBack();
		jp.revalidate();
		jp.repaint();
	}

	private void compAttributeAggregation() {
		JButton compAggregate = new JButton("Composite attribute Aggregation");
		compAggregate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				attributesToAggregate.clear();
				view.getCanvasMenu().setEnterAggregation(true);
				compAttributeMode = true;
				confirmButton();
				// TODO Auto-generated method stub

			}
		});

		compAggregate.setEnabled(false);

		if (controller.getDrawEnts().size() + controller.getAttributes().size() > 0) {
			boolean moveOn = false;
			for (_DrawEnt ent : controller.getDrawEnts()) {
				if ((ent.attributes.size() + controller.getAttributes().size()) > 1) {
					moveOn = true;
					break;
				}
			}
			compAggregate.setEnabled(moveOn);
		} else if (controller.getDrawRelations().size() + controller.getAttributes().size() > 0) {
			boolean moveOn = false;
			for (_DrawRelation rel : controller.getDrawRelations()) {
				if (rel.getAttr().size() + controller.getAttributes().size() > 1) {
					moveOn = true;
					break;
				}
			}
			compAggregate.setEnabled(moveOn);
		}
		if (mainEnt != null) {

			if (mainEnt.attributes.size() + controller.getAttributes().size() > 1) {
				compAggregate.setEnabled(true);
			} else {
				compAggregate.setEnabled(false);
			}
		} else if (mainRel != null) {
			if (mainRel.getAttr().size() + controller.getAttributes().size() > 1) {
				compAggregate.setEnabled(true);
			} else {
				compAggregate.setEnabled(false);
			}
		}
		jp.add(compAggregate);
	}

	private void attributeAggregation() {
		JButton aggregate = new JButton("Attribute Aggregation");
		aggregate.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				attributesToAggregate.clear();
				view.getCanvasMenu().setEnterAggregation(true);
				confirmButton();
				// TODO Auto-generated method stub

			}
		});
		jp.add(aggregate);
		aggregate.setEnabled(false);

		if (controller.getAttributes().size() > 0 || controller.getCompAttr().size() > 0) {
			aggregate.setEnabled(true);
		}
		if (mainEnt != null) {
			if (mainEnt.attributes.size() > 1) {
				aggregate.setEnabled(true);
			}
		} else if (mainRel != null) {
			if (mainRel.getAttr().size() > 1) {
				aggregate.setEnabled(true);
			}
		}
	}

	private void addAnAttribute() {
		JButton attribute = new JButton("Add An attribute");
		attribute.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_DrawAttribute attr = controller.addAttribute();
				attr.hideLine();
				controller.addAttribute(attr);
				listAllOptions();
				// TODO Auto-generated method stub
			}
		});
		jp.add(attribute);
	}

	private void transferAttribute(List<_DrawEnt> generalised, _DrawGeneralisation gen) {
		JButton attributeSwitch = new JButton("Transfer attribute");
		attributeSwitch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (generalised.size() > 0) {
					attrToSwitch.removeAllItems();

					for (_DrawEnt ent : entsToGen) {
						for (_DrawAttribute attr : ent.attributes.keySet()) {
							attrToSwitch.addItem(attr);

						}
						for (_DrawAttribute attr : ent.compAttribute.keySet()) {
							attrToSwitch.addItem(attr);
						}
					}

					jp.removeAll();
					
					JLabel from = new JLabel(" Attribute: ");

					jp.add(from);
					JLabel entity = new JLabel("Selected entity: ");

					jp.add(attrToSwitch);

					jp.add(Box.createRigidArea(new Dimension(5, 0)));
					jp.add(entity);

					transfer(gen);					
					reconnectConnections(gen.getEntity());
					goBack();
					jp.revalidate();
					jp.repaint();
				} else {
					attributeSwitch.setEnabled(false);
					jp.revalidate();
					jp.repaint();
				}
			}
		});
		attributeSwitch.setEnabled(false);
		for(_DrawEnt ent :entsToGen) {
			if(ent.attributes.size() > 0 || ent.compAttribute.size()>0) {
				attributeSwitch.setEnabled(true);
				break;
			}
		}
		jp.add(attributeSwitch);
		jp.revalidate();
		jp.repaint();
	}
	
	private void createCompositePrimaryKey(_DrawEnt ent) {
		JButton compositeIdentifier = new JButton("Composite Identifier");
		JButton confirm = new JButton("Confirm");
		confirm.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				boolean ignore = false;
				for(_DrawAttribute attribute : getAttributesToAggregate()) {
					if(ent.attributes.containsKey(attribute)) {
					}
					else {
						ignore = true;
					}
				}
				if(ignore) {
					for(_DrawAttribute attribute :	view.getCanvasMenu().bu.getAttributesToAggregate()){
						attribute.setSelected(false);
					}
					view.getCanvasMenu().setEnterAggregation(false);

					listAllOptions();
				}
				else {
					_DrawAttribute attribute = controller.addAttribute();					
					attribute.setCompositeIdentifier(true);
					attribute.selected();
					
					List<_DrawAttribute> identifiers = attribute.getCompositeIdentifiers();

					for(_DrawAttribute IDENT :	getAttributesToAggregate()){
						identifiers.add(IDENT);
						attribute.getAttribute().getIdentifiers().add(IDENT.getAttribute());

						
					}
					
					mainEnt.addAttribute(attribute, mainEnt.getBox());
					view.getCanvasMenu().setEnterAggregation(false);
					listAllOptions();
				}
			}
		});
		
		compositeIdentifier.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				jp.removeAll();
				jp.setLayout(new FlowLayout());
				view.getCanvasMenu().setEnterAggregation(true);
				jp.add(confirm);
				goBack();
				jp.revalidate();
				jp.repaint();
			}
		});
		
		
		compositeIdentifier.setEnabled(false);
		if(ent!=null) {
		if(ent.attributes.size() > 0 ) {
			
			for(_DrawAttribute attribute: ent.attributes.keySet()) {
				if(attribute.isPrimary() || attribute.isCompositeIdentifier()) {
					jp.add(compositeIdentifier);
					return;
				}
			}
			compositeIdentifier.setEnabled(true);
		}
		}
		jp.add(compositeIdentifier);
	}

	private void transfer(_DrawGeneralisation gen) {
		JButton transfer = new JButton("Transfer");
		
		jp.setLayout(new FlowLayout());
		jp.add(transfer);
		jp.revalidate();
		jp.repaint();
		transfer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.removeASpecificAttribute((_DrawAttribute) attrToSwitch.getSelectedItem());
				_DrawEnt picked = gen.getEntity();

				if (attrToSwitch.getSelectedItem() instanceof _DrawCompositeAttribute) {
					if (picked != null) {
						picked.addCompAttr((_DrawCompositeAttribute) attrToSwitch.getSelectedItem());
						attrToSwitch.removeItem((_DrawCompositeAttribute) attrToSwitch.getSelectedItem());
						if(attrToSwitch.getItemCount() == 0) {
							transfer.setEnabled(false);
						}

					}
				} else if (attrToSwitch.getSelectedItem() instanceof _DrawAttribute) {
					if (picked != null) {
						picked.addAttribute((_DrawAttribute) attrToSwitch.getSelectedItem(), picked.getBox());
						attrToSwitch.removeItem((_DrawAttribute) attrToSwitch.getSelectedItem());
						if(attrToSwitch.getItemCount() == 0) {
							transfer.setEnabled(false);
						}

					}
				}
				jp.repaint();
				view.getCanvas().repaint();

			}
		});
	}

	private void enteredEntity() {
		jp.removeAll();
		jp.setLayout(new FlowLayout());
		JComboBox<_DrawEnt> potEntities = new JComboBox<_DrawEnt>(); // Used to show Potential entities
		JButton confirmChoice = new JButton("Confirm Choice");
		potEntities.removeAllItems();
		for (_DrawEnt ent : controller.getDrawEnts()) {
			potEntities.addItem(ent);
		}
		confirmChoice.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_DrawEnt ent = (_DrawEnt) potEntities.getSelectedItem();
				int sameNumber = -1;
				if (compAttributeMode) {
					int spacer = 0;
					_DrawCompositeAttribute comp = controller.addCompAttribute();
					ent.addCompAttr(comp);
					for (_DrawAttribute attribute : attributesToAggregate) {
						int x = comp.getLocation().x;
						int y = comp.getLocation().y;
						spacer += 25;
						if (ent.attributes.containsKey(attribute)) { // Checks to see if we are already connected to an
																		// entity with a specific attribute
							// Used for composite attribute creation
							attribute.setSelected(false);

							x += spacer;
							Point point = new Point(x, y);
							controller.removeASpecificAttribute(attribute);
							comp.addExistingAttributes(attribute, point);
						} else {
							if (controller.getAttributes().contains(attribute)) {
								attribute.unhide();
								attribute.setSelected(false);
								x += spacer;
								Point point = new Point(x, y);
								controller.removeASpecificAttribute(attribute);
								comp.addExistingAttributes(attribute, point);
							} else {
								attribute.setSelected(false);
							}
						}
					}
					listAllOptions();
					return;
				}
				for (_DrawAttribute attribute : attributesToAggregate) {
					if (ent.attributes.containsKey(attribute)) { // Checks to see if we are already connected to an
																	// entity with a specific attribute
																	// Used for composite attribute creation
						attribute.setSelected(false);
						sameNumber += 1;
						continue;
					} else {
						if (controller.getAttributes().contains(attribute)) {
							attribute.unhide();
							attribute.setSelected(false);
							controller.removeASpecificAttribute(attribute);
							ent.addAttribute(attribute, ent.getBox());
						} else {
							attribute.setSelected(false);
						}
					}

				}

				if (sameNumber == attributesToAggregate.size() - 1) { // create comp
					int spacer = 0;
					_DrawCompositeAttribute comp = controller.addCompAttribute();
					ent.addCompAttr(comp);
					for (_DrawAttribute attribute : attributesToAggregate) {

						int x = comp.getLocation().x;
						int y = comp.getLocation().y;
						spacer += 25;
						x += spacer;
						Point point = new Point(x, y);
						controller.removeASpecificAttribute(attribute);
						comp.addExistingAttributes(attribute, point);
						attribute.setSelected(false);
					}
				}
				listAllOptions();

			}
		});
		jp.add(potEntities);
		jp.add(Box.createRigidArea(new Dimension(0, 20)));
		jp.add(confirmChoice);
		jp.revalidate();
		jp.repaint();

	}

	private void enteredRelation() {
		jp.removeAll();
		jp.setLayout(new FlowLayout());
		JButton confirmChoice = new JButton("Confirm Choice");
		JComboBox<_DrawRelation> potRelations = new JComboBox<_DrawRelation>(); // Used to show Potential entities
		potRelations.removeAllItems();
		for (_DrawRelation ent : controller.getDrawRelations()) {
			potRelations.addItem(ent);
		}
		confirmChoice.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_DrawRelation rel = (_DrawRelation) potRelations.getSelectedItem();
				int sameNumber = 0;

				if (compAttributeMode) {
					int spacer = 0;
					_DrawCompositeAttribute comp = controller.addCompAttribute();
					rel.addCompAttribute(comp);
					for (_DrawAttribute attribute : attributesToAggregate) {
						int x = comp.getLocation().x;
						int y = comp.getLocation().y;
						spacer += 25;
						if (rel.getCompAttr().containsKey(attribute)) { // Checks to see if we are already connected to
																		// an entity with a specific attribute
							// Used for composite attribute creation
							attribute.setSelected(false);
							rel.getAttr().remove(attribute);
							rel.getCompAttr().remove(attribute);

							x += spacer;
							Point point = new Point(x, y);
							comp.addExistingAttributes(attribute, point);
						} else {
							if (controller.getAttributes().contains(attribute)) {
								attribute.unhide();
								attribute.setSelected(false);
								x += spacer;
								Point point = new Point(x, y);
								controller.removeASpecificAttribute(attribute);
								comp.addExistingAttributes(attribute, point);
							} else {
								attribute.setSelected(false);
							}
						}
					}
					listAllOptions();
					return;
				}
				for (_DrawAttribute attribute : attributesToAggregate) {
					if (rel.getAttr().containsKey(attribute)) {
						sameNumber++;
						continue;
					} else {
						if (controller.getAttributes().contains(attribute)) {
							attribute.unhide();
							attribute.setSelected(false);
							rel.addAttribute(attribute);
						} else {
							attribute.setSelected(false);
						}
					}

				}

				if (sameNumber == attributesToAggregate.size()) {
					int spacer = 0;
					_DrawCompositeAttribute comp = controller.addCompAttribute();
					rel.addCompAttribute(comp);
					for (_DrawAttribute attribute : attributesToAggregate) {

						int x = comp.getLocation().x;
						int y = comp.getLocation().y;
						spacer += 25;
						x += spacer;
						Point point = new Point(x, y);
						comp.addExistingAttributes(attribute, point);
						rel.getAttr().remove(attribute);
					}
				}
				listAllOptions();

			}
		});
		jp.add(potRelations);
		jp.add(Box.createRigidArea(new Dimension(0, 20)));
		jp.add(confirmChoice);
		jp.revalidate();
		jp.repaint();
	}

	private void chooseBetween() {

		jp.removeAll();
		JButton ent = new JButton("Entity");
		JButton rel = new JButton("Relation");

		ent.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				view.getCanvasMenu().setEnterAggregation(false);
				enteredEntity();

				// TODO Auto-generated method stub

			}
		});

		rel.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				view.getCanvasMenu().setEnterAggregation(false);
				enteredRelation();
				// TODO Auto-generated method stub

			}
		});
		if (controller.getDrawEnts().size() == 0) {
			ent.setEnabled(false);
		}
		jp.add(ent);
		jp.add(Box.createRigidArea(new Dimension(0, 40)));

		if (controller.getDrawRelations().size() == 0) {
			rel.setEnabled(false);
		}

		jp.add(rel);

		jp.add(Box.createRigidArea(new Dimension(0, 40)));

		jp.revalidate();
		jp.repaint();
	}

	private void entityGeneration() {
		JButton generation = new JButton("Entity generation");
		generation.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_Entity entity = new _Entity("EMPTY_" + tracker, UUID.randomUUID().toString());
				_DrawEnt ent = new _DrawEnt(entity);
				tracker++;
				controller.unfocusAll();
				ent.setFocused(true);
				controller.addEnt(entity);
				controller.addDrawEnt(ent);
				listAllOptions();
			}
		});

		jp.add(generation);
	}

	private void relationshipGenTernary(JComboBox<_DrawEnt> entBox, JComboBox<_DrawEnt> entBox2) {
		JComboBox<_DrawEnt> entBox3 = new JComboBox<>();
		JButton relGen = new JButton("Ternary relation");
		JButton confirm = new JButton("Confirm");
		for (_DrawEnt ent : controller.getDrawEnts()) {
			entBox3.addItem(ent);
		}
		relGen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				jp.removeAll();
				jp.setLayout(new FlowLayout());
				jp.add(entBox);
				jp.add(Box.createRigidArea(new Dimension(0, 50)));
				jp.add(entBox2);
				jp.add(Box.createRigidArea(new Dimension(0, 50)));
				jp.add(entBox3);
				jp.add(Box.createRigidArea(new Dimension(0, 50)));
				jp.add(confirm);
				jp.add(Box.createRigidArea(new Dimension(0, 50)));
				goBack();
				jp.revalidate();
				jp.repaint();

			}
		});

		confirm.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				_DrawEnt ent1 = (_DrawEnt) entBox.getSelectedItem();
				_DrawEnt ent2 = (_DrawEnt) entBox2.getSelectedItem();
				_DrawEnt ent3 = (_DrawEnt) entBox3.getSelectedItem();
				if (ent1 != ent2 && ent2 != ent3 && ent1 != ent3) {
					_DrawRelation relation = controller.createADrawRelation();
					relation.setPosition(ent1.x, ent1.y + 100); // Add our relation in first and connect it to our main
																// entity.
					controller.addAEntityConnection(ent1, relation);

					controller.addAEntityConnectionEnd(ent2, relation);

					controller.addAEntityConnectionEnd(ent3, relation);

					listAllOptions();
				}

			}
		});
		jp.add(relGen);

	}

	private void relationShipGen() {
		JButton relGen = new JButton("Relationship generation");
		JButton confirm = new JButton("Confirm");
		JComboBox<_DrawEnt> entBox = new JComboBox<_DrawEnt>();
		JComboBox<_DrawEnt> entBox2 = new JComboBox<>();
		for (_DrawEnt ent : controller.getDrawEnts()) {
			entBox.addItem(ent);
		}
		for (_DrawEnt ent : controller.getDrawEnts()) {
			entBox2.addItem(ent);
		}

		relGen.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				jp.removeAll();
				jp.setLayout(new FlowLayout());
				if (controller.getDrawEnts().size() > 2) {
					relationshipGenTernary(entBox, entBox2);
				}
				jp.add(entBox);
				jp.add(Box.createRigidArea(new Dimension(0, 50)));
				jp.add(entBox2);
				jp.add(Box.createRigidArea(new Dimension(0, 50)));
				jp.add(confirm);
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				goBack();
				jp.revalidate();
				jp.repaint();

				// TODO add 2/3 combo Boxes to generate an entity between
			}
		});

		confirm.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_DrawEnt ent1 = (_DrawEnt) entBox.getSelectedItem();
				_DrawEnt ent2 = (_DrawEnt) entBox2.getSelectedItem();

				_DrawRelation relation = controller.createADrawRelation();
				relation.setPosition(ent1.x, ent1.y + 100); // Add our relation in first and connect it to our main
															// entity.
				controller.addAEntityConnection(ent1, relation);
				controller.addAEntityConnectionEnd(ent2, relation);
				listAllOptions();
			}
		});
		if (controller.getDrawEnts().size() < 1) {
			relGen.setEnabled(false);
		}
		jp.add(relGen);
	}

	public List<_DrawEnt> getEntsToGen() {
		return entsToGen;
	}

	private void reconnectConnections(_DrawEnt entity) {

		JButton confirm = new JButton("Confirm");

		JLabel label_1 = new JLabel("Reconnect from");
		
		JLabel label_2 = new JLabel("Reconnect to");
		
		relationsToConnectTo.removeAllItems();
		ArrayList<_DrawRelation> filteredList = new ArrayList<>();
		if(potEntities.getItemCount()==0) {
			potEntities.addItem(entity);
		}
		for (_DrawEnt ent : entsToGen) {
			for (_DrawRelation relation : entityList(ent)) {
				if (!filteredList.contains(relation)) {
					filteredList.add(relation);
					relationsToConnectTo.addItem(relation);
				}
			}
		}
	//	System.out.println("Size of gens: "+ controller.getGeneralisations().size());
	//	System.out.println("Filtered list: " + filteredList.size());
	//	System.out.println("Filtered ItemList: " + relationsToConnectTo.getItemCount());
		relationsToConnectTo.setSelectedItem(relationsToConnectTo.getItemCount());

		reconnect.setEnabled(false);
		if (filteredList.size() > 0) {
			reconnect.setEnabled(true);
		}

		confirm.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (entity != null) {

					_DrawRelation rel = (_DrawRelation) relationsToConnectTo.getSelectedItem();

					for (_DrawConnection connection : rel.getConnections()) {
						if (entsToGen.contains(connection.getStartEnt())) {
							controller.removeConnection(connection);
							_DrawEnt ent = (_DrawEnt) potEntities.getSelectedItem();
							connection.addSingleConnection(ent, ent.getBox(), rel); // Reinitialise
							ent.addConnectionE(ent.getBox(), connection); // add New connection
							controller.addConnection(connection); // register new connection
							rel.getConnections().add(connection);
							relationsToConnectTo.removeItem(rel);
							jp.repaint();
							if (relationsToConnectTo.getItemCount() == 0) {
								for (_DrawRelation relation : controller.getDrawRelations()) {
									relation.setCol(Color.white);
								}
								for (int i = 0; i < potEntities.getItemCount(); i++) {
									_DrawEnt reset = potEntities.getItemAt(i);
									reset.resetColor();
								}
								listAllOptions();
							}
							jp.repaint();
							view.getCanvas().repaint();
							break;
						}
					}

				}

			}
		});
		reconnect.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				jp.removeAll();
				jp.setLayout(new FlowLayout());

				jp.add(label_1);

				jp.add(relationsToConnectTo);
				jp.add(Box.createRigidArea(new Dimension(20, 0)));

				jp.add(label_2);
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				jp.add(potEntities);
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				jp.add(confirm);
				goBack();
				jp.revalidate();
				jp.repaint();

			}
		});
		jp.add(reconnect);
		jp.revalidate();
		jp.repaint();
	}

	private void enterStructuredDesign() {
		JButton sd = new JButton("<html>Enter Structured <br/> design</html>");
		sd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				structuredDesign = true;
				listAllOptions();
			}
		});
		jp.add(sd);

	}

	private void exitStructuredDesign() {
		JButton sd = new JButton("Exit Structured design");
		sd.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				structuredDesign = false;
				listAllOptions();

			}
		});
		jp.add(sd);
	}

	private void enterStages() {
		if (structuredDesign) {
			jp.removeAll();

			switch (stage) {

			case (0):
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				addAnAttribute();
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				advanceStage();
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				break;
			case (1):
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				addAnAttribute();
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				attributeAggregation();
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				compAttributeAggregation();
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				advanceStage();
				break;
			case (2):
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				addAnAttribute();
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				attributeAggregation();
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				compAttributeAggregation();
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				generalise();
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				advanceStage();
				break;

			
			case (3):
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				addAnAttribute();
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				attributeAggregation();
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				compAttributeAggregation();
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				generalise();
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				entityGeneration();
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				relationShipGen();
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				createCompositePrimaryKey(mainEnt);
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				advanceStage();
				break;
			}
			jp.revalidate();
			jp.repaint();
		}
	}

	private void advanceStage() {
		JButton confirm = new JButton("Advance Stage");
		confirm.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				stage += 1;
				if (stage == 4) {
					stage = 0;
				}
				listAllOptions();

			}
		});
		jp.add(confirm);
	}

	// Method to obtain a list of relations that we are connected to given an
	// entity.
	private List<_DrawRelation> entityList(_DrawEnt target) {
	//	System.out.println("Entity Connections: " + target.totalConnections().size());
		List<_DrawConnection> entitiesConnections = target.totalConnections(); // Know what relations we are connected
																				// to given
		// our entity
		List<_DrawRelation> entitiesRelation = new ArrayList<>(); // Find out how much other entities we are connected
		// to

		if (entitiesConnections.size() > 0) {
			for (_DrawConnection connection : entitiesConnections) {
				entitiesRelation.add(connection.getRelation()); // we do this by finding out what relations we are
				// connected to.
			}
		}

		return entitiesRelation;
	}

	
	
	public List<_DrawAttribute> getAttributesToAggregate(){
		return attributesToAggregate;
	}

}
