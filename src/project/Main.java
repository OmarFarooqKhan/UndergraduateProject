package project;

import project.controller.Controller;
import project.model.Model;
import project.view.View;

import javax.swing.*;

public class Main extends JFrame
{
	
	private static final long serialVersionUID = -7026052166952174257L;

	public static void main(String[] args)
	{
		SwingUtilities.invokeLater(new Runnable()
		{
			public void run()
			{
				Model model = new Model();
				Controller controller = new Controller(model);
				View view = new View(model, controller);
				view.setVisible(true);
			}
		});
	}
}
