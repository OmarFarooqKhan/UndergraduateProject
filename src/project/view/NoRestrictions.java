package project.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.UUID;

import javax.swing.JButton;
import javax.swing.JPanel;

import project.controller.Controller;
import project.model._Entity;

public class NoRestrictions {
	
	
	 private Controller controller;
	private View view;
	private JPanel jp;

	public NoRestrictions(JPanel jp, View view, Controller controller) {
		this.jp = jp;
		this.view = view;
		this.controller = controller;
	}

	public void setup() {
		jp.setPreferredSize(new Dimension(200,200));
		addAnEntity();
		jp.repaint();
		jp.revalidate();
	}
	
	private void addAnEntity() {
		JButton addEntity = new JButton("Create Entity");
		addEntity.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				_Entity entity = new _Entity("NEW",UUID.randomUUID().toString());
				_DrawEnt ent = new _DrawEnt(entity);
				controller.unfocusAll();
				ent.setFocused(true);
				controller.addEnt(entity);
				controller.addDrawEnt(ent);
				view.repaint();				
			}
		});
		jp.add(addEntity);
		jp.repaint();
		
	}

}
