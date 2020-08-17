package project.view;

import java.awt.Dimension;

import javax.swing.JPanel;

import project.controller.Controller;

public class CanvasMenu  extends JPanel {
	
	TopDownApproach td;
	BottomUpApproach bu;
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -2504871202479101754L;
	/**
	 * 
	 */
	Canvas 			    canvas;
	View					view;
	Controller 			controller;
	
	private boolean topDown = false;
	private boolean bottomUp = false;
	private boolean enterAggregation = false;
	private boolean enterGeneralisation =false;

	// private boolean noRestrictions = false;
	
	
	public String s = "a";
	private int currentSelected = 0;
	private NoRestrictions nr;
	private boolean noRestrictions;
	public CanvasMenu(View view, Canvas canvas, Controller controller) {
		this.view = view;
		this.canvas = canvas;
		this.controller = controller;
		
		setPreferredSize(new Dimension(180,700));
		this.td = new TopDownApproach(this, view, controller);
		this.bu = new BottomUpApproach(this, view, controller);
		this.nr = new NoRestrictions(this, view, controller);
	}
	
	public void setUp(_DrawEnt ent, _DrawRelation rel) {
		if(topDown) {
			td.clearAndAdd(ent, rel);
			
		}else if(bottomUp) {
			bu.setUp(ent, rel);
			
		}
		else {
			if(noRestrictions) {
				nr.setup();
			}
		}
	}
	
	public void enterTopDown() {
		topDown = true;
		bottomUp = false;
		noRestrictions = false;
		currentSelected = 1;
		td.options();
	}
	
	public void enterBottomUp() {
		topDown = false;
		bottomUp = true;
		noRestrictions = false;
		currentSelected = 2;

		bu.listAllOptions();
	}
	
	public void enterNoRestrictions() {
		topDown = false;
		bottomUp = false;
		noRestrictions = true;
		currentSelected = 3;
		nr.setup();
	}

	public boolean isEnterAggregation() {
		return enterAggregation;
	}

	public void setEnterAggregation(boolean enterAggregation) {
		this.enterAggregation = enterAggregation;
	}
	
	public void setEnterGeneralisation(boolean enterGeneralisation) {
		this.enterGeneralisation = enterGeneralisation;
	}
	public boolean isEnterGeneralisation() {
		return enterGeneralisation;
	}
	
	public int getCurrentSelected() {
		return currentSelected;
	}
	
	public void updateMenu(int val) {
		if(currentSelected == 1) {
			enterTopDown();
			return;
		}
		else {
			if(currentSelected == 2) {
				enterBottomUp();
				return;
			}
		}
		if(currentSelected == 3) {
				enterNoRestrictions();
				return;
		}
		
		else {
			
		}
	}

}
