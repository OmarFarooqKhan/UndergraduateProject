package project.view;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.util.ArrayList;
import java.util.HashMap;
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
import project.model._Relation;
import project.model._Subset;

public class TopDownApproach {

	JPanel jp;
	JButton attributeDevelopment;
	JButton switchToTopDown;
	JButton expand;
	JButton goBack;
	JButton expandSplit;
	JButton expandConnected;
	JButton reconnect;
	JLabel topDown;
	View view;
	Controller controller;
	_DrawEnt mainEnt;
	_DrawRelation mainRel;
	private JComboBox<_DrawEnt> potEntities = new JComboBox<_DrawEnt>(); // Used to show Potential entities
	private JComboBox<_DrawAttribute> attrToSwitch = new JComboBox<_DrawAttribute>();
	private JComboBox<_DrawAttribute> targetAttributesToRefine = new JComboBox<_DrawAttribute>();
	private JComboBox<Object> potRelations = new JComboBox<Object>();
	private JComboBox<_DrawEnt> otherEnts = new JComboBox<_DrawEnt>();
	private HashMap<_DrawRelation, List<_DrawEnt>> ourTargets = new HashMap<>();
	private boolean structured = false;
	private int stage = 0;

	private int nameTrack = 0;

	public TopDownApproach(JPanel jp, View view, Controller controller) {
		this.jp = jp;
		this.view = view;
		this.controller = controller;
	}

	public void clearAndAdd(_DrawEnt e, _DrawRelation r) {
		this.mainEnt = e;
		this.mainRel = r;

		options();
	}

	private void basicActions() {
		JButton basic = new JButton("Add an entity");
		basic.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_Entity entity = new _Entity("EMPTY", UUID.randomUUID().toString());
				_DrawEnt ent = new _DrawEnt(entity);
				controller.unfocusAll();
				ent.setFocused(true);
				controller.addEnt(entity);
				controller.addDrawEnt(ent);
				options();
			}
		});
		basic.setEnabled(false);
		if (controller.getDrawEnts().size() == 0) {
			basic.setEnabled(true);
		}
		jp.add(basic);
		jp.revalidate();
		jp.repaint();
	}

	public void options() {
		for (_DrawAttribute attribute : view.getCanvasMenu().bu.attributesToAggregate) {
			attribute.setSelected(false);
		}
		view.getCanvasMenu().bu.getAttributesToAggregate().clear();
		view.getCanvasMenu().setEnterAggregation(false);
		jp.removeAll();
		jp.setLayout(new BoxLayout(jp, BoxLayout.Y_AXIS));
		jp.add(Box.createRigidArea(new Dimension(30, 0)));

		if (structured) {

			enterStages();
			exitSD();
			jp.revalidate();
			jp.repaint();
			view.repaint();
			return;
		}

		topDown = new JLabel("TOP DOWN APPROACH");
		jp.add(topDown);
		jp.add(Box.createRigidArea(new Dimension(70, 10)));

		addAttributeDevelopment();
		jp.add(Box.createRigidArea(new Dimension(30, 0)));

		transferAttribute();
		jp.add(Box.createRigidArea(new Dimension(30, 0)));

		uncorrelatedEntites(mainEnt);
		jp.add(Box.createRigidArea(new Dimension(30, 0)));
		alreadyConnected(mainEnt);
		jp.add(Box.createRigidArea(new Dimension(0, 20)));

		relationExpansion(mainRel);
		jp.add(Box.createRigidArea(new Dimension(0, 20)));

		relationEntityExpansion();
		jp.add(Box.createRigidArea(new Dimension(0, 20)));

		generalise(mainEnt);

		jp.add(Box.createRigidArea(new Dimension(0, 20)));

		basicActions();
		jp.add(Box.createRigidArea(new Dimension(0, 20)));
		enterStructuredDesign();
		jp.revalidate();
		jp.repaint();
		view.repaint();
		// relationExpansion();
	}

	private void enterStructuredDesign() {
		JButton strD = new JButton("Enter Structured Design");
		strD.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				structured = true;
				options();

			}
		});
		jp.add(strD);
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
				options();
			}
		});
		jp.add(confirm);

	}

	private void enterStages() {
		if (structured) {
			jp.removeAll();

			switch (stage) {

			case (0):
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				basicActions();
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				advanceStage();
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				break;
			case (1):
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				uncorrelatedEntites(mainEnt);
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				alreadyConnected(mainEnt);
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				advanceStage();
				break;
			case (2):
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				uncorrelatedEntites(mainEnt);
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				alreadyConnected(mainEnt);
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				generalise(mainEnt);
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				relationExpansion(mainRel);
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				relationEntityExpansion();
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				advanceStage();
				break;

			case (3):
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				uncorrelatedEntites(mainEnt);
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				alreadyConnected(mainEnt);
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				generalise(mainEnt);
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				relationExpansion(mainRel);
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				relationEntityExpansion();
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				addAttributeDevelopment();
				jp.add(Box.createRigidArea(new Dimension(30, 0)));
				transferAttribute();
				jp.add(Box.createRigidArea(new Dimension(30, 0)));
				break;
			}
			jp.revalidate();
			jp.repaint();
		}
	}

	private void exitSD() {
		JButton strD = new JButton("Exit Structured Design");
		strD.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				structured = false;
				stage = 0;
				options();

			}
		});
		jp.add(strD);

	}

	private void relationEntityExpansion() {

		JButton relationExpand = new JButton("Relation expansion");

		relationExpand.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (mainRel != null) {

					List<_DrawEnt> entitiesConnections = new ArrayList<>(); // en0 is start and en1 is end.
					for (_DrawConnection connection : mainRel.getConnections()) {
						entitiesConnections.add(connection.getStartEnt());
					}

					_DrawConnection toBeRemoved = null;
					for (_DrawConnection conn : mainRel.getConnections()) { // Remove existing connection first from
						// entity
						if (conn.getStartEnt().equals(entitiesConnections.get(1))) {
							entitiesConnections.get(1).removeConnection(conn);
							toBeRemoved = conn;
						}
					}
					if (mainRel.getConnections().contains(toBeRemoved)) { // Clean up old connections
						mainRel.getConnections().remove(toBeRemoved);
						controller.removeConnection(toBeRemoved);
					}

					_Entity en = new _Entity("DFLT-Exp", UUID.randomUUID().toString()); // Entity to place within our
					// relation expansion.
					_DrawEnt entity = new _DrawEnt(en);
					entity.setLocation(mainRel.x, mainRel.y + 100);

					_DrawConnection connection = new _DrawConnection(entity.getBox(), entity);

					connection.addRelation(mainRel);
					connection.addSingleConnection(entity, connection.getStartPos(), mainRel);

					entity.addConnection(entity.getBox(), connection);
					mainRel.addEntry(connection);
					controller.addConnection(connection);

					controller.addEnt(en);
					controller.addDrawEnt(entity);

					_DrawConnection conn2 = new _DrawConnection(entity.getSouthBox(), entity);
					_Relation rel = new _Relation("DFLT-Expand");
					_DrawRelation relation = new _DrawRelation(rel);
					relation.setPosition(entity.x, entity.y + 100);

					conn2.addRelation(relation);
					conn2.addSingleConnection(entity, conn2.getStartPos(), relation);

					entity.addConnectionE(entity.getSouthBox(), conn2);
					relation.addEntry(conn2);

					controller.addRelation(rel);
					controller.addDrawRel(relation);
					controller.addConnection(conn2);

					_DrawConnection conn3 = new _DrawConnection(entitiesConnections.get(1).getBox(),
							entitiesConnections.get(1));
					conn3.addSingleConnection(entitiesConnections.get(1), conn3.getStartPos(), relation);
					entitiesConnections.get(1).addConnection(entitiesConnections.get(1).getBox(), conn3);
					relation.addEntry(conn3);
					controller.addConnection(conn3);

					if (mainRel.getConnections().size() > 3) {
						for (int i = 4; i < mainRel.getConnections().size(); i++) {
							_DrawConnection conn = new _DrawConnection(entitiesConnections.get(1).getBox(),
									entitiesConnections.get(1));
							conn3.addSingleConnection(entitiesConnections.get(1), conn.getStartPos(), relation);
							entitiesConnections.get(1).addConnection(entitiesConnections.get(1).getBox(), conn);
							relation.addEntry(conn);
							controller.addConnection(conn);
						}

					}

					view.getCanvas().repaint();
				} else {
					relationExpand.setEnabled(false);
				}
			}
		});

		jp.add(relationExpand);
		relationExpand.setEnabled(false);
		if (mainRel != null) {
			relationExpand.setEnabled(true);
		}
		jp.repaint();

	}

	private void relationExpansion(_DrawRelation relation) {
		List<_DrawEnt> entitiesConnections = new ArrayList<>(); // Know what we are connected to given
		// our relation

		JButton relationExpand = new JButton("Parallel Expansion");

		JButton independant = new JButton("Independant Expansion");
		independant.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (_DrawConnection connection : relation.getConnections()) {
					entitiesConnections.add(connection.getStartEnt());
				}

				_Relation rel = new _Relation("DFLT");
				_DrawRelation relation = new _DrawRelation(rel);
				relation.setPosition(mainRel.x + 100, mainRel.y);

				controller.addRelation(rel);
				controller.addDrawRel(relation);

				for (int i = 0; i < entitiesConnections.size(); i++) {
					_DrawConnection additonalConnection = new _DrawConnection(entitiesConnections.get(i).getBox(),
							entitiesConnections.get(i));
					additonalConnection.addRelation(relation);
					additonalConnection.addSingleConnection(additonalConnection.getStartEnt(),
							additonalConnection.getStartPos(), relation);
					additonalConnection.getStartEnt().addConnection(additonalConnection.getStartPos(),
							additonalConnection); // add connection to our entity
					relation.addEntry(additonalConnection);
					controller.addConnection(additonalConnection);
				}

				_Relation rel2 = new _Relation("DFLT");
				_DrawRelation relation2 = new _DrawRelation(rel2);
				relation2.setPosition(mainRel.x - 50, mainRel.y);

				for (int i = 0; i < entitiesConnections.size(); i++) {
					_DrawConnection additonalConnection = new _DrawConnection(entitiesConnections.get(i).getBox(),
							entitiesConnections.get(i));
					additonalConnection.addRelation(relation2);
					additonalConnection.addSingleConnection(additonalConnection.getStartEnt(),
							additonalConnection.getStartPos(), relation2);
					additonalConnection.getStartEnt().addConnection(additonalConnection.getStartPos(),
							additonalConnection); // add connection to our entity
					relation2.addEntry(additonalConnection);
					controller.addConnection(additonalConnection);
				}
				controller.addRelation(rel2);
				controller.addDrawRel(relation2);
				controller.removeDrawRelation(mainRel);
				mainRel = null;
				options();

			}
		});
		JButton partition = new JButton("Constraint Expansion");
		partition.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub

			}
		});

		relationExpand.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (relation != null) {
					jp.removeAll();
					jp.setLayout(new FlowLayout());
					jp.add(Box.createRigidArea(new Dimension(0, 30)));
					jp.add(independant);
					jp.add(Box.createRigidArea(new Dimension(0, 30)));
					// jp.add(partition);
					// jp.add(Box.createRigidArea(new Dimension(0, 30)));
					goBack();
					jp.revalidate();
					jp.repaint();

				} else {
					relationExpand.setEnabled(false);
					jp.repaint();
				}
			}
		});

		jp.add(relationExpand);
		relationExpand.setEnabled(false);
		if (mainRel != null) {
			relationExpand.setEnabled(true);
		}
		jp.revalidate();
		jp.repaint();

		view.getCanvas().repaint();
	}

	private void uncorrelatedEntites(_DrawEnt ent) {
		expandSplit = new JButton("Split");
		jp.setLayout(new FlowLayout());

		expandSplit.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (ent != null) {

					_Entity en1 = new _Entity("DFLT-" + nameTrack, UUID.randomUUID().toString());
					_DrawEnt entity1 = new _DrawEnt(en1);
					entity1.setLocation(ent.x + 100, ent.y);

					controller.addEnt(en1);
					controller.addDrawEnt(entity1);

					nameTrack++;
					_Entity en = new _Entity("DFLT-" + nameTrack, UUID.randomUUID().toString());
					_DrawEnt entity = new _DrawEnt(en);
					entity.setLocation(ent.x - 150, ent.y);

					controller.addEnt(en);
					controller.addDrawEnt(entity);

					controller.removeDrawEnt(ent);
					nameTrack++;
					jp.revalidate();
					jp.repaint();
				}
			}
		});

		jp.add(expandSplit);
		if (mainEnt == null && mainRel == null || mainRel != null) {
			expandSplit.setEnabled(false);
		}
		jp.revalidate();
		jp.repaint();
	}

	private void addAttributeDevelopment() {
		attributeDevelopment = new JButton("Attribute development");
		attributeDevelopment.setFont(new Font("Arial", Font.PLAIN, 12));

		attributeDevelopment.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				targetAttributesToRefine.removeAllItems();
				potEntities.removeAllItems();
				if (mainEnt != null || mainRel != null) {
					jp.removeAll();
					attributeExpansion();
					if (mainEnt != null) {
						if (mainEnt.attributes.size() > 0) {
							if (mainEnt.attributes.size() > 0) {
								createCompositePrimaryKey(mainEnt);
							}
							attributeRefinement(true);
						} else {
							attributeRefinement(false);
						}
					} else {
						if (mainRel.getAttr().size() > 0) {
							attributeRefinement(true);
						} else {
							attributeRefinement(false);
						}
					}
					goBack();
				} else {
					attributeDevelopment.setSelected(false);
				}

			}
		});
		jp.add(attributeDevelopment);
		if (mainEnt == null && mainRel == null) {
			attributeDevelopment.setEnabled(false);
		}
	}

	// Method to update the Side Menu-bar
	private void goBack() {
		goBack = new JButton("Go back");
		goBack.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				for (_DrawEnt ent : controller.getDrawEnts()) {
					ent.resetColor();
				}
				for (_DrawRelation rel : controller.getDrawRelations()) {
					rel.setCol(Color.white);
				}
				if (controller.getGeneralisations().size() > 0) {
					ArrayList<_DrawGeneralisation> gentToRemove = new ArrayList<>();
					for (_DrawGeneralisation gen : controller.getGeneralisations()) {
						if (gen.getSubsets().size() == 0) {
							gentToRemove.add(gen);
						}
					}
					for (_DrawGeneralisation gen : gentToRemove) {
						controller.removeGeneralisation(gen);
					}
				}
				options();

			}
		});
		jp.add(goBack);
		jp.repaint();
	}

	private void attributeRefinement(boolean enabled) {
		JButton attributeRefinement = new JButton("Attribute Refinement");
		attributeRefinement.setFont(new Font("Arial", Font.PLAIN, 12));
		attributeRefinement.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				jp.removeAll();
				targetAttributesToRefine.removeAllItems();
				if (mainEnt != null) {
					for (_DrawAttribute addMe : mainEnt.attributes.keySet()) {
						targetAttributesToRefine.addItem(addMe);
					}

				} else {
					for (_DrawAttribute addMe : mainRel.getAttr().keySet()) {
						targetAttributesToRefine.addItem(addMe);
					}
				}
				jp.add(targetAttributesToRefine);
				expandOrSwap();
				goBack();
				jp.revalidate();
				jp.repaint();
			}
		});
		attributeRefinement.setEnabled(enabled);
		jp.add(attributeRefinement);
		jp.repaint();
	}

	private void expandOrSwap() {
		JButton changeToComp = new JButton("Change to Composite");
		JButton expandAttribute = new JButton("Expand attribute");
		changeToComp.setFont(new Font("Arial", Font.PLAIN, 12));
		expandAttribute.setFont(new Font("Arial", Font.PLAIN, 12));
		changeToComp.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				if (mainEnt != null) {
					_DrawAttribute attributeToSwitch = (_DrawAttribute) targetAttributesToRefine.getSelectedItem();
					mainEnt.attributes.remove(attributeToSwitch);
					targetAttributesToRefine.removeItem((_DrawAttribute) targetAttributesToRefine.getSelectedItem());
					_DrawCompositeAttribute attribute = new _DrawCompositeAttribute(controller.addCompAttri());
					mainEnt.addCompAttr(attribute);
					controller.removeFreeAttribute(attributeToSwitch);
				} else if (mainRel != null) {
					_DrawAttribute attributeToSwitch = (_DrawAttribute) targetAttributesToRefine.getSelectedItem();
					mainRel.getAttr().remove(attributeToSwitch);
					targetAttributesToRefine.removeItem((_DrawAttribute) targetAttributesToRefine.getSelectedItem());
					_DrawCompositeAttribute attribute = new _DrawCompositeAttribute(controller.addCompAttri());
					attribute.setLocation(attribute.getLocation().x, attribute.getLocation().y + 5);
					mainRel.addCompAttribute(attribute);
				}
				if (targetAttributesToRefine.getItemCount() == 0) {
					expandAttribute.setEnabled(false);
					changeToComp.setEnabled(false);
					jp.repaint();
				}

				view.getCanvas().repaint();

			}
		});
		expandAttribute.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (targetAttributesToRefine.getItemCount() > 0) {
					if (mainEnt != null) {
						if (targetAttributesToRefine.getSelectedItem() instanceof _DrawCompositeAttribute) {
							expandAttribute.setEnabled(false);
						} else {

							_DrawAttribute tobeadded = controller.addAttribute();
							tobeadded.setLocation(mainEnt.getCenterX() + 40, mainEnt.y);
							mainEnt.expandAttribute(tobeadded,
									mainEnt.attributes.get(targetAttributesToRefine.getSelectedItem()));
							targetAttributesToRefine.addItem(tobeadded);
						}

					} else {
						_DrawAttribute tobeadded = controller.addAttribute();
						mainRel.addAttribute(tobeadded);
						targetAttributesToRefine.addItem(tobeadded);

					}

					view.getCanvas().repaint();

				} else {
					expandAttribute.setEnabled(false);
				}
			}
		});
		if (mainEnt == null && mainRel == null) {
			expandAttribute.setEnabled(false);
		} else {
			if (mainEnt != null) {
				if (mainEnt.attributes.size() == 0 && mainEnt.compAttribute.size() == 0) {
					expandAttribute.setEnabled(false);
				}
			} else {
				if (mainRel.getCompAttr().size() == 0 && mainRel.getAttr().size() == 0) {
					expandAttribute.setEnabled(false);
				}
			}
		}
		jp.add(expandAttribute);
		jp.add(changeToComp);
		jp.repaint();

	}

	private void attributeExpansion() {
		JButton compAttributeDevelopment = new JButton(" Add Composite Attribute");
		JButton foreignKey = new JButton("Add a foreign Key");
		JButton attributeDevelopment = new JButton("Add Attribute");
		attributeDevelopment.setFont(new Font("Arial", Font.PLAIN, 12));
		compAttributeDevelopment.setFont(new Font("Arial", Font.PLAIN, 12));

		attributeDevelopment.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (mainEnt != null) {
					_DrawAttribute attribute = controller.addAttribute();
					mainEnt.addAttribute(attribute, mainEnt.getLocation());
					view.getCanvas().canvasToolBar().displayAttributes(mainEnt, null);
					jp.removeAll();
					attributeExpansion();
					if (mainEnt.attributes.size() > 0) {
						attributeRefinement(true);
					} else {
						attributeRefinement(false);
					}

					goBack();

				} else if (mainRel != null) {
					view.getCanvas().canvasToolBar().displayAttributes(null, mainRel);

					_DrawAttribute attribute = controller.addAttribute();
					controller.addAttributeRel(mainRel, attribute);
					mainRel.addAttribute(attribute);
					jp.removeAll();

					attributeExpansion();
					if (mainRel.getAttr().size() > 0) {
						attributeRefinement(true);
					} else {
						attributeRefinement(false);
					}
					goBack();

				}
				view.getCanvas().repaint();

			}
		});
		compAttributeDevelopment.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (mainEnt != null) {
					mainEnt.addCompAttr(controller.addCompAttribute());
					jp.removeAll();
					attributeExpansion();
					if (mainEnt.attributes.size() > 0) {
						attributeRefinement(true);
					} else {
						attributeRefinement(false);
					}
					goBack();

				} else {
					mainRel.addCompAttribute(controller.addCompAttribute());
					jp.removeAll();
					attributeExpansion();
					if (mainRel.getAttr().size() > 0) {
						attributeRefinement(true);
					} else {
						attributeRefinement(false);
					}
					goBack();

				}
				view.getCanvas().repaint();
			}
		});

		foreignKey.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (mainEnt != null && controller.getDrawEnts().size() > 1) {

					for (_DrawEnt entity : controller.getDrawEnts()) {
						if (entity.equals(mainEnt)) {
							continue;
						} else {
							if (entity.attributes.size() > 0 || entity.compAttribute.size() > 0) {
								potEntities.addItem(entity);
							}
						}
					}
					foreignKeyRules();
					// WORKHERE
				}
				// TODO Auto-generated method stub

			}
		});

		foreignKey.setEnabled(false);
		if (controller.getDrawEnts().size() > 1) {
			foreignKey.setEnabled(true);
		}
		jp.add(compAttributeDevelopment);
		jp.add(attributeDevelopment);
		// jp.add(foreignKey);
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
				for (_DrawAttribute attribute : view.getCanvasMenu().bu.getAttributesToAggregate()) {
					if (ent.attributes.containsKey(attribute)) {
					} else {
						ignore = true;
					}
				}
				if (ignore) {
					for (_DrawAttribute attribute : view.getCanvasMenu().bu.getAttributesToAggregate()) {
						attribute.setSelected(false);
					}
					view.getCanvasMenu().setEnterAggregation(false);

					options();
				} else {
					_DrawAttribute attribute = controller.addAttribute();
					attribute.setCompositeIdentifier(true);
					attribute.selected();
					List<_DrawAttribute> identifiers = attribute.getCompositeIdentifiers();

					for (_DrawAttribute IDENT : view.getCanvasMenu().bu.getAttributesToAggregate()) {
						identifiers.add(IDENT);
						attribute.getAttribute().getIdentifiers().add(IDENT.getAttribute());

					}

					mainEnt.addAttribute(attribute, mainEnt.getBox());
					view.getCanvasMenu().setEnterAggregation(false);
					options();
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
		if (ent.attributes.size() > 0) {

			for (_DrawAttribute attribute : ent.attributes.keySet()) {
				if (attribute.isPrimary() || attribute.isCompositeIdentifier()) {
					jp.add(compositeIdentifier);
					return;
				}
			}
			compositeIdentifier.setEnabled(true);
		}
		jp.add(compositeIdentifier);

	}

	private void foreignKeyRules() {
		jp.removeAll();
		jp.setLayout(new FlowLayout());
		JButton confirm = new JButton("Confirm");
		JLabel nameOfEntity = new JLabel("Name of the entity");
		JLabel chosenAttribute = new JLabel("Attribute to choose");

		jp.add(Box.createRigidArea(new Dimension(0, 20)));

		potEntities.addItemListener(new ItemListener() {

			@Override
			public void itemStateChanged(ItemEvent e) {
				_DrawEnt ent = (_DrawEnt) potEntities.getSelectedItem();
				targetAttributesToRefine.removeAllItems();
				if (ent != null && (ent.attributes.size() > 0 || ent.compAttribute.size() > 0)) {
					for (_DrawAttribute attr : ent.attributes.keySet()) {
						if (attr.isForeign()) {
			//				System.out.println(attr.isForeign() + "attribute was foreign");
							continue;
						} else {
							targetAttributesToRefine.addItem(attr);
						}
					}

					for (_DrawAttribute attr : ent.compAttribute.keySet()) {
						if (attr.isForeign()) {
							continue;
						} else {
							targetAttributesToRefine.addItem(attr);
						}
					}
					if (targetAttributesToRefine.getItemCount() > 0) {
						confirm.setEnabled(true);
					}
					jp.revalidate();
					jp.repaint();
				}
			}
		});
		confirm.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_DrawAttribute attribute = (_DrawAttribute) targetAttributesToRefine.getSelectedItem();
				if (targetAttributesToRefine.getSelectedItem() instanceof _DrawCompositeAttribute) {
					_DrawCompositeAttribute comp = (_DrawCompositeAttribute) targetAttributesToRefine.getSelectedItem();
					_DrawCompositeAttribute attributeToRef = new _DrawCompositeAttribute(controller.addCompAttri());
					for (_DrawAttribute ignore : comp.getOtherAttribs().keySet()) {

						_DrawAttribute newReference = controller.addAttribute();
						ignore.setName(ignore.getName());
						attributeToRef.setForeign(true);
						attributeToRef.getReferences().add(ignore);

						attributeToRef.addExistingAttributes(newReference, mainEnt.getLocation());

						mainEnt.addCompAttr(attributeToRef);
					}

				} else {
					_DrawAttribute attr = controller.addAttribute();
					attr.setForeign(true);
					attr.getAttribute().setForeignKey(true);
					attr.getReferences().add(attribute);
					mainEnt.addAttribute(attr, mainEnt.getBox());

				}
				options();

			}
		});

		for (ItemListener item : potEntities.getItemListeners()) {
			potEntities.removeItemListener(item);
		}
		_DrawEnt ent = (_DrawEnt) potEntities.getSelectedItem();
		if (ent != null) {
			for (_DrawAttribute attr : ent.attributes.keySet()) {
				if (attr.isForeign()) {
				//	System.out.println(attr.isForeign());
					continue;
				} else {
					targetAttributesToRefine.addItem(attr);
				}
			}

			for (_DrawAttribute attr : ent.compAttribute.keySet()) {
				if (attr.isForeign()) {
					continue;
				} else {
					targetAttributesToRefine.addItem(attr);
				}
			}
		}

		confirm.setEnabled(false);
		if (targetAttributesToRefine.getItemCount() > 0) {
			confirm.setEnabled(true);
		}
		jp.add(nameOfEntity);
		jp.add(Box.createRigidArea(new Dimension(20, 0)));
		jp.add(potEntities);
		jp.add(Box.createRigidArea(new Dimension(0, 30)));
		jp.add(Box.createRigidArea(new Dimension(15, 0)));
		jp.add(chosenAttribute);

		jp.add(Box.createRigidArea(new Dimension(30, 0)));

		jp.add(targetAttributesToRefine);
		jp.add(Box.createRigidArea(new Dimension(20, 0)));

		jp.add(confirm);
		jp.add(Box.createRigidArea(new Dimension(0, 40)));
		goBack();
		jp.revalidate();
		jp.repaint();
	}

	private void transferAttribute() {
		JButton attributeSwitch = new JButton("Transfer attribute");
		
		boolean gene = false;
		for(_DrawGeneralisation gen: controller.getGeneralisations()) {
			if(gen.getEntity().equals(mainEnt)) {
				gene = true;
				break;
			}	
		}
		attributeSwitch.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (mainEnt != null) {
					if (!mainEnt.attributes.isEmpty() || !mainEnt.compAttribute.isEmpty()) {
						boolean gene = false;
						for(_DrawGeneralisation gen: controller.getGeneralisations()) {
							if(gen.getEntity().equals(mainEnt)) {
								gene = true;
								break;
							}	
						}
						if (mainEnt.totalConnections().size() > 0 || gene ) {

							jp.removeAll();
							attrToSwitch.removeAllItems();
							potRelations.removeAllItems();

							for (_DrawAttribute attr : mainEnt.attributes.keySet()) {
								attrToSwitch.addItem(attr);

							}
							for (_DrawAttribute attr : mainEnt.compAttribute.keySet()) {
								attrToSwitch.addItem(attr);
							}

							ArrayList<_DrawRelation> relList = new ArrayList<>();

							if(mainEnt.totalConnections().size()>0) {
								for (_DrawConnection connection : mainEnt.totalConnections()) {
									if (relList.contains(connection.getRelation())) {
										continue;
									} else {
										relList.add(connection.getRelation());
									}
								}

								for (_DrawRelation rel : relList) {
									potRelations.addItem(rel);
								}
							}

							JLabel from = new JLabel(mainEnt.getName() + " Attribute: ");

							jp.add(from);
							JLabel entity = new JLabel("Selected Target: ");

							jp.add(attrToSwitch);
							otherEnts.removeAllItems();
							
							for(_DrawGeneralisation gen: controller.getGeneralisations()) {
								if(gen.getEntity() .equals(mainEnt)) {
									for(_DrawSubset sub:gen.getSubsets()) {
										otherEnts.addItem(sub.getEntity());
									}
									break;
								}	
							}
							for (_DrawEnt ent : entityList(mainEnt)) {
								otherEnts.addItem(ent);
								potRelations.addItem(ent);
							}

							jp.add(Box.createRigidArea(new Dimension(5, 0)));
							jp.add(entity);
							jp.add(potRelations);
							// jp.add(otherEnts);
							transfer();
							goBack();
							jp.revalidate();
							jp.repaint();
						} else {
							attributeSwitch.setEnabled(false);
							jp.revalidate();
							jp.repaint();
						}
					} else {
						attributeSwitch.setEnabled(false);
						jp.revalidate();
						jp.repaint();
					}
				} else {
					attributeSwitch.setEnabled(false);
					jp.revalidate();
					jp.repaint();
				}
			}
		});
		jp.remove(attributeSwitch);
		jp.add(attributeSwitch);
		attributeSwitch.setEnabled(false);
		if (mainEnt != null) {
			if (mainEnt.totalConnections().size() > 0
					&& (mainEnt.attributes.size() + mainEnt.compAttribute.size() > 0) || gene) {
				attributeSwitch.setEnabled(true);
			}
		} else {
			if (mainRel != null) {
				if (mainRel.getConnections().size() > 0
						&& mainRel.getAttr().size() + mainRel.getCompAttr().size() > 0) {
					attributeSwitch.setEnabled(true);
				}
			}
		}

		jp.revalidate();
		jp.repaint();
	}

	private void transfer() {
		JButton transfer = new JButton("Transfer");

		jp.add(transfer);
		transfer.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				// mainEnt.attributes.remove(attrToSwitch.getSelectedItem());
				controller.removeASpecificAttribute((_DrawAttribute) attrToSwitch.getSelectedItem());
				_DrawEnt picked = null;
				_DrawRelation picked2 = null;
				if (potRelations.getSelectedItem() instanceof _DrawEnt) {
					picked = (_DrawEnt) potRelations.getSelectedItem();
				} else if (potRelations.getSelectedItem() instanceof _DrawRelation) {
					picked2 = (_DrawRelation) potRelations.getSelectedItem();
				}

				if (attrToSwitch.getSelectedItem() instanceof _DrawCompositeAttribute) {
					// mainEnt.compAttribute.remove(attrToSwitch.getSelectedItem());
					if (picked != null) {
						picked.addCompAttr((_DrawCompositeAttribute) attrToSwitch.getSelectedItem());
						// picked.addAttribute((_DrawAttribute) attrToSwitch.getSelectedItem(),
						// picked.getBox());
						attrToSwitch.removeItem(picked);

					} else {
						picked2.addCompAttribute((_DrawCompositeAttribute) attrToSwitch.getSelectedItem());
						// picked2.addAttribute((_DrawAttribute) attrToSwitch.getSelectedItem());
						attrToSwitch.removeItem(picked2);
					}
				} else if (attrToSwitch.getSelectedItem() instanceof _DrawAttribute) {
					if (picked != null) {
						picked.addAttribute((_DrawAttribute) attrToSwitch.getSelectedItem(), picked.getBox());
						attrToSwitch.removeItem(picked);

					} else {
						// picked2.addCompAttribute((_DrawCompositeAttribute)
						// attrToSwitch.getSelectedItem());
						picked2.addAttribute((_DrawAttribute) attrToSwitch.getSelectedItem());
						attrToSwitch.removeItem(picked2);
					}
				}
				jp.repaint();
				view.getCanvas().repaint();

			}
		});
	}

	private void alreadyConnected(_DrawEnt ent) {
		potEntities.removeAllItems();
		ourTargets.clear();

		jp.revalidate();
		jp.repaint();

		expandConnected = new JButton("Expand Further");

		jp.setLayout(new FlowLayout());
		expandConnected.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (ent != null) {

					List<_DrawConnection> entitiesConnections = ent.totalConnections(); // Know what we are connected to
																						// given our
					// entity
					List<_DrawRelation> entitiesRelation = new ArrayList<>(); // Find out how much other entities we are
																				// connected
					// to
					List<_DrawConnection> relationConnection;
					if (entitiesConnections.size() > 0) {
						for (_DrawConnection connection : entitiesConnections) {
							entitiesRelation.add(connection.getRelation()); // we do this by finding out what relations
																			// we are
							// connected to.
						}
					}

					for (_DrawRelation relation : entitiesRelation) { // At each relation we find every entity that
																		// isn't the one we
						// are looking at but the ones that share a relation
						relationConnection = relation.getConnections();
						List<_DrawEnt> filteredList = new ArrayList<>();
						for (_DrawConnection connection : relationConnection) {
							if (connection.getStartEnt().equals(ent)) {
								continue;
							} else {
								filteredList.add(connection.getStartEnt());
							}
						}
						ourTargets.put(relation, filteredList); // for each relation we store our entities that are
																// unique.
					}

					howManyExpansions(ourTargets, ent);
				}
			}

		});
		jp.add(expandConnected);
		if (mainEnt == null && mainRel == null || mainRel != null) {
			expandConnected.setEnabled(false);
		}

		jp.revalidate();
		jp.repaint();

	}

	private void howManyExpansions(HashMap<_DrawRelation, List<_DrawEnt>> e, _DrawEnt ent) {
		jp.removeAll();
		reconnect = new JButton("Reconnect");
		JButton genericExpansion = new JButton("Generic relationship");
		JButton ternaryExpansion = new JButton("Ternary relationship");
		genericExpansion.setFont(new Font("Arial", Font.PLAIN, 12));
		genericExpansion.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				genericExpansion(mainEnt, 150);
				view.getCanvas().getCtb().displayAttributes(mainEnt, null);
				if (mainEnt.totalConnections().size() > 1) {
					if (mainEnt != null) {
						if (mainEnt.totalConnections().size() > 1) {
				//			System.out.println(mainEnt.totalConnections().size() + " total connections entity holds");
				//			System.out.println(controller.getDrawConnections().size() + " controller connections");
							reconnect.setEnabled(true);
						}
					}
					reconnectConnections();
				}
				view.getCanvas().repaint();
			}
		});
		jp.add(genericExpansion);

		ternaryExpansion.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_DrawRelation relation = genericExpansion(mainEnt, 150);
				_DrawEnt thirdEntity = controller.createADrawEnt();
				Point point = relation.getLocation();
				point.x += 100;
				thirdEntity.setLocation(point);
				controller.addAEntityConnection(thirdEntity, relation);
				potEntities.addItem(thirdEntity);
				view.getCanvas().getCtb().displayAttributes(mainEnt, null);
				if (mainEnt.totalConnections().size() > 1) {
					if (mainEnt != null) {
						if (mainEnt.totalConnections().size() > 1) {
					//		System.out.println(mainEnt.totalConnections().size() + " total connections entity holds");
					//		System.out.println(controller.getDrawConnections().size() + " controller connections");
							reconnect.setEnabled(true);
						}
					}
					reconnectConnections();
				}

				view.getCanvas().repaint();
			}
		});
		jp.add(ternaryExpansion);

		reconnect.setEnabled(false);
		jp.add(reconnect);
		goBack();
		jp.repaint();
		jp.revalidate();

	}

	public void addSubset(_DrawGeneralisation gen) {
		JButton subset = new JButton("Add subset");
		subset.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {

				_DrawEnt ent = controller.createADrawEnt();
				ent.setLocation(gen.getP2());
				_Subset sub = new _Subset(ent.getEntity(), gen.getGen());
				_DrawSubset subset = new _DrawSubset(ent, gen, sub);
				gen.getGen().addSubset(sub);
				gen.getSubsets().add(subset);
				view.getCanvas().repaint();
			}
		});
		jp.add(subset);
		goBack();

	}

	public void generalise(_DrawEnt entityToGeneralise) {
		JButton generalise = new JButton("Generalise");
		JButton transform = new JButton("Transform hierarchy");

		transform.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				controller.transformGeneralisationHeirachys();
				options();
			}
		});

		generalise.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (entityToGeneralise != null) {
					_Generalise gen = new _Generalise(entityToGeneralise.getEntity());
					_DrawGeneralisation generalisation = new _DrawGeneralisation(entityToGeneralise, gen);
					controller.addGeneralisation(generalisation);
					jp.removeAll();
					addSubset(generalisation);

					jp.revalidate();
					jp.repaint();
					view.getCanvas().repaint();
				}

			}
		});
		generalise.setEnabled(false);
		if (entityToGeneralise != null) {
			generalise.setEnabled(true);
		}

		jp.add(generalise);
		transform.setEnabled(false);

		jp.add(Box.createRigidArea(new Dimension(0, 20)));
		if (controller.getGeneralisations().size() > 0) {
			transform.setEnabled(true);
		}
		jp.add(transform);
		jp.repaint();
	}

	private void reconnectConnections() {

		JButton confirm = new JButton("Confirm");
		JLabel label_1 = new JLabel("Reconnect to");
		JLabel label_2 = new JLabel("Reconnect from");
		JComboBox<_DrawRelation> relationsToConnectTo = new JComboBox<>();

		for (ItemListener item : potEntities.getItemListeners()) {
			potEntities.removeItemListener(item);
		}

		confirm.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				if (mainEnt != null) {

					_DrawRelation rel = (_DrawRelation) relationsToConnectTo.getSelectedItem();

					for (_DrawConnection connection : rel.getConnections()) {
						if (connection.getStartEnt().equals(mainEnt)) {
							controller.removeConnection(connection);
							_DrawEnt ent = (_DrawEnt) potEntities.getSelectedItem();
							connection.addSingleConnection(ent, ent.getBox(), rel); // Reinitialise
							ent.addConnectionE(ent.getBox(), connection); // add New connection
							controller.addConnection(connection); // register new connection
							rel.getConnections().add(connection);
							relationsToConnectTo.removeItem(rel);
							if (relationsToConnectTo.getItemCount() == 0) {
								for (ItemListener item : potEntities.getItemListeners()) {
									potEntities.removeItemListener(item);
								}
								for (ItemListener item : relationsToConnectTo.getItemListeners()) {
									relationsToConnectTo.removeItemListener(item);
								}
								for (_DrawRelation relation : controller.getDrawRelations()) {
									relation.setCol(Color.white);
								}
								for (int i = 0; i < potEntities.getItemCount(); i++) {
									_DrawEnt reset = potEntities.getItemAt(i);
									reset.resetColor();
								}
								options();
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

				relationsToConnectTo.addItemListener(new ItemListener() {

					@Override
					public void itemStateChanged(ItemEvent e) {

						for (_DrawRelation rel : controller.getDrawRelations()) {
							rel.setCol(Color.white);
						}
						_DrawRelation rel = (_DrawRelation) relationsToConnectTo.getSelectedItem();
						if (rel != null)
							rel.setCol(Color.GREEN);
						view.getCanvas().repaint();

					}
				});

				if (potEntities.getItemCount() > 0) {
					_DrawEnt ent = (_DrawEnt) potEntities.getSelectedItem();
					ent.setCol(Color.cyan);
					view.getCanvas().repaint();
				}
				potEntities.addItemListener(new ItemListener() {

					@Override
					public void itemStateChanged(ItemEvent e) {
						_DrawEnt ent = (_DrawEnt) potEntities.getSelectedItem();
						if (ent != null) {
							ent.setCol(Color.cyan);
							for (_DrawEnt reset : controller.getDrawEnts()) {
								if (!ent.equals(reset)) {
									reset.resetColor();
								}
							}
					//		System.out.println("hey thats  working");

							view.getCanvas().repaint();
						} else {
					//		System.out.println("hey thats not working");
						}

					}
				});

				for (_DrawRelation relation : ourTargets.keySet()) {
					relationsToConnectTo.addItem(relation);
				}
				jp.setLayout(new FlowLayout());

				jp.add(label_2);
				jp.add(relationsToConnectTo);
				jp.add(Box.createRigidArea(new Dimension(20, 0)));

				jp.add(label_1);
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				jp.add(potEntities);
				jp.add(Box.createRigidArea(new Dimension(0, 20)));
				jp.add(confirm);
				goBack();
				jp.revalidate();
				jp.repaint();

			}
		});

		jp.revalidate();
		jp.repaint();
	}

	private _DrawRelation genericExpansion(_DrawEnt ent, int x) {

		_Entity en1 = new _Entity("DFLT_" + nameTrack, UUID.randomUUID().toString()); // add our final entity
		_DrawEnt entity1 = new _DrawEnt(en1);
		entity1.setLocation(ent.x - x, ent.y + 60);
		controller.addEnt(en1);
		controller.addDrawEnt(entity1);
		potEntities.addItem(entity1);
		for(_DrawGeneralisation gen: controller.getGeneralisations()) {
			if(gen.getEntity().equals(ent)) {
				gen.setEntity(entity1);
				break;
			}
			for(_DrawSubset sub:gen.getSubsets()) {
				if(sub.getEntity().equals(ent)){
					_Subset su = new _Subset(en1, gen.getGen());
					_DrawSubset subs = new _DrawSubset(entity1, gen, su);
					gen.getSubsets().add(subs);
					break;
				}
			}
		}
		
	

		if (ent.attributes.size() > 0 || ent.compAttribute.size() > 0) {
			List<_DrawAttribute> attributesToTransfer = new ArrayList<>();
			List<_DrawCompositeAttribute> CompAttributesToTransfer = new ArrayList<>();

			if (ent.attributes.size() > 0) {
				for (_DrawAttribute attr : ent.attributes.keySet()) {
					attributesToTransfer.add(attr);
				}
				for (_DrawAttribute attr : attributesToTransfer) {
					controller.removeASpecificAttribute(attr);
					entity1.addAttribute(attr, entity1.getBox());
				}
				attributesToTransfer.clear();
			}
			if (ent.compAttribute.size() > 0) {
				for (_DrawCompositeAttribute attr : ent.compAttribute.keySet()) {
					CompAttributesToTransfer.add(attr);
				}
				for (_DrawCompositeAttribute attr : CompAttributesToTransfer) {
					controller.removeASpecificAttribute(attr);
					entity1.addCompAttr(attr);
				}
			}

		}
		if (ent.totalConnections().size() > 0) {
			List<_DrawRelation> relationsToConnectTo = new ArrayList<>();
			for (_DrawConnection conn : ent.totalConnections()) {
				relationsToConnectTo.add(conn.getRelation());
			}
			for (_DrawRelation relation : relationsToConnectTo) {
				_DrawConnection end = new _DrawConnection(entity1.getBox(), entity1); // establish the final connection
																						// from
				// relation to entity
				end.addSingleConnection(entity1, end.getStartPos(), relation);
				entity1.addConnection(entity1.getBox(), end);
				relation.addEntry(end);
				controller.addConnection(end);
			}
		}
		nameTrack++;

		_DrawConnection conn = new _DrawConnection(entity1.getBox(), entity1); // Initialise our objects
		_Relation rel = new _Relation("DFLT");
		_DrawRelation relation = new _DrawRelation(rel);

		relation.setPosition(ent.x, ent.y); // Add our relation in first and connect it to our main entity.
		conn.addRelation(relation);
		controller.addRelation(rel);
		controller.addDrawRel(relation);
		conn.addSingleConnection(conn.getStartEnt(), conn.getStartPos(), relation);
		conn.getStartEnt().addConnection(conn.getStartPos(), conn);
		relation.addEntry(conn);
		controller.addConnection(conn);

		_Entity en = new _Entity("DFLT_" + nameTrack, UUID.randomUUID().toString()); // add our final entity
		_DrawEnt entity = new _DrawEnt(en);
		entity.setLocation(ent.x + x - 70, ent.y + 60);
		controller.addEnt(en);
		controller.addDrawEnt(entity);
		potEntities.addItem(entity);
		nameTrack++;

		_DrawConnection end = new _DrawConnection(entity.getBox(), entity); // establish the final connection from
		// relation to entity
		end.addSingleConnection(entity, end.getStartPos(), relation);
		entity.addConnectionE(entity.getBox(), end);
		relation.addEntry(end);

		controller.addConnection(end);

		potEntities.removeItem(ent);
		controller.removeDrawEnt(ent);
		mainEnt = entity1;
	//	System.out.println(mainEnt.totalConnections().size() + " total connections entity holds");

		return relation;

	}

	// Method to obtain a list of entities that we are connected to given an entity.
	private List<_DrawEnt> entityList(_DrawEnt target) {
		List<_DrawConnection> entitiesConnections = target.totalConnections(); // Know what relations we are connected
																				// to given
		// our entity
		List<_DrawRelation> entitiesRelation = new ArrayList<>(); // Find out how much other entities we are connected
		// to
		List<_DrawConnection> relationConnection; // Connections to the other entities
		List<_DrawEnt> filteredList = new ArrayList<>();

		if (entitiesConnections.size() > 0) {
			for (_DrawConnection connection : entitiesConnections) {
				entitiesRelation.add(connection.getRelation()); // we do this by finding out what relations we are
				// connected to.
			}
		}
		for (_DrawRelation relation : entitiesRelation) { // At each relation we find every entity that isn't the one we
			// are looking at but the ones that share a relation
			relationConnection = relation.getConnections();

			for (_DrawConnection connection : relationConnection) {
				if (connection.getStartEnt().equals(target)) {
					continue;
				} else {
					filteredList.add(connection.getStartEnt());
				}
			}
		}

		return filteredList;
	}

}
