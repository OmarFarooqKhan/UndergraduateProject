package project.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import project.model._Attribute;
import project.model._Relation;

public class _DrawRelation extends Rectangle implements Serializable {
	
	private Color col = Color.white;
	private static final long serialVersionUID = -2858084097073708875L;
	private _Relation relation;
	private String name;
	private List<_DrawConnection> cons;
	private HashMap<_DrawAttribute,Point> attr;
	private HashMap<_DrawCompositeAttribute,Point> compAttr;
	private boolean focused = false;
	public List<_DrawConnection> getConnections() {
		return cons;
	}

	private boolean pressed = false;

	public _DrawRelation(_Relation rel) {	
		 attr = new HashMap<_DrawAttribute,Point>();
		 cons = new ArrayList<_DrawConnection>();
		 compAttr = new HashMap<_DrawCompositeAttribute, Point>();
		 this.relation = rel;
		 setName(rel.getName());
		 height = 30;
		 width = 30;
		 x = 0;
		 y = 0;
	}
	
	public boolean isPressed() {
		return pressed;
	}
	
	public String toString() {
		return name;
	}
	
	public void press() {
		pressed = true;
	}

	public void unpress() {
		pressed = false;
	}
	
	public void setPosition(int xpos, int ypos) {
		this.x = xpos ;
		this.y = ypos ;
	}
	
	public Point getCenter() {
		int x_ = (int) this.getCenterX();
		int y_ = (int) this.getCenterY();
		return new Point(x_,y_);
	}
	public Point getPos() {
		return new Point(x,y);
	}
	
	public _Relation getRelation() {
		return relation;
	}
	
	public void addEntry(_DrawConnection e) {
		cons.add(e);
	}
	
	protected void draw(Graphics g) {
		Graphics2D g2 = (Graphics2D) g;
		g2.rotate(-Math.PI/4, this.getCenterX(), this.getCenterY());
		g2.setColor(Color.GRAY);
		g2.fillRect(x+2,y+4, width, height);
		g2.setColor(Color.BLACK);
		g2.drawRect(x,y, width+1, height+1);
		g2.setColor(col);
		g2.fillRect(x,y, width, height);
		g2.setColor(Color.BLACK);
		int w1 = x + width;
		int h1 =  y + height;
		//g2.drawLine(w1 , h1, (w1)+15, (h1+ 15));

		g2.rotate(Math.PI/4, this.getCenterX(), this.getCenterY());
		g2.drawString(getName(), (w1)+30, (h1 -20));
		
		 update(g);
    }
	
	public void setCol(Color color) {
		col = color;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		relation.setName(name);
		this.name = name;
	}

	public boolean isFocused() {
		return focused;
	}

	public void setFocused(boolean focused) {
		this.focused = focused;
	}
	
	private void update(Graphics g) {
		for(_DrawAttribute attribute: attr.keySet()) {
			attribute.lineUp2(getCenter(),attribute.getLocation() );
			attribute.paint(g);
		}
		for(_DrawCompositeAttribute attribute: compAttr.keySet()) {
			attribute.lineUp2(getCenter(),attribute.getLocation() );
			attribute.paint(g);
		}

	}

	public HashMap<_DrawCompositeAttribute, Point> getCompAttr() {
		return compAttr;
	}


	public void addAttribute(_DrawAttribute attr) {
		attr.lineUp(getCenter(), 1);
		if(attr.isPrimary()) {
			attr.noLongerPrimary();
		}
		relation.attributes.add(attr.getAttribute());
		this.attr.put(attr, attr.getLocation());		
	}
	
	public void addCompAttribute(_DrawCompositeAttribute attr) {
		attr.lineUp(getCenter(), 1);
		
		ArrayList<_Attribute> list = new ArrayList<>();
		
		for(_Attribute attribute:attr.getCompAttribute().getAdditionalAttributes()) {
			list.add(attribute);
		}
		relation.compositeAttributes.put(attr.getCompAttribute(), list);
		
		this.compAttr.put(attr, attr.getLocation());		
	}

	public HashMap<_DrawAttribute,Point> getAttr() {
		return attr;
	}

}
