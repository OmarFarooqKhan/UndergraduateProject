package project.view;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.geom.Ellipse2D;
import java.io.Serializable;
import java.util.HashMap;
import java.util.UUID;

import project.model._Attribute;
import project.model._CompositeAttribute;

public class _DrawCompositeAttribute extends _DrawAttribute implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private HashMap<_DrawAttribute,Point> otherAttribs;
	private _CompositeAttribute attribute;

	
	public HashMap<_DrawAttribute, Point> getOtherAttribs() {
		return otherAttribs;
	}
	public _DrawCompositeAttribute(_CompositeAttribute a) {
		super(a);
		this.attribute = a;
		otherAttribs = new HashMap<>();
		this.circle = new Ellipse2D.Double(20, 20, 80, 40);
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		for (_DrawAttribute iterable_element : otherAttribs.keySet()) {
			updateLocation(iterable_element);
			iterable_element.paint(g);
		}
	}
	
	private void updateLocation(_DrawAttribute attribute) {
		int x = (int) (circle.getCenterX() + 40);
		int y = (int) (circle.getCenterY());
		Point point = new Point(x,y);
		attribute.lineUp2(point, attribute.getLocation());
	}
	public _DrawAttribute addComponentAttribute(Point pnt) {
		int x = (int) (pnt.x + 40);
		int y = (int) (pnt.y);
		Point point = new Point(x,y);
		_Attribute attributenew = new _Attribute("dlft",UUID.randomUUID().toString());
		_DrawAttribute compAttribute = new _DrawAttribute(attributenew);
		compAttribute.lineUp(point, point.y-compAttribute.getLocation().y);
		otherAttribs.put(compAttribute, compAttribute.getLocation());
		attribute.addAdditionalAttribute(attributenew);
		
		// System.out.println(compAttribute.circle.getBounds().getLocation());
		return compAttribute;
		
	}
	
	public void addExistingAttributes(_DrawAttribute attr,Point point) {
		attr.setCircle(point);
		attribute.addAdditionalAttribute(attr.getAttribute());
		otherAttribs.put(attr, point);
	}
	
	public _CompositeAttribute getCompAttribute() {
		return attribute;
	}
	

}
