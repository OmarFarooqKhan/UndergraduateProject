package project.view;

import project.controller.Controller;
import project.model.Model;
import project.view.actions.ClearAction;
import project.view.actions.ExitAction;
import project.view.actions.ExportSQLAction;
import project.view.actions.ExtendPageAction;
import project.view.actions.OpenAction;
import project.view.actions.RecenterAction;
import project.view.actions.SaveAction;
import project.view.actions._AddAttributeAction;
import project.view.actions._AddEntAction;
import project.view.actions._AddRelation;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * User: Alan P. Sexton Date: 21/06/13 Time: 13:42
 */
public class View extends JFrame
{
	/**
	 * 
	 */
	private static final long	serialVersionUID	= -6963519874728205328L;
	private Canvas				canvas				= null;
	private JScrollPane			canvasScrollPane;
	private CanvasToolBarMode 	canvasToolbarMenu;
	private CanvasMenu			canvasMenu;
	private String 				filename = "";
	
	public View(Model model, Controller controller)
	{
		super("Basic Java GUI Application");
		controller.addView(this);
	

		// We will use the default BorderLayout, with a scrolled panel in
		// the centre area, a tool bar in the NORTH area and a menu bar

		
		canvas = new Canvas(model, this, controller);
		
		canvasScrollPane = new HVMouseWheelScrollPane();

		// The default when scrolling is very slow. Set the increment as
		// follows:
		canvasScrollPane.getVerticalScrollBar().setUnitIncrement(16);
		canvasScrollPane.getHorizontalScrollBar().setUnitIncrement(16);
		canvasScrollPane.setViewportView(canvas);
		getContentPane().add(canvasScrollPane, BorderLayout.CENTER);



		
		// exitAction has to be final because we reference it from within
		// an inner class

		final AbstractAction exitAction = new ExitAction(this, controller);
		AbstractAction clearAction = new ClearAction(this,controller);
		AbstractAction addEnt = new _AddEntAction(this,controller);
		AbstractAction addRel = new _AddRelation(this,controller);
		AbstractAction addAttr = new _AddAttributeAction(this, controller);
		AbstractAction openAction = new OpenAction(this, controller);
		AbstractAction saveAction = new SaveAction(this,controller);
		AbstractAction recenter = new RecenterAction(this, controller);
		AbstractAction genSQL = new ExportSQLAction(controller);
		AbstractAction extendPage = new ExtendPageAction(this);
		
		canvasMenu = new CanvasMenu(this, canvas,controller);
		canvasToolbarMenu = new CanvasToolBarMode(this, canvas,canvasMenu,controller);
		canvasToolbarMenu.setFloatable(false);
		canvasToolbarMenu.add(Box.createRigidArea(new Dimension(10, 0)));

		canvasToolbarMenu.add(exitAction).setBorder(new EmptyBorder(5, 0, 5, 0));
		canvasToolbarMenu.add(Box.createRigidArea(new Dimension(10, 0)));

		canvasToolbarMenu.add(clearAction).setBorder(new EmptyBorder(5, 0, 5, 0));
		canvasToolbarMenu.add(Box.createRigidArea(new Dimension(10, 0)));
		canvasToolbarMenu.add(recenter).setBorder(new EmptyBorder(5, 0, 5, 0));
		canvasToolbarMenu.add(Box.createRigidArea(new Dimension(10, 0)));
		canvasToolbarMenu.add(extendPage).setBorder(new EmptyBorder(5, 0, 5, 0));
		
		getContentPane().add(canvasToolbarMenu,BorderLayout.NORTH);
		getContentPane().add(canvasMenu,BorderLayout.EAST);
	

		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent we)
			{
				exitAction.actionPerformed(null);
			}
		});

		// Set up the menu bar
		JMenu fileMenu;
		fileMenu = new JMenu("File");
		fileMenu.add(exitAction);
		fileMenu.add(openAction);
		fileMenu.add(saveAction);
		fileMenu.add(genSQL);

		JMenuBar menuBar;

		menuBar = new JMenuBar();
		menuBar.add(fileMenu);

		setJMenuBar(menuBar);
	
		JToolBar toolBar;
		toolBar = new JToolBar(null,JToolBar.VERTICAL);
		toolBar.setFloatable(false);
		toolBar.setRollover(true);
		toolBar.add(exitAction).setBorder(new EmptyBorder(5, 0, 5, 0));
		toolBar.addSeparator();
		toolBar.add(clearAction).setBorder(new EmptyBorder(5, 0, 5, 0));
		toolBar.addSeparator();
		toolBar.add(addEnt).setBorder(new EmptyBorder(5, 0, 5, 0));
		toolBar.addSeparator();
		toolBar.add(addRel).setBorder(new EmptyBorder(5, 0, 5, 0));
		toolBar.addSeparator();
		toolBar.add(addAttr).setBorder(new EmptyBorder(5, 0, 5, 0));
		// getContentPane().add(toolBar, BorderLayout.WEST);
		pack();
		
		setBounds(0, 0, 1000, 900);
		
		
	}

	public void adaptToNewImage(int num)
	{
		setCanvasSize(num);
	}
	
	
	/**
	 * Adapt the settings for the ViewPort and scroll bars to the dimensions
	 * required. This needs to be called any time the image changes size.
	 */
	protected void setCanvasSize(int num)
	{
		if(num == 1) {
			Dimension dimens = canvas.getPreferredSize();
			dimens.setSize(dimens.getWidth(),dimens.getHeight()+50);
			canvas.setSize(dimens);
			canvas.setPreferredSize(dimens);
		}
		else {
			canvas.setSize(new Dimension(800,705));
			canvas.setPreferredSize(new Dimension(800,705));
		}
		

		// need this so that the scroll bars knows the size of the canvas that
		// has to be
		// scrolled over
		canvas.validate();
	}

	public Canvas getCanvas()
	{
		return canvas;
	}
	
	public CanvasMenu getCanvasMenu() {
		return canvasMenu;
	}

	public JScrollPane getCanvasScrollPane()
	{
		return canvasScrollPane;
	}
	public String getFileName() {
		return filename;
	}
}
