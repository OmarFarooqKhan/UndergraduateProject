package project.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.Serializable;

import project.model._Connection;

public class _DrawConnection implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Point start = null;
	Point end = null;
	
	private _DrawEnt startEnt;
	private _DrawRelation d;
	private _Connection connection;

	private String card1 = "(1,1)";
	private int position_x = 0; 
	private int position_y = 0;
	private int relation_x = 0;
	private int relation_y = 0;
	int from;
	int to;
	
	public _DrawConnection(Point start, _DrawEnt e) {
		this.start= start;
		startEnt = e;
	}
		
	public void addSingleConnection(_DrawEnt e, Point startPoint ,_DrawRelation r) {
		start.x = (int)startPoint.getX();  // Sets the start Point of the boxed entity 
		start.y = (int)startPoint.getY();
		startEnt = e;   // Holds the info of the entity
		d = r;			 // Holds the info of the relation
		end = d.getPos(); // draw a line to a relation
		
	}
	
	public void _updateStart(Point p,_DrawEnt ent) {
		this.start = p;
	}
	
	public void _updateEnd(Point p,_DrawEnt ent) {
		this.start = p;
	}
	
	public void addRelation(_DrawRelation r) {
		d = r;
	}

	public _DrawEnt getStartEnt() {
		return startEnt;
	}
	public void setStartEnt(_DrawEnt entity) {
		connection.setEntity(entity.getEntity());
		startEnt = entity;
	}
	
	public void setCard(String card) {
		
		card1 = card;
		connection.setCard(card);
	}
	
	public _DrawRelation getRelation() {
		return d;
	}
	
//	private void setUpLine(Graphics g) {
//		double averageX = (start.x + d.getX())/2;
//		double averageY = (start.y + d.getY())/2;
//		double sX = start.x;
//		double sY = start.y;
//	
//		double pythagx = (sX * sX) + (averageX *averageX);
//		double pythagy = (sY * sY) + (averageY *averageY);
//		
// 		
//		pythagx = Math.sqrt(pythagx);
//		pythagy = Math.sqrt(pythagy);
//		
//		g.setColor(Color.red); 										// Testing for line smoothness
//		g.drawLine(start.x, start.y, (int)pythagx, (int)pythagy);
//		
//		g.setColor(Color.BLUE);
//		g.drawLine(start.x, start.y, (int)averageX, (int)averageY);
//		
//		g.setColor(Color.green);
//		
//		double pythagcx = (sX * sX) + (averageX *averageX);
//		double pythagcy = (sY * sY) + (averageY *averageY);
//		
//		pythagcx = Math.sqrt(pythagcx);
//		pythagcy = Math.sqrt(pythagcy);
//		
//		g.drawLine((int)averageX, (int)averageY,(int)pythagcx ,(int)pythagcy );
//		
//	}
	public Point getStartPos() {
		return start;
	}
	public String getCard() {
		return card1;
	}
	public void setConnection(_Connection connection) {
		this.connection = connection;
		
	}
	public _Connection getConnection() {
		return connection;
	}
	public void draw(Graphics g){
		g.setColor(Color.BLACK);
		
		position_x = start.x ;
		position_y = start.y ;
		
		relation_x = (int) d.getCenterX();
		relation_y =  (int) d.getCenterY();
		
		g.drawLine(position_x,position_y,position_x,relation_y);
		g.drawLine(position_x,relation_y, relation_x, relation_y);
		g.drawString(card1, (position_x+relation_x)/2,(relation_y) +25);	
	}
	
	
	
	

}
