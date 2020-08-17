package project.view.actions;

import project.controller.Controller;
import project.utils.SimpleFileFilter;
import project.view.View;

import javax.swing.*;


import java.awt.event.ActionEvent;
import java.io.EOFException;
import java.io.File;
import java.io.IOException;

/**
 * User: Alan P. Sexton Date: 20/06/13 Time: 21:04
 */
public class OpenAction extends AbstractAction
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= 9036684359479464138L;
	private View				view;
	private Controller			controller;


	// Note that once we first create a file chooser object, we keep it and
	// re-use
	// it rather than creating a new one each time that we invoke this action.
	// This has the effect that the chooser dialog always starts in the last
	// directory we opened from, rather than going back to the starting
	// directory.
	private JFileChooser		fc	= null;

	{
		putValue(NAME, "Open new project image file...");
		putValue(SMALL_ICON, new ImageIcon(
				getClass().getResource("/project/icons/fileopen.png")));
		putValue(SHORT_DESCRIPTION, "Opens a new image file");
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control O"));
	}

	public OpenAction(View view, Controller controller)
	{
		this.view = view;
		this.controller = controller;
	}

	public void actionPerformed(ActionEvent e)
	{
		if (fc == null)
		{
			fc = new JFileChooser(".");
			
			SimpleFileFilter filter = new SimpleFileFilter();
			filter.addExtension(".txt");
			filter.setDescription("image files");
			fc.setFileFilter(filter);
		}
		fc.setDialogTitle("Choose a file to open");
		int result = fc.showOpenDialog(view);
		try
		{
		if (result == JFileChooser.APPROVE_OPTION)
		{
				File f = fc.getSelectedFile();
				System.out.println(fc.getSelectedFile().getName());
				controller.clearEverything();
				controller.loadAFile(f,fc.getSelectedFile().getName());
			
			}
		}
		catch(EOFException eofe) {
		    //eof - no error in this case
		}
		catch (IOException ioe)
		{
			JOptionPane.showMessageDialog(view, ioe.getMessage(),
					"Accessing Image File", JOptionPane.ERROR_MESSAGE);
			// ioe.printStackTrace();
		}
	}

}
