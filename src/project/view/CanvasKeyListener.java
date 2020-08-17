package project.view;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;

import project.controller.Controller;
import project.model.Model;

public class CanvasKeyListener implements KeyListener{

	Model model;
	View view;
	Controller controller;
	Canvas canvas;

	public CanvasKeyListener(Model model, View view, Controller controller, Canvas canvas) {
		this.model = model;
		this.view = view;
		this.controller = controller;
		this.canvas = canvas;
		this.canvas.requestFocus();
		// System.out.println(this.canvas.hasFocus());
		this.canvas.addKeyListener(this);
		
	}
	private String text = " ";
	boolean clicked = false;
	
	@Override
	public void keyTyped(KeyEvent e) {
//		view.getCanvas().addKeyListener(this);
//		
//		for (_DrawEnt ent : controller.getEnts()) {
//			if (ent.focus) {
//					char text1 = e.getKeyChar();
//					text += text1;
//					ent.setName(text);
//					view.repaint();
//					break;
//			}  
//		}
//		text = "";
	}

	@Override
	public void keyPressed(KeyEvent e) {
		view.getCanvas().addKeyListener(this);
		for (_DrawEnt ent : controller.getDrawEnts()) {
			ent.setName(""+e.getKeyChar());
		}

	}

	@Override
	public void keyReleased(KeyEvent e) {
		view.getCanvas().removeKeyListener(this);

		// TODO Auto-generated method stub
		
	}
	
	public String getText(){
		return this.text;
	}
	
	
}
