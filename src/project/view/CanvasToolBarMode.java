package project.view;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JToolBar;

import project.controller.Controller;


public class CanvasToolBarMode extends JToolBar{
	/**
	 * 
	 */
	private static final long serialVersionUID = -8488800365998524657L;
	View view;
	Canvas canvas;
	CanvasMenu canvasMenu;
	Controller controller;
	
	JButton topDown;
	JButton bottomUp;
	JButton freelyChoose;
	
	private boolean pressed = false;
	private boolean pressed2 = false;

	public CanvasToolBarMode(View view, Canvas canvas, CanvasMenu canvasMenu, Controller controller) {
		this.view = view;
		this.canvas = canvas;
		this.canvasMenu = canvasMenu;
		canvasMenu.setVisible(false);
		this.controller = controller;
		setUp();
	}
	
	private void setUp() {
		topDown = new JButton("Top Down");
		bottomUp = new JButton("Bottom up");
		//freelyChoose = new JButton("No restrictions");
		
		topDown.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(pressed) {
					pressed = !pressed;
					topDown.setSelected(pressed);
					bottomUp.setSelected(false);
					canvasMenu.setVisible(false);
					//freelyChoose.setSelected(false);

//					view.getCanvas().remove(canvasMenu);
//					view.getCanvas().repaint();
//					view.getCanvas().revalidate();
					canvasMenu.removeAll();
					canvasMenu.revalidate();
					canvasMenu.repaint();
					return;
				}
				else {
					pressed = !pressed;
					pressed2 = false;
					canvasMenu.setVisible(true);
					topDown.setSelected(pressed);
				//	freelyChoose.setSelected(false);
					bottomUp.setSelected(false);
					canvasMenu.enterTopDown();
				}
								
			}
		});
		
		bottomUp.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				if(pressed2) {
					pressed2 = !pressed2;
					bottomUp.setSelected(pressed2);
					topDown.setSelected(false);
					//freelyChoose.setSelected(false);
					canvasMenu.setVisible(false);
					canvasMenu.removeAll();
					canvasMenu.revalidate();
					canvasMenu.repaint();
					return;
				}
				else {
					pressed2 = !pressed2;
					pressed = false;
					bottomUp.setSelected(pressed2);
					topDown.setSelected(false);
				//	freelyChoose.setSelected(false);
					canvasMenu.setVisible(true);
					canvasMenu.enterBottomUp();
				}
				
			}
		});
//		
//		freelyChoose.addActionListener(new ActionListener() {
//			
//			@Override
//			public void actionPerformed(ActionEvent e) {
//				canvasMenu.enterNoRestrictions();
//				if(pressed2) {
//					pressed2 = !pressed2;
//					freelyChoose.setSelected(pressed2);
//					bottomUp.setSelected(false);
//					topDown.setSelected(false);
//					canvasMenu.setVisible(false);
//					canvasMenu.removeAll();
//					canvasMenu.revalidate();
//					canvasMenu.repaint();
//					return;
//				}
//				else {
//					pressed2 = !pressed2;
//					pressed = false;
//					freelyChoose.setSelected(pressed2);
//					bottomUp.setSelected(false);
//					topDown.setSelected(false);
//					canvasMenu.setVisible(true);
//					canvasMenu.enterNoRestrictions();
//				}
//			}
//		});
		
		add(topDown);
		add(Box.createRigidArea(new Dimension(5, 0)));
		add(bottomUp);
		add(Box.createRigidArea(new Dimension(5, 0)));
		//add(freelyChoose);
		
	}

}
