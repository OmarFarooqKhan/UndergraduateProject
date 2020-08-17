package project.view;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Stroke;
import java.awt.event.MouseEvent;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Line2D;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.io.Serializable;
import java.util.ArrayList;

import javax.swing.event.MouseInputListener;

import project.model._Attribute;



public class _DrawAttribute implements MouseInputListener, Serializable {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -3499793568559626792L;
	/**
	 * 
	 */
	Ellipse2D circle = new Ellipse2D.Double(20, 20, 20, 20);
	Line2D line = new Line2D.Double(10, 20, 50, 20);
	private Color color = Color.BLACK;
	private boolean pK = false;
	private boolean fK = false;
	private boolean pressed = false;
	private boolean hideLine = false;
	private boolean selected = false;
	private boolean weakened = false;
	double x1; 
	double y1;
	private boolean pls = false;
	private _Attribute attribute;
	private int height = 0;
	private int length = 0;
	private ArrayList<_DrawAttribute> references;
	private ArrayList<_DrawAttribute> identifyingAttributes;

	private String Type = "text";
	private String name;
	private boolean compositeIdentifier;

	public _DrawAttribute(_Attribute a) {
		setAttribute(a);
		 references = new ArrayList<>();
		 identifyingAttributes = new ArrayList<>();
		this.name = a.getName();
	}
	
	public void paint(Graphics g){
		 Graphics2D g2d = (Graphics2D) g.create();
		 g2d.setColor(Color.BLACK);
		 g2d.drawString(name, (int)circle.getCenterX() +15, (int)(circle.getCenterY()+5));
		
		
		 if(!hideLine) {
			 g2d.draw(line);
		 }
		
		 if(compositeIdentifier) {
			 for(int i = 0; i< identifyingAttributes.size(); i++) {
				 
				 if(i+1 -  identifyingAttributes.size() != 0) {
					 
					 identifyingAttributes.get(i+1);
					 g.drawLine(identifyingAttributes.get(i).getMidpoint().x,
							 identifyingAttributes.get(i).getMidpoint().y,
							 identifyingAttributes.get(i+1).getMidpoint().x,
							 identifyingAttributes.get(i+1).getMidpoint().y);
				 }
				 else {
					 g.drawLine(identifyingAttributes.get(i).getMidpoint().x,
							 identifyingAttributes.get(i).getMidpoint().y, this.getMidpoint().x,this.getMidpoint().y);
				 }
			 }
			
		 }
		 if(selected) {
			 g2d.setColor(Color.BLUE);
			 g2d.fill(circle);
		 }
		 if(fK) {
			 g2d.setColor(Color.black);
			 Stroke dashed = new BasicStroke(3, BasicStroke.CAP_BUTT, BasicStroke.JOIN_BEVEL, 0, new float[]{2}, 0);
			 g2d.setStroke(dashed);
			 g2d.draw(circle);
			 return;
		 }
		 g2d.setColor(color);
		 g2d.draw(circle);
		 if(pK) {
			 if(weakened) {
				 String str = "...";
				 for(int i =0; i < name.length();i++) {
					 str+=".";
				 }
				 g2d.drawString(str,(int)circle.getCenterX() +15, (int)(circle.getCenterY()+7));
				 return;
			 }
			 g2d.setColor(Color.black);
			 g2d.fill(circle);
		 }
	}
	
	public void hideLine() {
		hideLine = true;
	}
	
	public void unhide() {
		hideLine = false;
	}
	protected void isPK() {
		pK = !pK;
	}
	
	public void selected() {
		attribute.setPrimaryKey(true);
		pK = true;
	}
	public void unselected() {
		attribute.setPrimaryKey(false);

		pK = false;
	}
	
	public void setCircle(Point p) {
		Rectangle2D bounds = circle.getBounds2D();
        bounds.setFrame(new Rectangle2D.Double(p.x, p.y, bounds.getWidth(), bounds.getHeight()));
        circle.setFrame(bounds);
	}

	public void lineUp(Point p, double e) {
		if(e < 0 ) {
			 line.setLine(p,new Point(p.x+40,p.y+40));
			 Rectangle2D bounds = circle.getBounds2D();
	         bounds.setFrame(new Rectangle2D.Double(p.x+50, p.y+50, bounds.getWidth(), bounds.getHeight()));
	         circle.setFrame(bounds);
			
		}else {
		 line.setLine(p,new Point(p.x-40,p.y-40));
		 Rectangle2D bounds = circle.getBounds2D();
         bounds.setFrame(new Rectangle2D.Double(p.x-50, p.y-50, bounds.getWidth(), bounds.getHeight()));
         circle.setFrame(bounds);
		}
	}
	
	// Used to draw a line from one of the anchor points to our key location.
	public void lineUp2(Point p, Point c) {
		 line.setLine(p, c); 
		 
    }
	
	public Line2D thisRect() {
		return line;
	}
	private Point getMidpoint() {
		int x = 0;
		int y = 0;
		
		x = (int) ((line.getX1() + line.getX2()) / 2) ; 
		y= (int) ((line.getY1() + line.getY2()) / 2) ; 
		
		
		
		return new Point(x,y);
	}
	
	
	
	@Override
	public void mouseClicked(MouseEvent e) {
		if(this.circle.getBounds().contains(e.getPoint())) {
			pls = true;
			x1 = e.getX();
			y1 = e.getY();
			return;
		}
		else {
			pls = false;
		}
	}
	
	public boolean gotHit() {
		return pls;
	} 

	@Override
	public void mousePressed(MouseEvent e) {
			x1 = e.getX();
			y1 = e.getY();
			pressed = true;	
	}

	@Override
	public void mouseReleased(MouseEvent e) {
		pressed = false;
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
		if(pressed) {
			int dx = e.getX() - (int) x1;
			int dy = e.getY() - (int) y1;
			Point2D ab = line.getP1();
			 Rectangle2D bounds = circle.getBounds2D();
	         bounds.setFrame(new Rectangle2D.Double(dx + bounds.getX(),dy + bounds.getY(), bounds.getWidth(), bounds.getHeight()));
	         circle.setFrame(bounds);
	         circle.getBounds().setLocation(e.getPoint());
			 line.setLine(ab, e.getPoint());
			 x1 += dx;
		     y1 += dy;
		}
		
	}
	
	public void setLocation(double x, double y) {
		 Rectangle2D bounds = circle.getBounds2D();
         bounds.setFrame(new Rectangle2D.Double(x + bounds.getX(),y + bounds.getY(), bounds.getWidth(), bounds.getHeight()));
         circle.setFrame(bounds);
	}

	public Point getLocation() {
		int x = (int)circle.getBounds().getCenterX();
		int y = (int)circle.getBounds().getCenterY();
		return new Point(x,y);
	}
	@Override
	public void mouseMoved(MouseEvent e) {
		// TODO Auto-generated method stub
		
	}

	public _Attribute getAttribute() {
		return attribute;
	}

	private void setAttribute(_Attribute attribute) {
		this.attribute = attribute;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int distance) {
		this.height = distance;
	}

	public int getLength() {
		return length;
	}

	public void setLength(int length) {
		this.length = length;
	}
	public String getName() {
		return name;
	}
	public String toString() {
		return name;
	}
	
	public boolean isPressed() {
		return pressed;
	}
	public void unPress() {
		 pressed = false;
	}
	public void setPressed() {
		pressed = true;
	}
	public boolean isPrimary() {
		return pK;
	}
	public boolean isForeign() {
		return fK;
	}
	public void setForeign(boolean key) {
		fK = key;
	}
	
	public void setName(String name) {
		attribute.setName(name.trim());
		this.name = name.trim();
	}

	public boolean isSelected() {
		return selected;
	}
	
	public void setType(int type) {
		switch(type) {
			case(1):
				Type ="text";
				attribute.setType("text");
				break;
			case(2):
				Type ="integer";
				attribute.setType("integer");
				break;
			case(3):
				Type = "boolean";
				attribute.setType("boolean");
				break;
			case(4):
				Type = "decimal";
				attribute.setType("decimal");
				break;
		}
			
	}

	public void setSelected(boolean selected) {
		this.selected = selected;
	}

	public void noLongerPrimary() {
		attribute.setPrimaryKey(false);
		pK = false;
	}
	public void setColor(Color cyan) {
		color = cyan;
		// TODO Auto-generated method stub
	}

	public String getType() {
		// TODO Auto-generated method stub
		return Type;
	}

	public ArrayList<_DrawAttribute> getReferences() {
		return references;
	}
	
	public ArrayList<_DrawAttribute> getCompositeIdentifiers() {
		return identifyingAttributes;
	}
	
	public boolean isCompositeIdentifier() {
		return compositeIdentifier;
	}
	
	
	
	public void setCompositeIdentifier(boolean value) {
		compositeIdentifier = value;
		attribute.setCompositeIdentifier(value);
	}

	public boolean isWeakened() {
		return weakened;
	}
	

	public void setWeakened(boolean weakened) {
		this.weakened = weakened;
	}
	
}
