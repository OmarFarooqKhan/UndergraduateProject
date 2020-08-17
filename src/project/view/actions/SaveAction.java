package project.view.actions;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import javax.swing.AbstractAction;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.KeyStroke;
import project.controller.Controller;
import project.utils.SimpleFileFilter;
import project.view.View;

public class SaveAction extends AbstractAction {
	/**
	* 
	*/
	private static final long serialVersionUID = 4928581198610192691L;
	/**
	 * 
	 */
	private Controller controller;
	private View view;
	private JFileChooser fc = null;
	private File file;

	// Note that once we first create a file chooser object, we keep it and
	// re-use
	// it rather than creating a new one each time that we invoke this action.
	// This has the effect that the chooser dialog always starts in the last
	// directory we opened from, rather than going back to the starting
	// directory.
	FileOutputStream fos = null;
	ObjectOutputStream oos = null;

	{
		putValue(NAME, "Save...");
		putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/project/icons/save.png")));
		putValue(SHORT_DESCRIPTION, "saves your file");
		putValue(ACCELERATOR_KEY, KeyStroke.getKeyStroke("control S"));
	}

	public SaveAction(View view, Controller controller) {
		this.controller = controller;
		this.view = view;
	}

	public void actionPerformed(ActionEvent e) {

		if (fc == null) {
			fc = new JFileChooser(".");
			// Use the following line instead if you always want the file
			// chooser to
			// start in the user's home directory rather than the current
			// directory
			// imageFileChooser = new
			// JFileChooser(System.getProperty("user.dir"));
			SimpleFileFilter filter = new SimpleFileFilter();
			filter.addExtension(".txt");
			filter.setDescription("Text files");
			fc.setFileFilter(filter);
			
		}
		fc.setDialogTitle("Save your file");
		int result = fc.showSaveDialog(view);

		if (result == JFileChooser.APPROVE_OPTION) {
			String filename = fc.getSelectedFile().getName();
			System.out.println(filename);
			if (fos == null) {
				try {
					
					filename = filename.trim();
					if(filename.contains(".txt")) {
						file = new File(filename);
						fos = new FileOutputStream(file);
						if (!file.exists()) {
							file.createNewFile();
						}
						oos = new ObjectOutputStream(fos);
						controller.saveToFile(oos);
						fos.flush();
						oos.flush();
					}
					else {
						filename = filename +".txt";
						file = new File(filename);
						fos = new FileOutputStream(file);
						if (!file.exists()) {
							file.createNewFile();
						}
						oos = new ObjectOutputStream(fos);
						controller.saveToFile(oos);
						fos.flush();
						oos.flush();
						
					}

				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				} finally {
					try {
						oos.close();
						fos.close();
						fos = null;
						oos = null;
					} catch (IOException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}

				}
			}
		}
	}
}
