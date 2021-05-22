package project.view;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.event.ItemListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import project.controller.Controller;
import project.model.Model;
import project.model._CompositeAttribute;

public class CanvasToolbar extends JPanel implements ActionListener {

	private static final long serialVersionUID = -3026980696368980856L;

	public JTextField textArea;
	Model model;
	View view;
	Controller controller;
	JToolBar tb;
	JButton exit;
	JButton attributeButton;
	JButton delete;
	JPanel jp;
	JComboBox<_DrawAttribute> attributeList;  // Used to show attribute
	private ArrayList<JTextField> cardList;
//	private _DrawAttribute privAttr;
//	private _DrawCompositeAttribute privComp;
	private int spacer = 0;

	public CanvasToolbar(Model model, View view, Controller controller) {
		this.view = view;
		this.model = model;
		this.controller = controller;

		this.setBorder(BorderFactory.createLineBorder(Color.black));
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(150, 60));

		cardList = new ArrayList<>();
		tb = new JToolBar();
		exit = new JButton("delete");
		attributeButton = new JButton("Attribute");
		textArea = new JTextField();
		jp = new JPanel();
		attributeList = new JComboBox<>();
		attributeList.setPreferredSize(new Dimension(60, 30));
		jp.setPreferredSize(new Dimension(100, 100));

		// jp.setBorder(BorderFactory.createLineBorder(Color.black));

		tb.setFloatable(false);
		textArea.setPreferredSize(new Dimension(90, 30));
		textArea.setEditable(true);

		attributeButton.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				for (_DrawEnt ent : controller.getDrawEnts()) {
					if (ent.isFocused()) {
						
						_DrawAttribute attr = controller.addAttribute();
						ent.addAttribute(attr, ent.getLocation());
						attributeList.addItem(attr);
						addRelevantButtons(attributeList, null, ent);
						addWeakEntButton(ent);
						setPreferredSize(new Dimension(180, 180));
						// jp.add(topDown,BorderLayout.SOUTH);
						view.repaint();
						return;
					}

				}
				for (_DrawRelation rel : controller.getDrawRelations()) {
					if (rel.isFocused()) {
						
						_DrawAttribute attr = controller.addAttribute();
						rel.addAttribute(attr);
						attributeList.addItem(attr);
						addRelevantButtons(attributeList, rel, null);
						setPreferredSize(new Dimension(200, 200));
						// jp.add(topDown,BorderLayout.SOUTH);
						view.repaint();
						return;
					}
				}
			}

		});
		
		
		
		
		exit.setFont(new Font("Arial", Font.PLAIN, 10));
		attributeButton.setFont(new Font("Arial", Font.PLAIN, 10));
		
		compAttributeButton();
		
		tb.add(Box.createRigidArea(new Dimension(5, 0)));
		tb.add(exit);
		tb.add(Box.createRigidArea(new Dimension(5, 0)));
		tb.add(attributeButton);

		
		jp.add(tb);
		add(textArea, BorderLayout.NORTH);
		add(jp, BorderLayout.CENTER);
		
		textArea.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent event) {
				for (_DrawEnt ent : controller.getDrawEnts()) {
					if (ent.isFocused()) {
						if (textArea.getText().trim().isEmpty()) {
							ent.setName("EMPTY");
							textArea.setText("EMPTY");
							setPreferredSize(new Dimension(180, 140));
							break;
						}
						ent.setName(textArea.getText());
						view.repaint();
						break;
					}
				}
			}
		});
		exit.addActionListener(this);


	}
	
	
	public void genericAttributeStats(_DrawAttribute attribute) {
		jp.removeAll();
		JCheckBox stringCheck = new JCheckBox("text");
		JCheckBox inte = new JCheckBox("integer");
		JCheckBox boo = new JCheckBox("boolean");
		JCheckBox dec = new JCheckBox("decimal");

		for(ActionListener aL :textArea.getActionListeners()) {
			textArea.removeActionListener(aL);
		}
		stringCheck.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					attribute.setType(1);
					boo.setSelected(false);
					inte.setSelected(false);
					dec.setSelected(false);
				}
			}
		});
		inte.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					attribute.setType(2);
					stringCheck.setSelected(false);
					boo.setSelected(false);
					dec.setSelected(false);
				}
			}
		});
		boo.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					attribute.setType(3);
					stringCheck.setSelected(false);
					inte.setSelected(false);
					dec.setSelected(false);
				}
			}
		});
		dec.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				if(e.getStateChange() == ItemEvent.SELECTED) {
					attribute.setType(4);
					stringCheck.setSelected(false);
					inte.setSelected(false);
					boo.setSelected(false);
				}
			}
		});
		
		if(attribute != null) {
			jp.remove(tb);
			textArea.setText(attribute.getName());
			String attributeType = attribute.getType();
			if(attributeType.equals("text")) {
					stringCheck.setSelected(true);
					inte.setSelected(false);
					boo.setSelected(false);
					dec.setSelected(false);
			}
					
			else if(attributeType.equals("integer")) {
					stringCheck.setSelected(false);
					inte.setSelected(true);
					boo.setSelected(false);
					dec.setSelected(false);
			}
							
			else if(attributeType.equals("boolean")) {	
					stringCheck.setSelected(false);
					inte.setSelected(false);
					boo.setSelected(true);
					dec.setSelected(false);
			}
			else if(attributeType.equals("decimal")) {	
					stringCheck.setSelected(false);
					inte.setSelected(false);
					boo.setSelected(false);
					dec.setSelected(true);
			}
			
			addKeyListenerAttribute(attribute);
			
			textArea.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					attribute.setName(textArea.getText());
					view.getCanvas().repaint();
				}
			});
			delete = new JButton("Delete");  						// Delete the selected attribute
			delete.setFont(new Font("Arial", Font.PLAIN, 10));
			delete.setPreferredSize(new Dimension(50,20));
			delete.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					controller.removeASpecificAttribute(attribute);
					textArea.setText("");
					setVisible(false);
					view.getCanvas().repaint();
				}
			});
			
			jp.add(delete);
			
			jp.add(stringCheck);
			jp.add(inte);
			jp.add(boo);
			jp.add(dec);
			
			
			jp.revalidate();
			jp.repaint();
			setPreferredSize(new Dimension(100, 180));
			view.getCanvas().repaint();
		}
	}
	
	public void attributeStats(_DrawCompositeAttribute attr) {
		jp.removeAll();

		for(ActionListener aL :textArea.getActionListeners()) {
			textArea.removeActionListener(aL);
		}
		
		
		if(attr != null) {	
			addKeyListenerAttribute(attr);

			attributeList.removeAllItems();
			for(_DrawAttribute att :attr.getOtherAttribs().keySet()) {
				attributeList.addItem(att);
			}
			
			textArea.setText(attr.getName());
			textArea.addActionListener(new ActionListener() {
				
				@Override
				public void actionPerformed(ActionEvent e) {
					attr.setName(textArea.getText());
					view.repaint();
				}
			});
			jp.setLayout(new FlowLayout());
			jp.add(attributeList);
			
			
			delete = new JButton("X");  						// Delete the selected attribute
			delete.setPreferredSize(new Dimension(15, 15));
			delete.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
						
						if (attributeList.getItemCount() == 0) {
							jp.remove(delete);
							view.getCanvas().repaint();
							return;
							
						}
						_DrawAttribute toBeDeleted = (_DrawAttribute) attributeList.getSelectedItem();
						controller.removeASpecificAttribute(toBeDeleted);
						

						attributeList.removeItem(attributeList.getSelectedItem());
					
					view.getCanvas().repaint();
				}
			});
			
			jp.add(delete);
			specified(attr);
			jp.revalidate();
			jp.repaint();
			setPreferredSize(new Dimension(160, 200));

		}
		
	}
	
	private void compAttributeButton() {
		JButton compAttribute = new JButton("Comp Attr");
		compAttribute.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				_CompositeAttribute dflt = new _CompositeAttribute("dflt",UUID.randomUUID().toString());
				_DrawCompositeAttribute comp = new _DrawCompositeAttribute(dflt);
				
				for (_DrawEnt ent : controller.getDrawEnts()) {
					if (ent.isFocused()) {
						ent.addCompAttr(comp);
						view.repaint();
						
						attributeList.addItem(comp);
						addRelevantButtons(attributeList, null, ent);
	
						// jp.add(topDown,BorderLayout.SOUTH);
					//	specified(comp);

						// jp.add(addCompAttribute);
						view.repaint();
						return;
					}
				}
				for (_DrawRelation rel : controller.getDrawRelations()) {
					if (rel.isFocused()) {
						rel.addCompAttribute(comp);

						attributeList.addItem(comp);
						addRelevantButtons(attributeList, rel, null);
						// jp.add(topDown,BorderLayout.SOUTH);
						// specified(comp);

						view.repaint();
						return;
					}
				}
			}
		});
		compAttribute.setFont(new Font("Arial", Font.PLAIN, 10));
		tb.add(compAttribute);
		view.repaint();
	}
	private void specified(_DrawCompositeAttribute spec) {
		JButton addAnother = new JButton("Add Additonal Attributes ");
		addAnother.setFont(new Font("Arial", Font.PLAIN, 10));
		spacer = 0;
		
		
		addAnother.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				int x = spec.getLocation().x;
				int y = spec.getLocation().y;
				spacer += 25;
				x+=spacer;
				Point point = new Point(x,y);
				attributeList.addItem(spec.addComponentAttribute(point));

				view.repaint();
			}
		});
		jp.remove(addAnother);
		jp.add(addAnother);
		jp.repaint();
	}

	private void connectionCheck(_DrawEnt e ) {
		ArrayList<_DrawRelation> list = new ArrayList<>();
		ArrayList<_DrawConnection> connectionsToBeRemoved = new ArrayList<>();
		for(_DrawConnection conn: controller.getDrawConnections()) {
			if(conn.getStartEnt().equals(e)) {
				list.add(conn.getRelation());
				connectionsToBeRemoved.add(conn);
			}
		}
		for(_DrawRelation rel : list) {
			for(_DrawConnection con : connectionsToBeRemoved) {
				if(rel.getConnections().contains(con)) {
					rel.getConnections().remove(con);
				}
			}
		}
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		_DrawEnt replace = null;
			for (_DrawEnt ent : controller.getDrawEnts()) {  //Find the entity we want to replace
				if (ent.isFocused()) {
					replace = ent;
					break;
				}
			}
			if (replace == null) { 					// If its not an entity its the relations we then look at
				_DrawRelation toBeRemoved = null;
				for (_DrawRelation rel : controller.getDrawRelations()) {
					if (rel.isFocused()) {
						toBeRemoved = rel;
						break;
					}
				}
				if (toBeRemoved != null) { 			// We find the right relation and remove all references.
					jp.removeAll();
					textArea.setText("");
					controller.removeDrawRelation(toBeRemoved);
					setVisible(false);
					setPreferredSize(new Dimension(180, 140));
				}
				return;
			}

			else {
				textArea.setText("");
				connectionCheck(replace);
				controller.removeDrawEnt(replace);
				view.getCanvasMenu().updateMenu(view.getCanvasMenu().getCurrentSelected());
				jp.removeAll();
				attributeList.removeAllItems();
				jp.add(tb);

				this.setPreferredSize(new Dimension(150, 60));
				setVisible(false);
				revalidate();
				repaint();
			}
		}

	// Method to display cardinalities of entities
	public void restructure(_DrawRelation r) {
		cardList.clear();
		jp.setLayout(new FlowLayout());
		for (_DrawConnection conn : r.getConnections()) {
			JTextField card = new JTextField();
			card.setPreferredSize(new Dimension(60, 30));
			card.setText(conn.getCard());
			card.addActionListener(new ActionListener() {
				@Override
				public void actionPerformed(ActionEvent e) {
					conn.setCard(card.getText());
					view.repaint();
				}

			});
			jp.add(card);
			cardList.add(card);
		}
		if(r.getConnections().size() >1) {
			setPreferredSize(new Dimension(200, 200));
		}
		else {
			setPreferredSize(new Dimension(150, 120));
		}
		revalidate();
		repaint();
	}

	public void displayAttributes(_DrawEnt entity, _DrawRelation R) {
		attributeList.removeAllItems();
		jp.removeAll();

		if (R != null) { // update the current attributes
			for (_DrawAttribute attribute : R.getAttr().keySet()) {
				attributeList.addItem(attribute);
			}
			for(_DrawCompositeAttribute attribute : R.getCompAttr().keySet()) {
				attributeList.addItem(attribute);
			}
			jp.add(tb, BorderLayout.NORTH);
			if(attributeList.getItemCount()>0) {
				jp.add(attributeList);
			}
		

			if (attributeList.getItemCount() > 0 || (R.getConnections().size() > 0)) {
				addRelevantButtons(attributeList, R, null);
			} 

			
			if((R.getConnections().size() == 0)&&(attributeList.getItemCount() == 0)) {
				// 	this.setSize(new Dimension(180, 100));
				setPreferredSize(new Dimension(150, 60));
				view.getCanvas().repaint();		
			}
			return;
		}
		
		else {
			if (entity != null) {
				
				for (_DrawAttribute attribute : entity.attributes.keySet()) {
					attributeList.addItem(attribute);
				}
				for(_DrawCompositeAttribute attribute : entity.compAttribute.keySet()) {
					attributeList.addItem(attribute);
				}
				jp.add(tb, BorderLayout.NORTH);
				jp.add(attributeList);

				
				addRelevantButtons(attributeList, null, entity);
				addWeakEntButton(entity);
				view.getCanvas().repaint();
				// jp.add(topDown,BorderLayout.SOUTH);


			} else {
				jp.add(tb, BorderLayout.NORTH);
				textArea.setText("");
				this.setPreferredSize(new Dimension(150, 60));
				view.getCanvas().repaint();

			}
		}
	}

	private void addRelevantButtons(JComboBox<_DrawAttribute> combo, _DrawRelation r, _DrawEnt en) {
		jp.removeAll(); // clear the panel
		
		JCheckBox pk = new JCheckBox("Primary Key");
		JTextField attributeName = new JTextField(); // Used for the selected attribute to change its name
		attributeName.setPreferredSize(new Dimension(60, 30));
		attributeName.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				_DrawAttribute attr = (_DrawAttribute) attributeList.getSelectedItem();
				attr.setName(attributeName.getText());
				attributeName.setText(attributeName.getText());
				view.repaint();
				jp.repaint();
			}

		});
		jp.add(tb, BorderLayout.NORTH);
		
		if (r != null) { // Check to see if the relation is connected to anything and add boxes for card
			if (r.getConnections().size() > 0) {
				restructure(r);
				jp.add(Box.createRigidArea(new Dimension(5, 0)));
			}
		}
		if(attributeList.getItemCount()>0) {
			jp.add(attributeList);
			jp.add(attributeName);
		}
		
		for( ItemListener al : attributeList.getItemListeners()) {
			attributeList.removeItemListener(al);
		}
		attributeList.addItemListener(new ItemListener() {
			
			@Override
			public void itemStateChanged(ItemEvent e) {
				
				if(e.getStateChange() == ItemEvent.SELECTED) {
					if(attributeList.getItemCount() >0) {
					_DrawAttribute attr = (_DrawAttribute) attributeList.getSelectedItem();
					if ((attr.isPrimary() ||attr.isCompositeIdentifier()) && attr!= null) {
						pk.setSelected(true);
						jp.repaint();
					}
					else {
						pk.setSelected(false);
						jp.repaint();
					}
				}
				else {
					jp.remove(attributeList);
					jp.remove(delete);
					jp.repaint();
				}
				}
			}
		});

		
		
		delete = new JButton("X");  						// Delete the selected attribute
		delete.setPreferredSize(new Dimension(15, 15));
		delete.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
	
				if(attributeList.getSelectedItem() instanceof _DrawCompositeAttribute) {
				//	System.out.println(attributeList.getItemCount()+" item count b4");
					controller.removeASpecificAttribute(( _DrawCompositeAttribute)attributeList.getSelectedItem());
					attributeList.removeItem(attributeList.getSelectedItem());
				//	System.out.println(attributeList.getItemCount()+" item count comp");
					jp.repaint();
				}
				else {
				//	System.out.println(attributeList.getItemCount()+" item count b4");

					controller.removeASpecificAttribute(( _DrawAttribute)attributeList.getSelectedItem());
					attributeList.removeItem(attributeList.getSelectedItem());
				//	System.out.println(attributeList.getItemCount()+" item count");
					jp.repaint();

				}
				
				
				if (attributeList.getItemCount() == 0) {
					jp.remove(attributeList);
					jp.remove(delete);
					jp.remove(pk);
					jp.remove(attributeName);
					setPreferredSize(new Dimension(180,140));
				}
				view.getCanvas().repaint();
			}
		});
	
		
		if (attributeList.getItemCount() > 0) {  // if the attribute count is > 0 
			if (en != null) { 			// Setting PK is only available to entities
				_DrawAttribute attr = (_DrawAttribute) attributeList.getSelectedItem();
				if (attr.isPrimary()) {
					pk.setSelected(true);
				}
				else {
					pk.setSelected(false);
				}
				for( ItemListener al : pk.getItemListeners()) {
					pk.removeItemListener(al);
				}
				if(en.isWeakEnt()) {
					attr.setWeakened(true);
				}
				pk.addItemListener(new ItemListener() {

					@Override
					public void itemStateChanged(ItemEvent e) {
						_DrawAttribute attr = (_DrawAttribute) attributeList.getSelectedItem();
						
						if (e.getStateChange() == ItemEvent.SELECTED) {
							if(en.isWeakEnt()) {
								attr.setWeakened(true);
							}
							else {
								attr.setWeakened(false);
							}
								attr.selected();
								view.repaint();
							if(attr.isCompositeIdentifier()) {
								attr.selected();
								view.repaint();
							}
						}
						if (e.getStateChange() == ItemEvent.DESELECTED) {
							if(en.isWeakEnt()) {
								attr.setWeakened(true);
							}
							else {
								attr.setWeakened(false);
							}
								attr.unselected();
								view.repaint();
							if(attr.isCompositeIdentifier()) {
								attr.selected();
								view.repaint();
								pk.setSelected(true);
							}
						}
					}
				});
				jp.add(pk);	
				setPreferredSize(new Dimension(180,180));
				jp.revalidate();
				jp.repaint();
				view.getCanvas().repaint();
				
			}
		}
	
		
	
		if(combo.getItemCount() > 0) {
			jp.add(delete);
		}
	
		if (r != null) {
			if(r.getConnections().size() > 0) {
				setPreferredSize(new Dimension(180,140));
			}
			else {
				setPreferredSize(new Dimension(180, 60));
			}
		}
		else if (en != null) {
			if(en.totalConnections().size() > 0) {
				setPreferredSize(new Dimension(180,180));
			}
			else if(en.attributes.size() + en.compAttribute.size() > 0) {
				setPreferredSize(new Dimension(180,200));
			}
			else {
				setPreferredSize(new Dimension(180, 60));
			}
			
		}
				
	}
	
	
	private void addWeakEntButton(_DrawEnt entity) {
		JCheckBox weakEntity = new JCheckBox("Weak Entity"); 
		boolean rel = false;
		_DrawRelation dependant = null;
		
		
		for (_DrawRelation relation : controller.getDrawRelations()) {
			if(rel) {
				break;
			}
			for(_DrawConnection conn : relation.getConnections()) {
				if(conn.getStartEnt().equals(entity) && relation.getConnections().size() > 1  && relation.getConnections().size() < 4) {
					rel = true; // Hunting for a relation connection 
					dependant = relation;
					break;
				}
			}
		}
		if(dependant == null) {
			return;
		}
		else {
			int singleEnt = 0;
			List<_DrawEnt> ourTargets = new ArrayList<>();
			for (_DrawConnection conn : dependant.getConnections()) { // To avoid situation of an entity being its own weak entity
				if(conn.getStartEnt().equals(entity)) {
					singleEnt++;
				}
				else {
					ourTargets.add(conn.getStartEnt());
				}
			}
			if(singleEnt > 1) {
				return;
			}
			
			if (entity.isWeakEnt()) {
				entity.getEntity().setWeakEnt(true);
				weakEntity.setSelected(true);
				jp.repaint();
			}
			
			weakEntity.addItemListener(new ItemListener() {
				@Override
				public void itemStateChanged(ItemEvent e) {
					
					if (e.getStateChange() == ItemEvent.SELECTED) {
						entity.setWeakEnt(true);
						entity.getEntity().setWeakEnt(true);
						
						for (_DrawConnection conn : entity.totalConnections()) {
							_DrawRelation rel =  conn.getRelation();
							
							for(_DrawConnection con:rel.getConnections()) {
								if(con.equals(conn)) {
									continue;
								}
								else {
									con.setCard("(1,1)");
								}
							}
							conn.setCard("(1,n)");
						}

						for(_DrawAttribute attribute : entity.attributes.keySet()) {
							if(attribute.isPrimary()) {
								attribute.setWeakened(true);
							}
						}
						
						view.repaint();
					}
					
					if (e.getStateChange() == ItemEvent.DESELECTED) {
						entity.setWeakEnt(false);
						entity.getEntity().setWeakEnt(false);
						for(_DrawAttribute attribute : entity.attributes.keySet()) {
							attribute.setWeakened(false);
						}

						view.repaint();
					}
				}
				
			});
			jp.add(weakEntity);
			jp.repaint();
		}
	}
	
	private void addKeyListenerAttribute(_DrawAttribute attr) {
		
		for (KeyListener key : textArea.getKeyListeners()) {
			textArea.removeKeyListener(key);
		}
		
		textArea.addKeyListener(new KeyListener() {
			@Override
			public void keyTyped(KeyEvent e) {
				if (attr.gotHit()) {
					attr.setName(textArea.getText() + e.getKeyChar());
					view.getCanvas().repaint();
				}
			}

			@Override
			public void keyPressed(KeyEvent e) {
			}

			@Override
			public void keyReleased(KeyEvent e) {
			}

		});
	}
}
