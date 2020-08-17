package project.view.actions;

import project.controller.Controller;
import project.model._Entity;
import project.view.View;
import project.view._DrawEnt;

import javax.swing.*;
import java.awt.event.ActionEvent;
import java.util.UUID;


public class _AddEntAction  extends AbstractAction {

	private static final long	serialVersionUID	= 7049149677012029316L;
	private View					view;
	private String				DFLT_NME = "EMPTY";
	private Controller			controller;

	{
		putValue(NAME, "Entity");
		putValue(SMALL_ICON, new ImageIcon(
				getClass().getResource("/project/icons/Entity.png")));
		putValue(SHORT_DESCRIPTION, " Adds an Entity");
	}

	public _AddEntAction(View view, Controller controller)
	{
		this.view = view;
		this.controller = controller;
	}

	public void actionPerformed(ActionEvent e)
	{
		_Entity entity = new _Entity(DFLT_NME,UUID.randomUUID().toString());
		_DrawEnt ent = new _DrawEnt(entity);
		controller.unfocusAll();
		ent.setFocused(true);
		controller.addEnt(entity);
		controller.addDrawEnt(ent);
		view.repaint();
	}
}
