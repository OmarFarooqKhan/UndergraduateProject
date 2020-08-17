package project.view;


import project.controller.Controller;
import project.model.Model;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.List;

import javax.swing.*;

/**
 * User: Alan P. Sexton Date: 20/06/13 Time: 18:00
 */
public class Canvas extends JPanel
{

	/**
	 * 
	 */
	private static final long	serialVersionUID	= -7552020673193783095L;
	private Model				model;
	private View					view;
	private Controller          controller;
	private CanvasMouseListener mouseListener;
	private CanvasToolbar ctb;
	
	public CanvasToolbar getCtb() {
		return ctb;
	}

	/**
	 * The default constructor should NEVER be called. It has been made private
	 * so that no other class can create a Canvas except by initialising it
	 * properly (i.e. by calling the parameterised constructor)
	 */
	@SuppressWarnings("unused")
	private Canvas()
	{
	}

	/**
	 * Create a <code>Canvas</code> object initialised to the given
	 * <code>View</code> and <code>Model</code>
	 *
	 * @param view
	 *            The View object that encapsulates the whole GUI
	 * @param model
	 *            The Model object that encapsulates the (view-independent) data
	 *            of the application
	 * @param controller
	 *            The Controller object that handles all operations
	 */
	protected Canvas(Model model, View view, Controller controller)
	{
		this.view = view;
		this.model = model;
		this.controller = controller;
		
		ctb = new CanvasToolbar(model, view, controller);

		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.NORTHEAST;

		this.setLayout(new GridBagLayout());
	
		// keyListener = new CanvasKeyListener(this.model, this.view, controller, this);
		mouseListener = new CanvasMouseListener(this.model, this.view,
				controller,this,ctb);
		
		setFocusable(true);
		// addKeyListener(keyListener);
		addMouseListener(mouseListener);
		setBackground(Color.white);
		setPreferredSize(new Dimension(800,705));

		//ctb.setVisible(false);
		this.add(ctb, gbc);

	}
	
	/**
	 * The method that is called to paint the contents of this component
	 *
	 * @param g
	 *            The <code>Graphics</code> object used to do the actual drawing
	 */
	
	
	protected void paintComponent(Graphics g){
		super.paintComponent(g);
		List<_DrawEnt> ents = controller.getDrawEnts();
		List<_DrawRelation> rel = controller.getDrawRelations();
		List<_DrawConnection> con = controller.getDrawConnections();
		List<_DrawAttribute> attr = controller.getAttributes();
		List <_DrawCompositeAttribute> comp = controller.getCompAttr();
		List<_DrawGeneralisation> gen = controller.getGeneralisations();

		for(_DrawConnection connection : con){
			connection.draw(g);
		}
		for (_DrawEnt ent : ents){
			ent.draw(g);
		}
		
		for (_DrawRelation relation : rel){
			relation.draw(g);
		}
		for (_DrawAttribute at : attr){
			at.paint(g);
		}
		
		for (_DrawAttribute at : attr){
			at.paint(g);
		}
		for (_DrawCompositeAttribute com : comp) {
			com.paint(g);
		}
		for(_DrawGeneralisation generalisation :gen) {
			generalisation.paint((Graphics2D) g);
		}
		
		mouseListener.paint(g);
	}
	public CanvasToolbar canvasToolBar() {
		return this.ctb;
	}
}


