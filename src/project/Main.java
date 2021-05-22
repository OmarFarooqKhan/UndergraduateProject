package project;

import project.controller.AttributeController;
import project.controller.Controller;
import project.model.Model;
import project.view.View;
import project.view.CanvasView.NewView;

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
				// Model model = new Model();
				// Controller controller = new Controller(model);
				// View view = new View(model, controller);
				// view.setVisible(true);
				
				Model model = new Model();
				AttributeController controller = new AttributeController(model);
				NewView view = new NewView(model, controller);
				view.setVisible(true);
			}
		});
	}
}
