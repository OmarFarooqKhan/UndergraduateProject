package project.view.actions;
import java.awt.event.ActionEvent;
import java.util.UUID;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import project.controller.Controller;
import project.model._Attribute;
import project.view.View;
import project.view._DrawAttribute;
import project.view._DrawEnt;

public class _AddAttributeAction  extends AbstractAction {

	private static final long	serialVersionUID	= 7049149677012029316L;
	private View					view;
	private Controller			controller;

	{
		putValue(NAME, "Attribute");
		putValue(SMALL_ICON, new ImageIcon(
				getClass().getResource("/project/icons/Entity.png")));
		putValue(SHORT_DESCRIPTION, " Adds an Attribute");
	}

	public _AddAttributeAction(View view, Controller controller)
	{
		this.view = view;
		this.controller = controller;
	}

	public void actionPerformed(ActionEvent e)
	{
		for (_DrawEnt ent : controller.getDrawEnts()) {
			if(ent.isFocused()) {
				_DrawAttribute attr = new _DrawAttribute(new _Attribute("DFLT",UUID.randomUUID().toString()));
				ent.addAttribute(attr,ent.getLocation());
				break;
			}
		}
		view.repaint();

	}
		
}
