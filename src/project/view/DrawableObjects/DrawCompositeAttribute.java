package project.view.DrawableObjects;

import java.awt.event.MouseEvent;
import java.io.Serializable;
import java.util.HashMap;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Rectangle2D;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;

public class DrawCompositeAttribute extends DrawableAttribute implements Serializable {

    private Ellipse2D __Circle;
    private Line2D __ConnectingLine;
    private DesignEnums __ChoseDesignEnums;

    private int __XPostion;
    private int __YPostion;

    private HashMap<DrawStandardAttribute, Point2D> _Attributes;

    public DrawCompositeAttribute(String name) {
        __NameOfAttribute = name;
        __Circle = new Ellipse2D.Double(20, 20, 80, 40);
        __ConnectingLine = new Line2D.Double(10, 20, 50, 20);

        _Attributes = new HashMap<>();
        SetDesignEnum(DesignEnums.CompositeAttribute);

        __XPostion = 20;
        __YPostion = 10;
    }

    public void Paint(Graphics g) {
        for (DrawStandardAttribute attribute : _Attributes.keySet()) {
            attribute.paint(g);
        }
        Graphics2D g2d = (Graphics2D) g.create();
		g2d.setColor(Color.BLACK);
		g2d.drawString(__NameOfAttribute, (int) __Circle.getCenterX() + 15, (int) (__Circle.getCenterY() + 5)); 
        g2d.draw(__Circle);
    }

    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mousePressed(MouseEvent e) {
        if (SetSelected(__Circle.getBounds().contains(e.getPoint()))) {
			__XPostion = e.getX();
			__YPostion = e.getY();
		}
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseExited(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        if (__Circle.getBounds().contains(e.getPoint())) {
			int dx = e.getX() - __XPostion;
			int dy = e.getY() - __YPostion;

			Point2D ab = __ConnectingLine.getP1();
			Rectangle2D bounds = __Circle.getBounds2D();
			bounds.setFrame(new Rectangle2D.Double(dx + bounds.getX() - 4, dy + bounds.getY() - 4, bounds.getWidth(),
					bounds.getHeight()));
			__Circle.setFrame(bounds);
			__Circle.getBounds().setLocation(e.getPoint());
			__ConnectingLine.setLine(ab, e.getPoint());
			__XPostion += dx;
			__YPostion += dy;
		}
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // TODO Auto-generated method stub

    }

    protected void AddAttribute(DrawStandardAttribute attribute) {
        _Attributes.put(attribute, attribute.getLocation());
    }

}
