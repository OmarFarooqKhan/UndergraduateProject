package project.view.CanvasView;

import project.controller.AttributeController;
import project.model._CompositeAttribute;
import project.view.DrawableObjects.*;

import javax.swing.event.MouseInputAdapter;
import javax.swing.event.MouseInputListener;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.MouseEvent;


/**
 * User: Alan P. Sexton Date: 21/06/13 Time: 00:52
 */
public class NewCanvasMouseListener extends MouseInputAdapter {
    AttributeController __Controller;
    NewCanvas __Canvas;

    double x1;
    double y1;
    int mouse1;
    int mouse2;
    int mouse3;
    int mouse4;
    boolean mouseDown = false;

    public NewCanvasMouseListener(AttributeController controller, NewCanvas canvas) {
        __Controller = controller;
        __Canvas = canvas;
    }

    public void paint(Graphics g) {
        if (mouseDown && (mouse1 > 0 && mouse2 > 0)) {
            g.setColor(Color.BLACK);
            g.drawLine(mouse1, mouse2, mouse3, mouse4);
        }
    }

    @Override
    public void mousePressed(MouseEvent e) {
        for (ViewStandardAttribute drawableAttribute : __Controller.GetDrawableObjects()) {
            drawableAttribute.mousePressed(e);
        }

        for (ViewCompositeAttribute drawableCompositeAttribute : __Controller.GetCompositeAttributes()) {
            drawableCompositeAttribute.mousePressed(e);
        }
        __Canvas.repaint();
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        for (ViewStandardAttribute drawableAttribute : __Controller.GetDrawableObjects()) {
            drawableAttribute.mouseDragged(e);
        }

        for (ViewCompositeAttribute drawableCompositeAttribute : __Controller.GetCompositeAttributes()) {
            drawableCompositeAttribute.mouseDragged(e);
        }
        __Canvas.repaint();
    }

    @Override
    public void mouseClicked(MouseEvent e){
        for(DrawableAttribute drawableAttribute: __Controller.GetSelectableObjects()){
            if(drawableAttribute.IsSelected()){
                __Controller.SetCurrentSelectedAttribute(drawableAttribute);
                break;
            }
        }
        __Controller.SetCurrentSelectedAttribute(null);
    }


}
