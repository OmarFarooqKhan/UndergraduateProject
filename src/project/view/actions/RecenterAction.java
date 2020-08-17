package project.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import project.controller.Controller;
import project.view.View;
import project.view._DrawEnt;
import project.view._DrawRelation;

public class RecenterAction extends AbstractAction {

	private static final long	serialVersionUID	= 7049149677012029316L;
	private View					view;
	private Controller			controller;

	{
		putValue(NAME, "Attribute");
		putValue(SMALL_ICON, new ImageIcon(
				getClass().getResource("/project/icons/center.png")));
		putValue(SHORT_DESCRIPTION, " Recenters everything off screen");
	}

	public RecenterAction(View view, Controller controller)
	{
		this.view = view;
		this.controller = controller;
	}

	public void actionPerformed(ActionEvent e)
	{
		for (_DrawEnt ent : controller.getDrawEnts()) {
			if((ent.getLocation().x < 0 || ent.getLocation().y > 700) ||
				ent.getLocation().x > 800 |ent.getLocation().y < 0) {
				ent.setLocation(400,350);
			}
		}
		for (_DrawRelation rel : controller.getDrawRelations()) {
			if(rel.getLocation().x < 0 || rel.getLocation().y > 700 ||
				rel.getLocation().x > 800 || rel.getLocation().y < 0){
				rel.setLocation(450,350);
			}
		}
		view.repaint();

	}

}
