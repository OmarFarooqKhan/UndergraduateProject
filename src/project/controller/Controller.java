package project.controller;

import project.model.Model;
import project.model._Attribute;
import project.model._CompositeAttribute;
import project.model._Connection;
import project.model._Entity;
import project.model._Relation;
import project.view.View;
import project.view._DrawAttribute;
import project.view._DrawCompositeAttribute;
import project.view._DrawConnection;
import project.view._DrawEnt;
import project.view._DrawGeneralisation;
import project.view._DrawRelation;
import project.view._DrawSubset;

import java.awt.Color;
import java.io.EOFException;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;

/**
 * User: Alan P. Sexton
 * Date: 22/06/13
 * Time: 17:17
 */

/**
 * Recall that the controller and related classes in the controller package are
 * the only classes allowed to update the model.
 */
public class Controller {
	Model model;
	View view;
	FileInputStream filestream;
	ObjectInputStream inputStream;
	private HashMap<_DrawEnt, List<_DrawEnt>> topDownList = new HashMap<>();
	public HashMap<_DrawEnt, ArrayList<_DrawEnt>> weakMembers = new HashMap<>();
	public List<_DrawEnt> ent = new ArrayList<_DrawEnt>();
	public List<_DrawRelation> rel = new ArrayList<_DrawRelation>();
	public List<_DrawConnection> cons = new ArrayList<_DrawConnection>();
	public List<_DrawAttribute> attr = new ArrayList<_DrawAttribute>();
	public List<_DrawGeneralisation> generalisations = new ArrayList<>();
	public List<_DrawCompositeAttribute> compattribute = new ArrayList<>();

	/**
	 * In this version of the MVC model, the <code>View</code> has to have a
	 * reference to the <code>Controller</code> so that it can inform it of view
	 * events. Hence create the <code>View</code> (with the <code>Controller</code>
	 * as one of its constructor parameters) after creating the
	 * <code>Controller</code>. However, the <code>Controller</code> needs a
	 * reference to its <code>View</code> to tell it what to do, so create the
	 * <code>Controller</code> without a <code>View</code> first, then create the
	 * <code>View</code> with the <code>Controller</code> as a parameter, then add
	 * the <code>View</code> to the <code>Controller</code>.
	 *
	 * @param model
	 *            the model to be associated with this controller
	 */
	public Controller(Model model) {
		this.model = model;
	}

	/**
	 * This implementation only allows one view to be associated with a controller
	 * at a time: it would be easy to modify this to allow multiple views.
	 *
	 * @param view
	 *            the <code>View</code> to be added to this controller
	 */
	public void addView(View view) {
		this.view = view;
	}

	public List<_Entity> getEnt() {
		return model.getEnts();
	}

	public List<_DrawGeneralisation> getGeneralisations() {
		return generalisations;
	}

	/**
	 * Add a new rectangle to the model and update the view accordingly
	 * 
	 * @param rect
	 *            the rectangle to be added
	 */

	public void addEnt(_Entity entity) {
		model.addEnt(entity);
		view.repaint();
	}

	public void addGeneralisation(_DrawGeneralisation gen) {
		model.addGen(gen.getGen());
		generalisations.add(gen);
	}

	public void addRelation(_Relation relation) {
		model.addRel(relation);
		view.repaint();
	}

	public void addConnection(_DrawConnection connection) {
		// System.out.println(model.getConnections().size() +" Model connection size
		// before adding connection");
		// System.out.println(cons.size() +" Controller connection size before adding
		// connection");
		_Connection conn = model.createConnection(connection.getStartEnt().getEntity(),
				connection.getRelation().getRelation());
		connection.setConnection(conn);
		conn.setCard(connection.getCard());
		cons.add(connection);
		// System.out.println(model.getConnections().size() +" Model connection size
		// after adding connection");
		// System.out.println(cons.size() +" Controller connection size after adding
		// connection");

		view.repaint();
	}

	public _DrawEnt createADrawEnt() {
		_Entity modelEntity = new _Entity("Un-named", UUID.randomUUID().toString());
		_DrawEnt entity = new _DrawEnt(modelEntity);
		model.addEnt(modelEntity);
		ent.add(entity);
		return entity;

	}

	public void exit(int exitStatus) {
		System.exit(exitStatus);
	}

	public void transformGeneralisationHeirachys() {
		if (generalisations.size() > 0) {
			for (_DrawGeneralisation gen : generalisations) {
				for (_DrawSubset sub : gen.getSubsets()) {
					_DrawEnt ent = sub.getEntity();
					createAConnection(gen.getEntity(), ent);
				}
			}
			clearGeneralisations();
			view.getCanvas().repaint();
		}
	}

	private void createAConnection(_DrawEnt ent, _DrawEnt entity) {

		_DrawConnection conn = new _DrawConnection(ent.getBox(), ent); // Initialise our objects
		_Relation rel = new _Relation("DFLT");
		_DrawRelation relation = new _DrawRelation(rel);

		relation.setPosition(ent.x, ent.y + 80); // Add our relation in first and connect it to our main entity.
		conn.addRelation(relation);
		addRelation(rel);
		addDrawRel(relation);
		conn.addSingleConnection(conn.getStartEnt(), conn.getStartPos(), relation);
		conn.getStartEnt().addConnection(conn.getStartPos(), conn);
		relation.addEntry(conn);
		addConnection(conn);
		conn.setCard("(0,1)");

		_DrawConnection end = new _DrawConnection(entity.getBox(), entity); // establish the final connection from
		// relation to entity
		end.addSingleConnection(entity, end.getStartPos(), relation);
		entity.addConnectionE(entity.getBox(), end);
		relation.addEntry(end);

		addConnection(end);

	}

	public List<_DrawEnt> getDrawEnts() {
		return ent;
	}

	public List<_DrawRelation> getDrawRelations() {
		return rel;
	}

	public List<_DrawConnection> getDrawConnections() {
		return cons;
	}

	public _DrawRelation createADrawRelation() {
		_Relation relation = new _Relation("rely");
		_DrawRelation drawRelation = new _DrawRelation(relation);
		model.addRel(relation);
		rel.add(drawRelation);
		return drawRelation;
	}

	public void addDrawEnt(_DrawEnt e) {
		ent.add(e);
		view.repaint();
	}

	public void addDrawRel(_DrawRelation r) {
		rel.add(r);
		// System.out.println(rel.size() + " added Relation Controller size");
		// System.out.println(model.getRelations().size()+ " added Relation Model
		// size");
		view.repaint();
	}

	public void addCompositeAttribute(_DrawCompositeAttribute attribute) {
		compattribute.add(attribute);
		view.repaint();
	}

	public void addAttribute(_DrawAttribute attribute) {
		attr.add(attribute);
		view.repaint();
	}

	public void removeDrawEnt(_DrawEnt e) {
		if (ent.contains(e)) {
			_DrawGeneralisation generalise = null;
			_DrawSubset sub = null;

			List<_DrawConnection> toBeRemoved = new ArrayList<>();
			List<_DrawEnt> wEtoBeRemoved = new ArrayList<>();
			for (_DrawConnection con : cons) {
				if (e.removeConnection(con)) {
					toBeRemoved.add(con);
				}
			}

			for (_DrawConnection con : toBeRemoved) {
				_DrawRelation relation = con.getRelation();
				for (_DrawConnection connection : relation.getConnections()) {
					if (connection.getStartEnt().isWeakEnt() && !connection.getStartEnt().equals(e)) {
						wEtoBeRemoved.add(connection.getStartEnt());
					}
				}
			}
			for (_DrawConnection con : toBeRemoved) {
				removeConnection(con);
			}
			for (_DrawEnt weakEnt : wEtoBeRemoved) {
				removeDrawEnt(weakEnt);
			}

			for (_DrawGeneralisation gen : generalisations) {

				if (gen.getEntity().equals(e)) {
					generalise = gen;
					break;
				}

				for (_DrawSubset subset : gen.getSubsets()) {
					if (subset.getEntity().equals(e)) {
						sub = subset;
						break;
					}
				}

				if (sub != null) {
					gen.getSubsets().remove(sub);
					gen.getGen().removeSubset(sub.getSub());
					if (gen.getSubsets().size() == 0) {
						generalise = gen;
					}
					break;
				}
			}
			if (generalise != null) {
				model.deleteGen(generalise.getGen());
				generalisations.remove(generalise);
			}
			model.deleteEnt(e.getEntity());
			ent.remove(e);
			view.repaint();
		}
	}

	public void removeDrawRelation(_DrawRelation r) {
		List<_DrawConnection> toBeRemoved = new ArrayList<>();
		if (rel.contains(r)) {
			for (_DrawConnection con : getDrawConnections()) {
				if (r.getConnections().contains(con)) {
					toBeRemoved.add(con);
				}
			}
			for (_DrawConnection con : toBeRemoved) {
				if (con.getStartEnt() != null) {
					con.getStartEnt().removeConnection(con);
				}
				removeConnection(con);
			}
			// System.out.println(rel.size() + " Controller relations left before removal");
			// System.out.println(model.getRelations().size() + " Model relations left
			// before removal");

			model.deleteRelation(r.getRelation());
			rel.remove(r);
			// System.out.println(rel.size() + " Controller relations left after removal");

			// System.out.println(model.getRelations().size() + " Model relations left after
			// removal");

			view.repaint();
		}
	}

	// UNUSED: when removing a connection remove the relation but just might delete
	// this instead.
	public void removeConnection(_DrawConnection c) {
		if (cons.contains(c)) {
			model.deleteConnection(c.getConnection());
			c.getStartEnt().removeConnection(c);
			c.getRelation().getConnections().remove(c);
			cons.remove(c);
			// System.out.println(model.getConnections().size() + " Model connections left
			// after Removal");
			// System.out.println(cons.size() + " Controller connections left after
			// Removal");

			view.repaint();
		}
	}

	public void clearRelation() {
		model.removeRelations();
		rel.clear();
		view.repaint();
	}

	public void clearEnt() {
		model.removeEnts();
		ent.clear();
		view.repaint();
	}

	public void clearAttr() {
		attr.clear();
		view.repaint();
	}

	public void clearCompAttr() {
		compattribute.clear();
		view.repaint();
	}

	public void clearConnection() {
		model.removeConnections();
		cons.clear();
		view.repaint();
	}

	public void clearGeneralisations() {
		model.removeGens();
		generalisations.clear();
		view.repaint();
	}

	public void clearEverything() {
		clearRelation();
		clearConnection();
		clearEnt();
		clearAttr();
		clearCompAttr();
		clearGeneralisations();

	}

	public void addAttributeRel(_DrawRelation relat, _DrawAttribute attribute) {
		relat.getRelation().addAtr(attribute.getAttribute());
	}

	public void removeAttribute(_DrawEnt entity, _DrawAttribute attribute) {
		for (_DrawAttribute att : entity.attributes.keySet()) {
			if (att.isCompositeIdentifier()) {
				if (att.getCompositeIdentifiers().contains(attribute)) {
					att.getAttribute().getIdentifiers().remove(attribute.getAttribute());
					att.getCompositeIdentifiers().remove(attribute);
				}
			}
		}
		// System.out.println(entity.getEntity().attributes.size() +" Before attribute
		// is removed from entity");
		model.deleteAttribute(entity.getEntity(), attribute.getAttribute());
		entity.attributes.remove(attribute);
		// System.out.println(entity.getEntity().attributes.size() +" After attribute is
		// removed from entity");
	}

	public void removeAttribute(_DrawRelation relation, _DrawAttribute attribute) {
		// System.out.println(relation.getRelation().attributes.size() +" Before Removed
		// attribute from a relation ");
		model.deleteAttribute(relation.getRelation(), attribute.getAttribute());
		relation.getAttr().remove(attribute);
		// System.out.println(relation.getRelation().attributes.size() +" After Removed
		// attribute from a relation ");

	}

	public void addAEntityConnection(_DrawEnt entity, _DrawRelation relation) {
		_DrawConnection connection = new _DrawConnection(entity.getBox(), entity);
		connection.addRelation(relation); // what we are connecting to
		connection.addSingleConnection(connection.getStartEnt(), connection.getStartPos(), relation);
		entity.addConnection(connection.getStartPos(), connection); // The enitity we are coming from
		addConnection(connection);
		relation.addEntry(connection);
		view.getCanvas().repaint();
	}

	public void addAEntityConnectionEnd(_DrawEnt entity, _DrawRelation relation) {
		_DrawConnection connection = new _DrawConnection(entity.getBox(), entity);
		connection.addRelation(relation); // what we are connecting to
		connection.addSingleConnection(connection.getStartEnt(), connection.getStartPos(), relation);
		entity.addConnectionE(connection.getStartPos(), connection); // The enitity we are coming from
		addConnection(connection);
		relation.addEntry(connection);
		view.getCanvas().repaint();
	}

	public void unfocusAll() {

		for (_DrawRelation rel : getDrawRelations()) {
			rel.setFocused(false);
		}

		for (_DrawEnt ent : getDrawEnts()) {
			ent.setFocused(false);
		}
	}

	public HashMap<_DrawEnt, List<_DrawEnt>> getTopDownList() {
		return topDownList;
	}

	public _DrawAttribute addAttribute() {
		_Attribute attribute = new _Attribute("Dflt", UUID.randomUUID().toString());
		_DrawAttribute attrib = new _DrawAttribute(attribute);
		// attr.add(attrib);
		return attrib;
	}

	public _DrawCompositeAttribute addCompAttribute() {
		_CompositeAttribute attribute = new _CompositeAttribute("Dflt", UUID.randomUUID().toString());
		_DrawCompositeAttribute attrib = new _DrawCompositeAttribute(attribute);
		return attrib;
	}

	public List<_DrawCompositeAttribute> getCompAttr() {
		return compattribute;
	}

	public List<_DrawAttribute> getAttributes() {
		return attr;
	}

	public boolean removeASpecificAttribute(_DrawAttribute attr) {
		if (!getAttributes().remove(attr)) {
			for (_DrawCompositeAttribute comp : getCompAttr()) {
				if (comp.getOtherAttribs().containsKey(attr)) {
					comp.getOtherAttribs().remove(attr);
					comp.getCompAttribute().removeAttribute(attr.getAttribute());
					view.getCanvas().repaint();
					return true;
				}
			}

			for (_DrawEnt ent : getDrawEnts()) {
				if (ent.attributes.containsKey(attr)) {
					removeAttribute(ent, attr);
					view.getCanvas().repaint();
					return true;
				} else if (ent.compAttribute.containsKey(attr)) {
					// System.out.println("Before removed From Main ent "+
					// ent.compAttribute.size());
					ent.compAttribute.remove(attr);
					// System.out.println("After removed From Main ent " +
					// ent.compAttribute.size());
					// System.out.println("Before removed From Model ent "+
					// ent.getEntity().getCompositeAttributes().size());

					ent.getEntity().removeCompositeAttribute((_CompositeAttribute) attr.getAttribute());
					// System.out.println("After removed From Model ent "+
					// ent.getEntity().getCompositeAttributes().size());

					view.getCanvas().repaint();
					return true;
				} else {
					for (_DrawCompositeAttribute comp : ent.compAttribute.keySet()) {
						if (comp.getOtherAttribs().containsKey(attr)) {
							// System.out.println(comp.getOtherAttribs().size() +" Comp Attribute size
							// before (view) " );
							// System.out.println(ent.getEntity().getCompositeAttributesList(comp.getCompAttribute()).size()
							// +" Comp Attribute size before (model) " );

							comp.getOtherAttribs().remove(attr);
							comp.getCompAttribute().removeAttribute(attr.getAttribute());

							// System.out.println(comp.getOtherAttribs().size() +" Comp Attribute size after
							// (view)" );
							// System.out.println(ent.getEntity().getCompositeAttributesList(comp.getCompAttribute()).size()
							// +" Comp Attribute size before (model) " );

							view.getCanvas().repaint();
							return true;
						}
					}
				}
			}
			for (_DrawRelation rel : getDrawRelations()) {
				if (rel.getAttr().containsKey(attr)) {
					removeAttribute(rel, attr);
					view.getCanvas().repaint();
					return true;
				} else if (rel.getCompAttr().containsKey(attr)) {
					rel.getCompAttr().remove(attr);
					rel.getRelation().removeCompositeAttribute((_CompositeAttribute) attr.getAttribute());
					view.getCanvas().repaint();
					return true;
				} else {
					for (_DrawCompositeAttribute comp : rel.getCompAttr().keySet()) {
						if (comp.getOtherAttribs().containsKey(attr)) {
							comp.getOtherAttribs().remove(attr);
							comp.getCompAttribute().removeAttribute(attr.getAttribute());
							view.getCanvas().repaint();
							return true;
						}
					}
				}
			}
		} else {
			return true;
		}
		return false;
	}

	public _Attribute addAttri() {
		_Attribute attrib = new _Attribute("dflt", UUID.randomUUID().toString());
		return attrib;
	}

	public _CompositeAttribute addCompAttri() {
		_CompositeAttribute comp = new _CompositeAttribute("Dflt", UUID.randomUUID().toString());
		return comp;
	}

	public void addFreeAttribute(_DrawAttribute att) {
		attr.add(att);
		view.repaint();
	}

	public void removeFreeAttribute(_DrawAttribute att) {
		attr.remove(att);
		view.repaint();
	}

	public void removeComp(_DrawCompositeAttribute com) {
		compattribute.remove(com);
		view.repaint();
	}

	public void removeGeneralisation(_DrawGeneralisation gen) {
		generalisations.remove(gen);
		view.repaint();
	}

	public void removeSpecificAttr(_DrawCompositeAttribute comp, _DrawAttribute att) {
		comp.getOtherAttribs().remove(att);
		comp.getCompAttribute().removeAttribute(att.getAttribute());
		view.repaint();
	}

	public void loadAFile(File f, String string) throws IOException{
		Object obj;
		try {
			filestream = new FileInputStream(string);
			try {
				inputStream = new ObjectInputStream(filestream);
				try {
					while ((obj = inputStream.readObject()) != null) {
						if (obj instanceof _DrawEnt) {
							_DrawEnt entity = (_DrawEnt) obj;
							ent.add(entity);
							model.addEnt(entity.getEntity());
				
						}
						if(obj instanceof _DrawRelation) {
							_DrawRelation relation = (_DrawRelation) obj;
							rel.add(relation);
							model.addRel(relation.getRelation());
						}
						if(obj instanceof _DrawConnection) {
							_DrawConnection connection = (_DrawConnection) obj;
							cons.add(connection);
							model.addCon(connection.getConnection());
						}
					
						if(obj instanceof _DrawGeneralisation) {
							_DrawGeneralisation gen = (_DrawGeneralisation) obj;
							generalisations.add(gen);
							model.addGen(gen.getGen());
						}
					}
			} catch (ClassNotFoundException e) {
				e.printStackTrace();
			} finally {
				inputStream.close();
			}
		} finally {
				filestream.close();
		}
		} catch (FileNotFoundException e) {
			throw e;
		}
		catch (EOFException e) {
			throw e;
		}
		catch (Exception e) {
		//	e.printStackTrace();
		}
	}

	public Model getModel() {
		return model;
	}

	public void saveToFile(ObjectOutputStream oos) {
		for (_DrawEnt ent : getDrawEnts()) {
			try {
				oos.writeObject(ent);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for (_DrawRelation rel : getDrawRelations()) {
			try {
				oos.writeObject(rel);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		for (_DrawConnection conn : getDrawConnections()) {
			try {
				oos.writeObject(conn);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		for (_DrawGeneralisation gen : getGeneralisations()) {
			try {
				oos.writeObject(gen);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void displayAllErrors(List<Object> allErrors) {
		for (int i = 0; i < allErrors.size(); i++) {
			Object wrong = allErrors.get(i);

			if (wrong instanceof _Connection) {
				for (_DrawRelation rela : rel) {
					if (rela.getRelation().equals(((_Connection) wrong).getRelation())) {
						rela.setCol(Color.red);
						break;
					}
				}
			}

			if (wrong instanceof _Entity) {
				for (_DrawEnt en : ent) {
					if (en.getEntity().equals(wrong)) {
						en.setCol(Color.red);
						break;
					}
				}
			}

			if (wrong instanceof _Attribute) {
				for (_Entity en : model.getEnts()) {
					if (en.getAttributes().contains(wrong)) {

						for (_DrawEnt den : ent) {
							if (den.getEntity().equals(en)) {
								for (_DrawAttribute attr : den.attributes.keySet()) {
									if (attr.getAttribute().equals(wrong)) {
										attr.setColor(Color.red);
										break;
									}
								}
							}
						}
					}

					for (_CompositeAttribute comp : en.getCompositeAttributes().keySet()) {
						if (en.getCompositeAttributesList(comp).contains(wrong)) {

							for (_DrawEnt den : ent) {
								if (den.getEntity().equals(en)) {
									for (_DrawCompositeAttribute attr : den.compAttribute.keySet()) {
										for (_DrawAttribute attrr : attr.getOtherAttribs().keySet()) {
											if (attrr.getAttribute().equals(wrong)) {
												attr.setColor(Color.red);
												break;
											}
										}
									}
								}
							}
						}
					}
				}

				for (_Relation relation : model.getRelations()) {
					for (_CompositeAttribute comp : relation.getCompositeAttributes().keySet()) {
						if (relation.getCompositeAttributesList(comp).contains(wrong)) {

							for (_DrawRelation rel : rel) {
								if (rel.getRelation().equals(relation)) {
									for (_DrawCompositeAttribute attr : rel.getCompAttr().keySet()) {
										for (_DrawAttribute attrr : attr.getOtherAttribs().keySet()) {
											if (attrr.getAttribute().equals(wrong)) {
												attr.setColor(Color.red);
												break;
											}
										}
									}
								}
							}
						}
					}
				}
			}

			if (wrong instanceof _CompositeAttribute) {
				for (_Entity en : model.getEnts()) {
					if (en.getCompositeAttributes().containsKey(wrong)) {

						for (_DrawEnt den : ent) {
							if (den.getEntity().equals(en)) {
								for (_DrawCompositeAttribute attr : den.compAttribute.keySet()) {
									if (attr.getCompAttribute().equals(wrong)) {
										attr.setColor(Color.red);
										break;
									}
								}
							}
						}

					}
				}
				for (_Relation relation : model.getRelations()) {
					if (relation.getCompositeAttributes().containsKey(wrong)) {

						for (_DrawRelation rela : rel) {
							if (rela.getRelation().equals(relation)) {
								for (_DrawCompositeAttribute attr : rela.getCompAttr().keySet()) {
									if (attr.getCompAttribute().equals(wrong)) {
										attr.setColor(Color.red);
										break;
									}
								}
							}
						}
					}
				}
			}
		}
		view.repaint();
	}
}
