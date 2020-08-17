package project.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import project.view.View;

public class ExtendPageAction extends AbstractAction {
	private static final long	serialVersionUID	= 7049149677012029336L;
	private View				view;

	{
		putValue(NAME, "Extend page");
		putValue(SMALL_ICON, new ImageIcon(
				getClass().getResource("/project/icons/bigger.png")));
		putValue(SHORT_DESCRIPTION, "Extends the page");

	}

	public ExtendPageAction(View view)
	{
		this.view = view;
	}

	public void actionPerformed(ActionEvent e)
	{
		view.adaptToNewImage(1);		
	}
}
