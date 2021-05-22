package project.view.CanvasView;

import project.controller.Controller;
import project.model.Model;
import project.model._Relation;
import project.view.CanvasToolbar;
import project.view.View;
import project.view._DrawAttribute;
import project.view._DrawCompositeAttribute;
import project.view._DrawConnection;
import project.view._DrawEnt;
import project.view._DrawRelation;
import project.view.DrawableObjects.*;

import javax.swing.event.MouseInputListener;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.util.ArrayList;
import java.util.List;

/**
 * User: Alan P. Sexton Date: 21/06/13 Time: 00:52
 */
public class NewCanvasMouseListener implements MouseInputListener

{
	Model model;
	View view;
	Controller controller;
	NewCanvas canvas;

	double x1;
	double y1;
	int mouse1;
	int mouse2;
	int mouse3;
	int mouse4;
	boolean mouseDown = false;

    private List<DrawableAttribute> __DrawableObjects;

	CanvasToolbar ctb;

	public NewCanvasMouseListener(Model model, View view, Controller controller, NewCanvas canvas, CanvasToolbar ctb) {
		this.model = model;
		this.view = view;
		this.controller = controller;
		this.canvas = canvas;
		this.ctb = ctb;
        __DrawableObjects = new ArrayList<DrawableAttribute>();
        ctb.setVisible(false);
	}

	public void paint(Graphics g) {
		if (mouseDown && (mouse1 > 0 && mouse2 > 0)) {
			g.setColor(Color.BLACK);
			g.drawLine(mouse1, mouse2, mouse3, mouse4);
		}
	}

    @Override
    public void mouseClicked(MouseEvent e) {
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (DrawableAttribute drawableAttribute : __DrawableObjects) {
            drawableAttribute.mousePressed(e);
        }
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void mouseMoved(MouseEvent e) {
    }
}
