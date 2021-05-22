package project.view;

import project.controller.Controller;
import project.model.Model;
import project.model._Relation;

import javax.swing.event.MouseInputListener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;

/**
 * User: Alan P. Sexton Date: 21/06/13 Time: 00:52
 */
public class CanvasMouseListener implements MouseInputListener

{
	Model model;
	View view;
	Controller controller;
	Canvas canvas;

	double x1;
	double y1;
	int mouse1;
	int mouse2;
	int mouse3;
	int mouse4;
	boolean mouseDown = false;
	private Point startPos = new Point(0, 0);
	private Point endPos = null;
	private _DrawConnection connection = null;

	private boolean _entity = false;
	private boolean _relation = false;
	private boolean _attribute = false;
	private boolean _compAttribute = false;

	CanvasToolbar ctb;

	public CanvasMouseListener(Model model, View view, Controller controller, Canvas canvas, CanvasToolbar ctb) {
		this.model = model;
		this.view = view;
		this.controller = controller;
		this.canvas = canvas;
		this.ctb = ctb;
		ctb.setVisible(false);

	}

	public void paint(Graphics g) {
		if (mouseDown && (mouse1 > 0 && mouse2 > 0)) {
			g.setColor(Color.BLACK);
			g.drawLine(mouse1, mouse2, mouse3, mouse4);
		}
	}

	@Override
	public void mouseClicked(MouseEvent e) {
		_DrawEnt entity = null;
		_DrawRelation relation = null;
		ctb.setVisible(false);

		for (KeyListener key : ctb.textArea.getKeyListeners()) {
			ctb.textArea.removeKeyListener(key);
		}

		for (ActionListener aL : ctb.textArea.getActionListeners()) {
			ctb.textArea.removeActionListener(aL);
		}

		if (_attribute && !_entity && !_relation && !_compAttribute) {
			for (_DrawAttribute attr : controller.getAttributes()) {
				if (attr.circle.getBounds().contains(e.getPoint())) {
					if (view.getCanvasMenu().isEnterAggregation()) {
						if (!attr.isSelected()) {
							attr.setColor(Color.BLUE);
							attr.setSelected(true);
							view.getCanvas().repaint();
						}
						if (view.getCanvasMenu().bu.attributesToAggregate.contains(attr)) {
							attr.setSelected(false);
							view.getCanvasMenu().bu.attributesToAggregate.remove(attr);
							view.getCanvas().repaint();
							return;
						} else {
							view.getCanvasMenu().bu.attributesToAggregate.add(attr);
						}
					}
					ctb.setVisible(true);
					ctb.genericAttributeStats(attr);
					ctb.revalidate();
					ctb.repaint();
					view.getCanvas().repaint();
					return;
				}
			}
		} else if (!_attribute && !_entity && !_relation && _compAttribute
				|| _attribute && !_entity && !_relation && _compAttribute) {
			for (_DrawCompositeAttribute attr : controller.getCompAttr()) {
				if (attr.circle.getBounds().contains(e.getPoint())) {
					ctb.textArea.setText(attr.getName());
					ctb.attributeStats(attr);
					ctb.setPreferredSize(new Dimension(160, 200));
					ctb.setVisible(false);
					ctb.revalidate();
					ctb.repaint();
					view.getCanvas().repaint();
					return;
				} else {
					for (_DrawAttribute attrib : attr.getOtherAttribs().keySet()) {
						if (attrib.circle.getBounds().contains(e.getPoint())) {
							ctb.genericAttributeStats(attrib);
							ctb.setVisible(true);
							ctb.revalidate();
							ctb.repaint();
							view.getCanvas().repaint();
							return;
						}
					}
				}
			}
		} else if (!_attribute && !_entity && _relation && !_compAttribute) {
			for (_DrawRelation rel : controller.getDrawRelations()) {
				if (rel.contains(e.getPoint())) {
					unfocus(10);
					unfocus(0);
					rel.setFocused(true);
					relation = rel;

					ctb.setVisible(true);
					ctb.textArea.setText(rel.getName());
					view.repaint();
					ctb.textArea.addKeyListener(new KeyListener() {
						@Override
						public void keyTyped(KeyEvent e) {
							if (rel.isFocused()) {
								rel.setName(ctb.textArea.getText() + e.getKeyChar());
								view.repaint();
							}
						}

						@Override
						public void keyPressed(KeyEvent e) {
						}

						@Override
						public void keyReleased(KeyEvent e) {
						}

					});
					view.repaint();
					break;

				}
			}

		} else if (_attribute && !_entity && _relation && !_compAttribute
				|| _attribute && !_entity && _relation && _compAttribute
				|| !_attribute && !_entity && _relation && _compAttribute) {
			for (_DrawRelation rel : controller.getDrawRelations()) {
				for (_DrawAttribute attr : rel.getAttr().keySet()) {
					if (attr.circle.getBounds().contains(e.getPoint())) {
						attr.mouseClicked(e);
						ctb.setVisible(true);
						if (view.getCanvasMenu().isEnterAggregation()) {
							if (!attr.isSelected()) {
								attr.setColor(Color.BLUE);
								attr.setSelected(true);
								view.getCanvas().repaint();

							}
							if (view.getCanvasMenu().bu.attributesToAggregate.contains(attr)) {
								view.getCanvasMenu().bu.attributesToAggregate.remove(attr);
								attr.setSelected(false);
								attr.setColor(Color.BLACK);
								view.getCanvas().repaint();
							} else {
								view.getCanvasMenu().bu.attributesToAggregate.add(attr);
								return;
							}
						} else {
							ctb.genericAttributeStats(attr);
							view.getCanvas().repaint();
							return;
						}
					}

				}

				for (_DrawCompositeAttribute comp : rel.getCompAttr().keySet()) {
					if (comp.circle.getBounds().contains(e.getPoint())) {
						ctb.setVisible(true);
						ctb.textArea.setText(comp.getName());
						ctb.attributeStats(comp);
						ctb.setPreferredSize(new Dimension(160, 200));
						ctb.revalidate();
						ctb.repaint();
						view.getCanvas().repaint();
						return;
					}
					else {
						for (_DrawAttribute attrib : comp.getOtherAttribs().keySet()) {
							if (attrib.circle.getBounds().contains(e.getPoint())) {
								attrib.mouseClicked(e);
								ctb.setVisible(true);
								ctb.genericAttributeStats(attrib);
								ctb.revalidate();
								ctb.repaint();
								view.getCanvas().repaint();
								return;
							}
						}
					}
				}
			}
		} else if (_attribute && _entity && !_relation && !_compAttribute
				|| !_attribute && _entity && !_relation && _compAttribute
				|| _attribute && _entity && !_relation && _compAttribute) {
			for (_DrawEnt ent : controller.getDrawEnts()) {
				for (_DrawAttribute attr : ent.attributes.keySet()) {
					if (attr.circle.getBounds().contains(e.getPoint())) {
						ctb.setVisible(true);
						if (view.getCanvasMenu().isEnterAggregation()) {
							if (!attr.isSelected()) {
								attr.setColor(Color.BLUE);
								attr.setSelected(true);
								view.getCanvas().repaint();
							}
							if (view.getCanvasMenu().bu.attributesToAggregate.contains(attr)) {
								attr.setSelected(false);
								attr.setColor(Color.BLACK);
								view.getCanvasMenu().bu.attributesToAggregate.remove(attr);
								view.getCanvas().repaint();
								return;
							} else {
								view.getCanvasMenu().bu.attributesToAggregate.add(attr);
							}
						}
						attr.mouseClicked(e);
						ctb.genericAttributeStats(attr);
						view.repaint();
						if (attr.gotHit()) {
							return;
						}
					}
				}
				for (_DrawCompositeAttribute comp : ent.compAttribute.keySet()) {
					if (comp.circle.getBounds().contains(e.getPoint())) {
						comp.mouseClicked(e);
						ctb.setVisible(true);
						ctb.textArea.setText(comp.getName());
						ctb.attributeStats(comp);
						ctb.setPreferredSize(new Dimension(160, 200));
						ctb.revalidate();
						ctb.repaint();
						view.getCanvas().repaint();
						return;
					} else {
						for (_DrawAttribute attrib : comp.getOtherAttribs().keySet()) {
							if (attrib.circle.getBounds().contains(e.getPoint())) {
								attrib.mouseClicked(e);
								ctb.setVisible(true);
								ctb.genericAttributeStats(attrib);
								ctb.revalidate();
								ctb.repaint();
								view.getCanvas().repaint();
								return;
							}
						}
					}
				}
			}
		} else if (!_attribute && _entity && !_relation && !_compAttribute) {
			for (_DrawEnt ent : controller.getDrawEnts()) {
				if (ent.contains(e.getPoint())) {
					ctb.setVisible(true);
					unfocus(0);
					unfocus(10);
					ent.setFocused(true); // we are focused
					entity = ent;
					ctb.textArea.setText(ent.getName());
					view.repaint();
					ctb.textArea.addKeyListener(new KeyListener() {

						@Override
						public void keyTyped(KeyEvent e) {
							if (ent.isFocused()) {
								ent.setName(ctb.textArea.getText() + e.getKeyChar());
								view.repaint();
							}
						}

						@Override
						public void keyPressed(KeyEvent e) {
						}

						@Override
						public void keyReleased(KeyEvent e) {
						}

					});
					if(view.getCanvasMenu().isEnterGeneralisation()) {
						if(ent.isEntToGeneralise()) {
							ent.resetColor();
							ent.setEntToGeneralise(false);
					//		System.out.println(view.getCanvasMenu().bu.getEntsToGen().size());
							view.getCanvasMenu().bu.getEntsToGen().remove(ent);

						}
						else {
							ent.setCol(Color.cyan);
							ent.setEntToGeneralise(true);
					//		System.out.println(view.getCanvasMenu().bu.getEntsToGen().size());
							view.getCanvasMenu().bu.getEntsToGen().add(ent);
					//		System.out.println(view.getCanvasMenu().bu.getEntsToGen().size());


						}
						return;
					}
				}
			}
		}
		// ctb.setPreferredSize(new Dimension(160, 200));
		ctb.displayAttributes(entity, relation);
		ctb.revalidate();
		ctb.repaint();
		view.getCanvasMenu().setUp(entity, relation);
		view.repaint();
	}

	@Override
	public void mousePressed(MouseEvent e) {
		mouseDown = true;
		Point point = e.getPoint();
		view.getCanvas().addMouseMotionListener(this);

		for (_DrawAttribute attr : controller.getAttributes()) {
			if (attr.circle.getBounds().contains(e.getPoint())) {
				attr.mousePressed(e);
				_attribute = true;
				_compAttribute = false;
				_entity = false;
				_relation = false;
				return;
			}
		}

		for (_DrawCompositeAttribute comp : controller.getCompAttr()) {
			if (comp.circle.getBounds().contains(e.getPoint())) {
				_attribute = true;
				_compAttribute = false;
				_entity = false;
				_relation = false;
				comp.mousePressed(e);
				return;
			} else {
				for (_DrawAttribute attr : comp.getOtherAttribs().keySet()) {
					if (attr.circle.getBounds().contains(e.getPoint())) {
						_attribute = true;
						_compAttribute = true;
						_entity = false;
						_relation = false;
						attr.mousePressed(e);
						return;
					}
				}
			}

		}

		for (_DrawEnt ent : controller.getDrawEnts()) {
			if (ent.contains(e.getPoint())) {
				_attribute = false;
				_compAttribute = false;
				_entity = true;
				_relation = false;
				x1 = e.getX();
				y1 = e.getY();
				startPos = e.getPoint();
				ent.press();

				if (ent.withinBox(point)) { // where we are not connected at all to anything and we want to connect. We
					// begin to draw the line from a box here.
					ent.unpress();
					mouse1 = point.x;
					mouse2 = point.y;
					connection = new _DrawConnection(startPos, ent);
					ent.addConnection(startPos, connection);
				}

				return;
			}
			for (_DrawAttribute attr : ent.attributes.keySet()) {
				if (attr.circle.getBounds().contains(e.getPoint())) {
					_attribute = true;
					_compAttribute = false;
					_entity = true;
					_relation = false;
					attr.mousePressed(e);
					canvas.repaint();
					return;
				}
			}
			for (_DrawCompositeAttribute comp : ent.compAttribute.keySet()) {
				if (comp.circle.getBounds().contains(e.getPoint())) {
					_attribute = false;
					_compAttribute = true;
					_entity = true;
					_relation = false;
					comp.mousePressed(e);
					canvas.repaint();
					return;
				} else {
					for (_DrawAttribute attr : comp.getOtherAttribs().keySet()) {
						if (attr.circle.getBounds().contains(e.getPoint())) {
							attr.mousePressed(e);
							_attribute = true;
							_compAttribute = true;
							_entity = true;
							_relation = false;
							canvas.repaint();
							return;
						}
					}
				}
			}
		}

		for (_DrawRelation rel : controller.getDrawRelations()) {
			if (rel.contains(e.getPoint())) {
				_attribute = false;
				_compAttribute = false;
				_entity = false;
				_relation = true;
				// rel.setCol(Color.cyan);
				x1 = e.getX();
				y1 = e.getY();
				startPos = e.getPoint();
				rel.press();
				return;
			}
			for (_DrawAttribute attr : rel.getAttr().keySet()) {
				if (attr.circle.getBounds().contains(e.getPoint())) {
					_attribute = true;
					_compAttribute = false;
					_entity = false;
					_relation = true;
					attr.mousePressed(e);
					view.getCanvas().repaint();
					return;
				}
			}
			for (_DrawCompositeAttribute comp : rel.getCompAttr().keySet()) {
				if (comp.circle.getBounds().contains(e.getPoint())) {
					_attribute = false;
					_compAttribute = true;
					_entity = false;
					_relation = true;
					comp.mousePressed(e);
					canvas.repaint();
					return;
				}
				for (_DrawAttribute attr : comp.getOtherAttribs().keySet()) {
					if (attr.circle.getBounds().contains(e.getPoint())) {
						_attribute = true;
						_compAttribute = true;
						_entity = false;
						_relation = true;
						attr.mousePressed(e);
						canvas.repaint();
						return;
					}
				}
			}
		}
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		Point ep = e.getPoint();
		mouse3 = (int) ep.getX();
		mouse4 = (int) ep.getY();
		endPos = new Point(mouse3, mouse4);

		boolean flagged = false;

		for (_DrawRelation rel : controller.getDrawRelations()) {
			if (rel.contains(endPos) && connection != null) {
				rel.addEntry(connection);
				connection.addSingleConnection(connection.getStartEnt(), connection.getStartPos(), rel);
				controller.addConnection(connection);
				view.repaint();
				connection = null;
				mouse1 = 0;
				mouse2 = 0;
				mouse3 = 0;
				mouse4 = 0;
				startPos = null;
				break;
			}
		}

		// Create our relation here
		for (_DrawEnt ent : controller.getDrawEnts()) {
			if (flagged) {
				break;
			}
			if (ent.contains(ep)) {
				flagged = true;
				if (ent.withinBox(ep)) {
					Point p = ent.getCursor(ep);
					if (p == null || connection == null) {
						return;
					} else {
						endPos = p;
					}
					createRelation(ent);
					connection = null;
					mouse1 = 0;
					mouse2 = 0;
					mouse3 = 0;
					mouse4 = 0;
					startPos = null;
					break;
				}
			}
		}
		if (flagged == false) { // we know we have hit empty space.

			if (connection != null) {
				// To handle the case of unwanted connections
				connection.getStartEnt().removeConnection(connection);
				view.repaint();
				startPos = null;
				mouse1 = 0;
				mouse2 = 0;
				mouse3 = 0;
				mouse4 = 0;
				connection = null;
			}
			for (_DrawEnt ent : controller.getDrawEnts()) {
				ent.resetColor();
			}
			for (_DrawRelation rel : controller.getDrawRelations()) {
				rel.setCol(Color.white);
			}
		}

		view.getCanvas().repaint();
		view.getCanvas().removeMouseMotionListener(this);
		mouseDown = false;
		x1 = 0;
		y1 = 0;
		endPos = null;

		for (_DrawEnt ent : controller.getDrawEnts()) {
			ent.unpress();
			for (_DrawAttribute attr : ent.attributes.keySet()) {
				attr.unPress();
			}
			for (_DrawCompositeAttribute att : ent.compAttribute.keySet()) {
				att.unPress();
				for (_DrawAttribute attr : att.getOtherAttribs().keySet()) {
					attr.unPress();
				}
			}
		}

		for (_DrawRelation rel : controller.getDrawRelations()) {
			rel.unpress();
			for (_DrawAttribute attr : rel.getAttr().keySet()) {
				attr.unPress();
			}
		}
	}

	private void createRelation(_DrawEnt ent) {
		_Relation rel = new _Relation("DFLT");
		_DrawRelation relation = new _DrawRelation(rel);
		relation.setPosition((startPos.x + endPos.x) / 2, (startPos.y + endPos.y) / 2);

		connection.addRelation(relation);
		controller.addDrawRel(relation);

		connection.getStartEnt().removeConnection(connection);
		connection.addSingleConnection(connection.getStartEnt(), connection.getStartPos(), relation);
		connection.getStartEnt().addConnection(connection.getStartPos(), connection);
		relation.addEntry(connection);
		controller.addConnection(connection);

		_DrawConnection end = new _DrawConnection(endPos, ent);
		end.addSingleConnection(ent, endPos, relation);
		ent.addConnectionE(endPos, end);
		relation.addEntry(end);

		controller.addConnection(end);
		controller.addRelation(rel);

	}

	@Override
	public void mouseDragged(MouseEvent e) {
		int dx = e.getX() - (int) x1;
		int dy = e.getY() - (int) y1;
		Point point = e.getPoint();

		// regular Free
		if (_attribute && !_entity && !_relation && !_compAttribute) {
			for (_DrawAttribute attr : controller.getAttributes()) {
				if (attr.circle.getBounds().contains(e.getPoint())) {
					attr.mouseDragged(e);
					view.getCanvas().repaint();
					return;
				}
			}
		}

		else if (!_attribute && !_entity && !_relation && _compAttribute
				|| _attribute && !_entity && !_relation && _compAttribute) {
			// composite Free
			for (_DrawCompositeAttribute comp : controller.getCompAttr()) {
				if (comp.circle.getBounds().contains(e.getPoint())) {
					comp.mouseDragged(e);
					view.getCanvas().repaint();
					return;
				} else { // specific composite Free
					for (_DrawAttribute attr : comp.getOtherAttribs().keySet()) {
						if (attr.circle.getBounds().contains(e.getPoint())) {
							attr.mouseDragged(e);
							view.getCanvas().repaint();
							return;
						}
					}
				}
			}
		}
		/////////////////////////////////////////////////////
		else if (_attribute && !_entity && _relation && !_compAttribute
				|| _attribute && !_entity && _relation && _compAttribute
				|| !_attribute && !_entity && _relation && _compAttribute) {
			for (_DrawRelation rel : controller.getDrawRelations()) {
				for (_DrawAttribute attr : rel.getAttr().keySet()) {
					if (attr.circle.getBounds().contains(e.getPoint())) {
						attr.mouseDragged(e);
						canvas.repaint();
						return;
					}
				}
				for (_DrawCompositeAttribute comp : rel.getCompAttr().keySet()) {
					if (comp.circle.getBounds().contains(e.getPoint())) {
						comp.mouseDragged(e);
						canvas.repaint();
						return;
					} else {
						for (_DrawAttribute attr : comp.getOtherAttribs().keySet()) {
							if (attr.circle.getBounds().contains(e.getPoint())) {
								attr.mouseDragged(e);
								canvas.repaint();
								return;
							}
						}
					}
				}
			}
		}
		/////////////////////////////////////////////////

		else if (_attribute && _entity && !_relation && !_compAttribute
				|| !_attribute && _entity && !_relation && _compAttribute
				|| _attribute && _entity && !_relation && _compAttribute) {
			for (_DrawEnt ent : controller.getDrawEnts()) {
				for (_DrawAttribute attr : ent.attributes.keySet()) { // Seeing if attributes are being dragged
					if (attr.circle.getBounds().contains(e.getPoint())) {
						attr.mouseDragged(e);
						canvas.repaint();
						return;
					}
				}
				for (_DrawCompositeAttribute comp : ent.compAttribute.keySet()) {
					if (comp.circle.getBounds().contains(e.getPoint())) {
						comp.mouseDragged(e);
						canvas.repaint();
						return;
					} else {
						for (_DrawAttribute attr : comp.getOtherAttribs().keySet()) {
							if (attr.circle.getBounds().contains(e.getPoint())) {
								attr.mouseDragged(e);
								canvas.repaint();
								return;
							}
						}
					}
				}

			}
		} else if (!_attribute && !_entity && _relation && !_compAttribute) {

			for (_DrawRelation rel : controller.getDrawRelations()) { // For dragging a relation.
				if (rel.contains(point) && rel.isPressed()) {
					rel.x = (int) (rel.getX() + dx);
					rel.y = (int) (rel.getY() + dy);
					view.repaint();
					x1 += dx;
					y1 += dy;
					return;
				}
			}
		} else if (!_attribute && _entity && !_relation && !_compAttribute) {

			for (_DrawEnt ent : controller.getDrawEnts()) {
				if (ent.withinBox(point) && (mouse1 > 0 && mouse2 > 0)) { // where we are connected to a point and we
					// don't
					// want to move but move the line.
					ent.unpress();
					break;

				} else if (ent.contains(point) && ent.isPressed()) { // for dragging the entity generally.
					ent.x = (int) (ent.getX() + dx);
					ent.y = (int) (ent.getY() + dy);
					view.repaint();
					x1 += dx;
					y1 += dy;
					break;
				}
			}
		}

		for (_DrawEnt ent : controller.getDrawEnts()) {
			ent.updateEnds();
			ent.updatePoints();
		}

		mouse3 = (int) point.getX();
		mouse4 = (int) point.getY();
		endPos = new Point(mouse3, mouse4);
		view.repaint();

	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
	}

	@Override
	public void mouseMoved(MouseEvent e) {
	}

	private void unfocus(int type) {
		if (type == 0) {
			for (_DrawEnt ent : controller.getDrawEnts()) {
				ent.setFocused(false);
			}
		} else {
			for (_DrawRelation rel : controller.getDrawRelations()) {
				rel.setFocused(false);
			}
		}
	}
}
