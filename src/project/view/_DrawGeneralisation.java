package project.view;


import java.awt.*;
import java.awt.geom.Point2D;
import java.io.Serializable;
import java.util.ArrayList;

import project.model._Generalise;


public class _DrawGeneralisation  implements Serializable {
	
    /**
	 * 
	 */
	private static final long serialVersionUID = -7206150454098863699L;
	private final int LINE_TYPE_SIMPLE = 0;
    private final int LINE_START_HORIZONTAL = 0;
    private  final int LINE_ARROW_SOURCE = 1;
 

    private final int LINE_ARROW_WIDTH = 10;
    
    private ArrayList<_DrawSubset> subsets;

 
    
	Point p1;
   
    Point p2;

    int lineType = LINE_TYPE_SIMPLE;
    
    int lineStart = LINE_START_HORIZONTAL;
    
    int lineArrow = LINE_ARROW_SOURCE;
	private _DrawEnt entity;
	private _Generalise gen; 

   

	public _DrawGeneralisation(_DrawEnt entity,_Generalise gen){
    		this.entity = entity;
    		this.setGen(gen);
    		int maxX =(int)entity.getMaxX();
    		int maxY =(int)entity.getMaxY();
    		int minX = (int)entity.getMinX();
    		subsets = new ArrayList<_DrawSubset>();
    		
    		int midX = maxX - minX;
    		midX = midX/2;
    		Point midPoint = new Point(midX+minX, maxY);
    		p1 = midPoint;
    		p2 = new Point(midX+minX, maxY+30);	
    		
    }
    
    public void paint(Graphics2D g2d) {
    		int maxX =(int)entity.getMaxX();
		int maxY =(int)entity.getMaxY();
		int minX = (int)entity.getMinX();
		int midX = maxX - minX;
		midX = midX/2;
		Point midPoint = new Point(midX+minX, maxY);
		p1 = midPoint;
		p2 = new Point(midX+minX, maxY+30);
		for(_DrawSubset sub :subsets) {
			sub.paint(g2d);
		}
		lineArrow =  LINE_ARROW_SOURCE;
		paintSimple(g2d);
		
    }

    protected void paintSimple(Graphics2D g2d) {
        g2d.drawLine(p1.x, p1.y, p2.x, p2.y);
    		paintArrow(g2d, p2, p1);
    }

    protected void paintArrow(Graphics2D g2d, Point p1, Point p2) {
        paintArrow(g2d, p1, p2, getRestrictedArrowWidth(p1, p2));
    }

    protected void paintArrow(Graphics2D g2d, Point p1, Point p2, int width) {
        Point2D.Float pp1 = new Point2D.Float(p1.x, p1.y);
        Point2D.Float pp2 = new Point2D.Float(p2.x, p2.y);
        Point2D.Float left = getLeftArrowPoint(pp1, pp2, width);
        Point2D.Float right = getRightArrowPoint(pp1, pp2, width);

        g2d.drawLine(p2.x, p2.y, Math.round(left.x), Math.round(left.y));
        g2d.drawLine(p2.x, p2.y, Math.round(right.x), Math.round(right.y));
    }


    public int getLineStart() {
        return lineStart;
    }

    public void setLineStart(int start) {
        lineStart = start;
    }

    public int getLineArrow() {
        return lineArrow;
    }

    public void setLineArrow(int arrow) {
        lineType = lineArrow;
    }

    public Point getP1() {
        return p1;
    }

    public void setP1(Point p) {
        p1 = p;
    }

    public Point getP2() {
        return p2;
    }

    public void setP2(Point p) {
        p2 = p;
    }

    protected static Point2D.Float getMidArrowPoint(Point2D.Float p1, Point2D.Float p2, float w) {
        Point2D.Float res = new Point2D.Float();
        float d = Math.round(Math.sqrt( (p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y)));

        if (p1.x < p2.x) {
            res.x = p2.x - w * Math.abs(p1.x - p2.x) / d;
        }
        else {
            res.x = p2.x + w * Math.abs(p1.x - p2.x) / d;
        }

        if (p1.y < p2.y) {
            res.y = p2.y - w * Math.abs(p1.y - p2.y) / d;
        }
        else {
            res.y = p2.y + w * Math.abs(p1.y - p2.y) / d;
        }

        return res;
    }


    private  Point2D.Float getLeftArrowPoint(Point2D.Float p1, Point2D.Float p2, float w) {
        Point2D.Float res = new Point2D.Float();
        double alpha = Math.PI / 2;
        if (p2.x != p1.x) {
            alpha = Math.atan( (p2.y - p1.y) / (p2.x - p1.x));
        }
        alpha += Math.PI / 10;
        float xShift = Math.abs(Math.round(Math.cos(alpha) * w));
        float yShift = Math.abs(Math.round(Math.sin(alpha) * w));
        if (p1.x <= p2.x) {
            res.x = p2.x - xShift;
        }
        else {
            res.x = p2.x + xShift;
        }
        if (p1.y < p2.y) {
            res.y = p2.y - yShift;
        }
        else {
            res.y = p2.y + yShift;
        }
        return res;
    }


    private Point2D.Float getRightArrowPoint(Point2D.Float p1, Point2D.Float p2, float w) {
        Point2D.Float res = new Point2D.Float();
        double alpha = Math.PI / 2;
        if (p2.x != p1.x) {
            alpha = Math.atan( (p2.y - p1.y) / (p2.x - p1.x));
        }
        alpha -= Math.PI / 10;
        float xShift = Math.abs(Math.round(Math.cos(alpha) * w));
        float yShift = Math.abs(Math.round(Math.sin(alpha) * w));
        if (p1.x < p2.x) {
            res.x = p2.x - xShift;
        }
        else {
            res.x = p2.x + xShift;
        }
        if (p1.y <= p2.y) {
            res.y = p2.y - yShift;
        }
        else {
            res.y = p2.y + yShift;
        }
        return res;
    }

    private int getRestrictedArrowWidth(Point p1, Point p2) {
        return Math.min(LINE_ARROW_WIDTH, (int) Math.sqrt( (p1.x - p2.x) * (p1.x - p2.x) + (p1.y - p2.y) * (p1.y - p2.y)));
    }
    public _DrawEnt getEntity() {
		return entity;
	}
    
    public void setEntity(_DrawEnt ent) {
		this.entity = ent;
	}
    public ArrayList<_DrawSubset> getSubsets() {
		return subsets;
	}

	public _Generalise getGen() {
		return gen;
	}

	private void setGen(_Generalise gen) {
		this.gen = gen;
	}
}
