package project.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import project.controller.Controller;
import project.model._Relation;
import project.view.View;
import project.view._DrawRelation;

public class _AddRelation extends AbstractAction {
	
	private static final long	serialVersionUID	= 7049149677012029316L;
	private View					view;
	private Controller			controller;

	{
		putValue(NAME, "Relation");
		putValue(SMALL_ICON, new ImageIcon(
				getClass().getResource("/project/icons/relation.png")));
		putValue(SHORT_DESCRIPTION, "Adds a relation");
	}

	public _AddRelation(View view, Controller controller)
	{
		this.view = view;
		this.controller = controller;
	}

	public void actionPerformed(ActionEvent e)
	{
		_Relation rel = new _Relation("DFLT");
		_DrawRelation relation = new _DrawRelation(rel);
		controller.unfocusAll();
		relation.setFocused(true);
		controller.addDrawRel(relation);
		
		view.repaint();
	}

}
