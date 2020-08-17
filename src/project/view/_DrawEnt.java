package project.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

import javax.swing.SwingConstants;

import project.model._Attribute;
import project.model._CompositeAttribute;
import project.model._Entity;

public class _DrawEnt extends Rectangle implements Serializable {
	private static final long serialVersionUID = 6739720759350916375L;
	
	
	public HashMap<_DrawAttribute,Point> attributes;
	public ConcurrentHashMap<_DrawCompositeAttribute, Point> compAttribute;
	private HashMap<_DrawAttribute,Integer> hiddenboxes;
	
	// BOXES FROM AN ENTITY 
	private List<_DrawConnection> box1 = new ArrayList<>();
	private List<_DrawConnection> box2 = new ArrayList<>();
	private List<_DrawConnection> box3 = new ArrayList<>();
	private List<_DrawConnection> box4 = new ArrayList<>();
	
	// BOXES TO AN ENTITY
	private List<_DrawConnection> box5 = new ArrayList<>();
	private List<_DrawConnection> box6 = new ArrayList<>();
	private List<_DrawConnection> box7 = new ArrayList<>();
	private List<_DrawConnection> box8 = new ArrayList<>();

	private int dist = 8;
	private int manage = 0;
	private int resetVal = 0;
	private _Entity entity;
	
	private String name;
	private Color col = Color.WHITE;
	//private _DrawEnt dependancy;
	//private _DrawAttribute ForeignKey;
	private boolean entToGeneralise = false;
	private boolean weakEnt = false;
	private boolean pressed = false;
	private  boolean focused = false;

	int locations[] = { SwingConstants.NORTH, SwingConstants.SOUTH, SwingConstants.EAST, SwingConstants.WEST };
	
	public _DrawEnt(_Entity e) {
		entity = e;
		attributes = new HashMap<>();
		hiddenboxes = new HashMap<>();
		compAttribute = new ConcurrentHashMap<_DrawCompositeAttribute,Point>();
		x = 0;
		y = 0;
		width = 120;
		height = 60;		
		this.setName(entity.getName());
	}
	
	
	public void draw(Graphics g) {
		g.setColor(Color.GRAY);
		g.fillRect(x+3, y+4, width, height);
		g.setColor(Color.BLACK);
		g.fillRect(x, y, width+1, height+1);
		g.setColor(col);
		g.fillRect(x, y, width, height);
		g.setColor(Color.BLACK);
		g.drawString(name, x+28, y +35);
		paintBorder(g);
		g.drawRect(x, y, width, height);
		
		if(weakEnt) {
			paintDepenant(g);
		}
		if(attributes.size() > 0 || compAttribute.size() >0) {
			for(_DrawAttribute att : attributes.keySet()) {
				updateAttr(att,g);
				 att.paint(g);
			}
			for(_DrawCompositeAttribute att : compAttribute.keySet()) {
				att.lineUp2(getBoxAttr(), att.getLocation());
				att.paint(g);
			}
		}
		
	}

	private void paintDepenant(Graphics g) {
		g.drawRect(x + (dist+10) / 2, y + (dist+10) / 2, width - dist - 10, height - dist -10);
	}


	public void paintBorder(Graphics g) {

		g.setColor(Color.BLACK);
		g.drawRect(x + dist / 2, y + dist / 2, width - dist, height - dist);
		for (int i = 0; i < locations.length; i++) {
			Rectangle rect = getRectangle(x, y, width, height, locations[i]);
			g.setColor(Color.WHITE);
			g.fillRect(rect.x, rect.y, rect.width - 1, rect.height - 1);
			g.setColor(Color.BLACK);
			g.drawRect(rect.x, rect.y, rect.width - 1, rect.height - 1);
		}
	}

	 public Point getCursor(Point startPos) {
	        for (int i = 0; i < locations.length; i++) {
	            Rectangle rect = getRectangle(x, y, width, height, locations[i]);
	            if (rect.contains(startPos)) {
	                return new Point((int)rect.getCenterX(),(int)rect.getCenterY()) ;
	            }
	        }
			return null;
	 }
	 
	 public Rectangle getRect(Point startPos) {
	        for (int i = 0; i < locations.length; i++) {
	            Rectangle rect = getRectangle(x, y, width, height, locations[i]);
	            if (rect.contains(startPos)) {
	                return rect;
	            }
	        }
			return null;
	 }
	 
	 // Where we have selected a box for our entity.
	 public boolean withinBox(Point startPos) {
		 for (int i = 0; i < locations.length; i++) {
	            Rectangle rect = getRectangle(x, y, width, height, locations[i]);
	            if (rect.contains(startPos)) {
	            		return true;
	            }
		 }
		 return false;
	 }
	 

	 public void setName(String name) {
		
		 this.name = name.trim();
		 entity.setName(name.trim());
	 }
	 
	 public String getName() {
			return entity.getName();
	}

	private Rectangle getRectangle(int x, int y, int w, int h, int location) {
		switch (location) {

		case SwingConstants.NORTH:
			return new Rectangle(x + w / 2 - dist / 2, y, dist, dist);

		case SwingConstants.SOUTH:
			return new Rectangle(x + w / 2 - dist / 2, y + h - dist, dist, dist);

		case SwingConstants.EAST:
			return new Rectangle(x, y + h / 2 - dist / 2, dist, dist);

		case SwingConstants.WEST:
			return new Rectangle(x + w - dist, y + h / 2 - dist / 2, dist, dist);

		}
		return null;
	}
	
	
	//FROM AN ENTITY 
	public void addConnection(Point point,_DrawConnection d) {
		for (int i = 0; i < locations.length; i++) {
            Rectangle rect = getRectangle(x, y, width, height, locations[i]);
            	if(rect.contains(point) && (locations[i] == SwingConstants.NORTH)) {
            		box1.add(d);
            		break;
            	}
            	if(rect.contains(point) && (locations[i] == SwingConstants.SOUTH)) {
            		box2.add(d);
            		break;
            	}	
            	if(rect.contains(point) && (locations[i] == SwingConstants.EAST)) {
            		box3.add(d);
            		break;
            	}	
            	if(rect.contains(point) && (locations[i] == SwingConstants.WEST)) {
            		box4.add(d);
            		break;
            	}
		}
	}
	
	
	//TO A ENTITY
	public void addConnectionE(Point point,_DrawConnection d) {
		for (int i = 0; i < locations.length; i++) {
            Rectangle rect = getRectangle(x, y, width, height, locations[i]);
            	if(rect.contains(point) && (locations[i] == SwingConstants.NORTH)) {
            		box5.add(d);
            		break;
            	}
            	if(rect.contains(point) && (locations[i] == SwingConstants.SOUTH)) {
            		box6.add(d);
            		break;
            	}	
            	if(rect.contains(point) && (locations[i] == SwingConstants.EAST)) {
            		box7.add(d);
            		break;
            	}	
            	if(rect.contains(point) && (locations[i] == SwingConstants.WEST)) {
            		box8.add(d);
            		break;
            	}
		}
	}
	
	public boolean removeConnection(_DrawConnection e) {
		if (box1.contains(e)) {
			box1.remove(e);
			return true;
		}
		else if (box2.contains(e)) {
			box2.remove(e);
			return true;
		}
		
		else if (box3.contains(e)) {
			box3.remove(e);
			return true;
		}
		
		else if (box4.contains(e)) {
			box4.remove(e);
			return true;
		}
		else if (box5.contains(e)) {
			box5.remove(e);
			return true;
		}
		else if (box6.contains(e)) {
			box6.remove(e);
			return true;
		}
		else if (box7.contains(e)) {
			box7.remove(e);
			return true;
		}
		else  if(box8.contains(e)){
			box8.remove(e);
			return true;
		}
		else {
			return false;
		}
	}
	
	public List<_DrawConnection> entityConnectionBox(_DrawConnection e) {
		if (box1.contains(e)) {
			return box1;
			
		}
		else if (box2.contains(e)) {
			return box2;
		}
		
		else if (box3.contains(e)) {
			return box3;
		}
		
		else if (box4.contains(e)) {
			return box4;
		}
		else if (box5.contains(e)) {
			return box5;
		}
		else if (box6.contains(e)) {
			return box6;
		}
		else if (box7.contains(e)) {
			return box7;
		}
		else  if(box8.contains(e)){
			return box8;
		}
		else {
			return null;
		}
	}
	
	public void updatePoints() {
		for(_DrawConnection conn: box1) {
			  Rectangle rect = getRectangle(x, y, width, height, SwingConstants.NORTH);
			  conn._updateStart(new Point((int) rect.getCenterX(),(int) rect.getCenterY()), this);
		}
		for(_DrawConnection conn: box2) {
			  Rectangle rect = getRectangle(x, y, width, height, SwingConstants.SOUTH);
			  conn._updateStart(new Point((int) rect.getCenterX(),(int) rect.getCenterY()), this);
		}
		for(_DrawConnection conn: box3) {
			  Rectangle rect = getRectangle(x, y, width, height, SwingConstants.EAST);
			  conn._updateStart(new Point((int) rect.getCenterX(),(int) rect.getCenterY()), this);
		}
		for(_DrawConnection conn: box4) {
			  Rectangle rect = getRectangle(x, y, width, height, SwingConstants.WEST);
			  conn._updateStart(new Point((int) rect.getCenterX(),(int) rect.getCenterY()), this);
		}
		
	}
	
	public void updateEnds() {
		for(_DrawConnection conn: box5) {
			  Rectangle rect = getRectangle(x, y, width, height, SwingConstants.NORTH);
			  conn._updateEnd(new Point((int) rect.getCenterX(),(int) rect.getCenterY()), this);
		}
		for(_DrawConnection conn: box6) {
			  Rectangle rect = getRectangle(x, y, width, height, SwingConstants.SOUTH);
			  conn._updateEnd(new Point((int) rect.getCenterX(),(int) rect.getCenterY()), this);
		}
		for(_DrawConnection conn: box7) {
			  Rectangle rect = getRectangle(x, y, width, height, SwingConstants.EAST);
			  conn._updateEnd(new Point((int) rect.getCenterX(),(int) rect.getCenterY()), this);
		}
		for(_DrawConnection conn: box8) {
			  Rectangle rect = getRectangle(x, y, width, height, SwingConstants.WEST);
			  conn._updateEnd(new Point((int) rect.getCenterX(),(int) rect.getCenterY()), this);
		}
	}
	public String getCenters() {
		String pos = "-";
		 for (int i = 0; i < locations.length; i++) {
	            Rectangle rect = getRectangle(x, y, width, height, locations[i]);
	            pos += " " + rect.getLocation();
	     }
		 return pos;
	}
	
	
	public void addAttribute(_DrawAttribute attr,Point p) {
		if(manage < 4 ) {
            Rectangle rect = getRectangle(x, y, width, height, locations[manage]);   
			attr.lineUp(rect.getLocation(),  p.y - rect.getLocation().getY());
			entity.getAttributes().add(attr.getAttribute());
			attr.setHeight((int) (attr.getLocation().y - rect.getBounds().getCenterY()));
			attr.setLength((int) (attr.getLocation().x-  rect.getBounds().getCenterX()));
			attributes.put(attr, new Point((int)rect.getCenterX(),(int)rect.getCenterY()));
			hiddenboxes.put(attr, manage);
			manage++;

            return;
		}
		else {
			manage = 0;
			Rectangle rect = getRectangle(x, y, width, height, locations[manage]);
			attr.lineUp(rect.getLocation(),p.y - rect.getLocation().getY());
			attributes.put(attr, rect.getLocation());
			entity.getAttributes().add(attr.getAttribute());
            hiddenboxes.put(attr, manage);
			manage++;

		}

	}
	
	public void expandAttribute(_DrawAttribute attr,Point p) {
		entity.getAttributes().add(attr.getAttribute());
		Rectangle rect = getRect(p);
		hiddenboxes.put(attr, manage);
		attr.lineUp(rect.getLocation(),  p.y - rect.getLocation().getY());
		attributes.put(attr, new Point((int)rect.getCenterX(),(int)rect.getCenterY()+5));
		
	}
	
	public void addCompAttr(_DrawCompositeAttribute comp) {
		int x = getLocation().x;
		int y = getLocation().y;
		Point p = new Point(x + 50, y -100);
		
		_CompositeAttribute coNew = comp.getCompAttribute();
		ArrayList<_Attribute> list = new ArrayList<>();
		
		for(_Attribute attribute:coNew.getAdditionalAttributes()) {
			list.add(attribute);
		}
		
		entity.getCompositeAttributes().put(coNew, list);
				
		comp.setCircle(p);
		comp.lineUp(getBoxAttr(),getBoxAttr().y-getBoxAttr().y -2);
		compAttribute.put(comp, getBoxAttr());
	}
	
	
	private void updateAttr(_DrawAttribute d,Graphics g) {
		Rectangle rect = getRectangle(x, y, width, height, locations[hiddenboxes.get(d)]); //The location of the box.
			
			d.lineUp2(
					new Point(
					(int) rect.getBounds().getCenterX(),
					(int) rect.getBounds().getCenterY()),
					d.getLocation());	
			
			attributes.replace(d, rect.getLocation());
	}

	public String toString() {
		return name;
	}

	public Point getBox() {
		Point q = new Point(0,0);
		if(resetVal < 4) {
			Rectangle rect = getRectangle(x, y, width, height, locations[resetVal]);
			 q = new Point((int)rect.getCenterX(),(int)rect.getCenterY());
			 resetVal++;
		}
		else {
			 resetVal=0;
			Rectangle rect = getRectangle(x, y, width, height, locations[resetVal]);
			 q = new Point((int)rect.getCenterX(),(int)rect.getCenterY());
		}
		return q;
	}
	
	public Point getClosestPoint(Point connectingTo) {
		Point con = connectingTo;
		Rectangle rect =  null;
		Rectangle best =  null;
		Point q ;
		double distance  = 0;
		for (int i = 0; i < locations.length; i++) {
			rect = getRectangle(x, y, width, height, locations[i]);
			
			q = new Point((int)rect.getCenterX(),(int)rect.getCenterY());
			if(best == null ) {
				distance = q.distance(con.getX(), con.getY());
			//	System.out.println(distance + "First");
				best = rect;
			}
			else {
				if(distance > q.distance(con.getX(), con.getY())) {
					distance = q.distance(con.getX(), con.getY());
					best = rect;
				//	System.out.println(distance + " "+ i);

					distance = q.distance(con.getX(), con.getY());
				}
			}
		}
		Point closest = new Point((int)rect.getCenterX(),(int)rect.getCenterY());
		return closest;
	}
	
	
	private Point getBoxAttr() {
		
		Rectangle rect = getRectangle(x, y, width, height, locations[3]);
		Point q = new Point((int)rect.getCenterX(),(int)rect.getCenterY());
		return q;
	}
	
	public void press() {
		pressed = true;
	}

	public void unpress() {
		pressed = false;
	}

	public boolean isPressed() {
		return pressed;
	}
	public _Entity getEntity() {
		return entity;
	}

	public boolean isFocused() {
		return focused;
	}

	public void setFocused(boolean focused) {
		this.focused = focused;
	}
	
	public void setWeakEnt(boolean b) {
		weakEnt = b;
	}
	public boolean isWeakEnt() {
		return weakEnt;
	}

	
	public List<_DrawConnection> totalConnections(){
		List<_DrawConnection> myConnections = new ArrayList<>();
		
		myConnections.addAll(box1);
		myConnections.addAll(box2);
		myConnections.addAll(box3);
		myConnections.addAll(box4);
		myConnections.addAll(box5);
		myConnections.addAll(box6);
		myConnections.addAll(box7);
		myConnections.addAll(box8);
		
		return myConnections;
		
	}
	public Point getSouthBox() {

		Rectangle rect = getRectangle(x, y, width, height, locations[1]);
		Point q = new Point((int)rect.getCenterX(),(int)rect.getCenterY());
		return q;
	}

	public void setCol(Color color) {
		col = color;
	}
	public void resetColor() {
		col = Color.WHITE;
	
	}

	// Method to avoid having more than 1 primary key per entity.
	public void primaryKeyReset() {
		for(_DrawAttribute attributeToUnselect :attributes.keySet()) {
			attributeToUnselect.noLongerPrimary();
		}
		for(_DrawCompositeAttribute attributeToUnselect: compAttribute.keySet()) {
			attributeToUnselect.noLongerPrimary();
		}
	}

	public boolean isEntToGeneralise() {
		return entToGeneralise;
	}


	public void setEntToGeneralise(boolean entToGeneralise) {
		this.entToGeneralise = entToGeneralise;
	}
}
