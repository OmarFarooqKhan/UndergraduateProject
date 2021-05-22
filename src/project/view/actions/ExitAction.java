package project.view.actions;

import project.controller.AttributeController;
import project.controller.Controller;
import project.view.View;
import project.view.CanvasView.NewView;

import javax.swing.*;
import java.awt.event.ActionEvent;

/**
 * User: Alan P. Sexton Date: 20/06/13 Time: 20:50
 */
public class ExitAction extends AbstractAction
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 7049149677012029336L;
	private NewView				    view;
	private AttributeController			controller;

	{
		putValue(NAME, "Quit");
		putValue(SMALL_ICON, new ImageIcon(
				getClass().getResource("/project/icons/exit.png")));
		putValue(SHORT_DESCRIPTION, "Quits the program");
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control Q"));

	}

	public ExitAction(NewView view, AttributeController controller)
	{
		this.view = view;
		this.controller = controller;
	}

	public void actionPerformed(ActionEvent e)
	{
		int response = JOptionPane.showConfirmDialog(view,
				"Do you really want to quit?", "Exit",
				JOptionPane.YES_NO_OPTION);
		if (response == JOptionPane.YES_OPTION)
			controller.Exit(0);
	}
}
