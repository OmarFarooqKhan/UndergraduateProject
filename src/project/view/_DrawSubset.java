package project.view;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;
import java.io.Serializable;

import project.model._Subset;

public class _DrawSubset  implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1668025124991108175L;
	private _DrawEnt entity;


	private _DrawGeneralisation gen;
	private _Subset sub;
	
	public _DrawSubset(_DrawEnt entity,_DrawGeneralisation generalisation,_Subset sub) {
		this.setSub(sub);
		this.entity = entity;
		this.gen = generalisation;
	}
	
	
	public void paint(Graphics g) {
		int minx = (int)entity.getMinX();
		int miny = (int)entity.getMaxY();
		int maxx = (int)entity.getMaxX();
		int midX = (maxx - minx)/2;
		Point endpoint = gen.getP2();
		
		
		Point e = new Point(minx,miny);
		Point p = entity.getLocation();
		
		g.setColor(Color.BLUE);

	
		
		if( (miny- endpoint.y) > p.y - endpoint.y && (miny - endpoint.y) > 0 ) {
			g.drawLine(p.x + midX, p.y, p.x + midX, endpoint.y ); // vertical
			g.drawLine(p.x + midX, endpoint.y, endpoint.x, endpoint.y); // horizontal
		
		}
		else {
			g.drawLine(e.x + midX, e.y, e.x + midX, endpoint.y);
			g.drawLine(e.x + midX, endpoint.y, endpoint.x, endpoint.y); // horizontal
		}
	
	}
	
	public _DrawEnt getEntity() {
		return entity;
	}


	public _Subset getSub() {
		return sub;
	}


	private void setSub(_Subset sub) {
		this.sub = sub;
	}

}
