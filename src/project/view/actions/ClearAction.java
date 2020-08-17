package project.view.actions;

import java.awt.event.ActionEvent;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JOptionPane;
import javax.swing.KeyStroke;

import project.controller.Controller;
import project.view.View;

public class ClearAction extends AbstractAction{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 7049149677012029336L;
	private View				view;
	private Controller			controller;

	{
		putValue(NAME, "Clear");
		putValue(SMALL_ICON, new ImageIcon(
				getClass().getResource("/project/icons/sweep.png")));
		putValue(SHORT_DESCRIPTION, "clears everything");
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control C"));

	}

	public ClearAction(View view, Controller controller)
	{
		this.view = view;
		this.controller = controller;
	}

	public void actionPerformed(ActionEvent e)
	{
		int response = JOptionPane.showConfirmDialog(view,
				"Do you really want Clear?", "Exit",
				JOptionPane.YES_NO_OPTION);
		if (response == JOptionPane.YES_OPTION)
			controller.clearEverything();
			view.adaptToNewImage(2);
	}
}

