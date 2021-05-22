package project.view.DrawableObjects;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Point;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;

public class DrawStandardAttribute extends DrawableAttribute implements Serializable {

	private static final long serialVersionUID = 1L;

	private Ellipse2D __Circle;
	private Line2D __ConnectingLine;
	private DesignEnums __ChosenDesignEnum; 

	private int __XPostion;
	private int __YPostion;

	public DrawStandardAttribute(String name) {
		__NameOfAttribute = name;

		__Circle = new Ellipse2D.Double(20, 20, 20, 20);
		__ConnectingLine = new Line2D.Double(10, 20, 50, 20);

		__XPostion = 20;
		__YPostion = 10;

		SetDesignEnum(DesignEnums.Attribute);
	}

	public void paint(Graphics g) {

		Graphics2D g2d = (Graphics2D) g.create();
		g2d.setColor(Color.BLACK);
		g2d.drawString(__NameOfAttribute, (int) __Circle.getCenterX() + 15, (int) (__Circle.getCenterY() + 5));

		if (IsSelected()) {
			g2d.setColor(Color.BLUE);
			g2d.fill(__Circle);
			SetCirclePosition();
		}

		if (IsForeignKey()) {
			g2d.setColor(Color.black);
			Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[] { 2 }, 0);
			g2d.setStroke(dashed);
			g2d.draw(__Circle);
			return;
		}

		g2d.draw(__Circle);
	}

	@Override
	public boolean IsPrimaryKey() {
		return __PrimaryKey;
	}

	@Override
	public void SetPrimaryKey(boolean isPrimaryKey) {
		__PrimaryKey = isPrimaryKey;
	}

	@Override
	public boolean IsForeignKey() {
		return __ForeignKey;
	}

	@Override
	public void SetForeignKey(boolean isForeignKey) {
		__ForeignKey = isForeignKey;
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
	}

	@Override
	public void mouseEntered(MouseEvent e) {
	}

	@Override
	public void mouseExited(MouseEvent e) {
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
	}

	@Override
	public void mouseClicked(MouseEvent e) {
	}

	public Point getLocation() {
		int x = (int)__Circle.getBounds().getCenterX();
		int y = (int)__Circle.getBounds().getCenterY();
		return new Point(x,y);
	}

	private void SetCirclePosition() {
		Rectangle2D bounds = __Circle.getBounds2D();
        bounds.setFrame(new Rectangle2D.Double(__XPostion, __YPostion, bounds.getWidth(), bounds.getHeight()));
        __Circle.setFrame(bounds);
	}

	@Override
	public DesignEnums GetDesignEnum() {
		return __ChosenDesignEnum;
	}

	@Override
	public void SetDesignEnum(DesignEnums designEnums) {
		__ChosenDesignEnum = designEnums;		
	}

}