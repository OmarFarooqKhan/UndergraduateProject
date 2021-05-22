package project.view.CanvasView;

import project.controller.AttributeController;
import project.view.DrawableObjects.DrawableAttribute;
import project.view.DrawableObjects.ViewCompositeAttribute;
import project.view.DrawableObjects.ViewStandardAttribute;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.util.ArrayList;

import javax.swing.*;


public class NewCanvas extends JPanel {
	private static final long serialVersionUID = -7552020673193783095L;
	private NewView view;
	private AttributeController controller;
	private NewCanvasMouseListener mouseListener;

	@SuppressWarnings("unused")
	private NewCanvas() {
	}

	protected NewCanvas(NewView newView, AttributeController controller) {
		this.view = newView;
		this.controller = controller;
		mouseListener = new NewCanvasMouseListener(controller, this);
		addMouseListener(mouseListener);
		addMouseMotionListener(mouseListener);
		InitialiseCanvas();
	}

	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		ArrayList<ViewStandardAttribute> _StandardAttributes = controller.GetDrawableObjects();
		ArrayList<ViewCompositeAttribute> _CompositeAttributes = controller.GetCompositeAttributes();

		if(_CompositeAttributes != null) {
			for(ViewCompositeAttribute _Composite : _CompositeAttributes){
				_Composite.Paint(g);
			}
		}
		if (_StandardAttributes != null) {
			for (ViewStandardAttribute at : _StandardAttributes) {
				at.Paint(g);
			}
		}

		// mouseListener.paint(g);
	}

	private void InitialiseCanvas() {

		GridBagConstraints gbc = new GridBagConstraints(0, 0, 1, 1, 1.0, 1.0, GridBagConstraints.NORTHWEST,
				GridBagConstraints.NONE, new Insets(0, 0, 0, 0), 0, 0);
		gbc.gridx = 1;
		gbc.gridy = 0;
		gbc.anchor = GridBagConstraints.NORTHEAST;

		this.setLayout(new GridBagLayout());

		setFocusable(true);
		setBackground(Color.white);
		setPreferredSize(new Dimension(800, 705));
	}
}
