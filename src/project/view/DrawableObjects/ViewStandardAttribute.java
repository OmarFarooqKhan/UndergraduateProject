package project.view.DrawableObjects;
import java.awt.Graphics;

public class ViewStandardAttribute extends DrawStandardAttribute {

	private static String DEFAULT_NAME = "Unnamed";

	public ViewStandardAttribute(String name) {
		super(name);
	}

	public ViewStandardAttribute() {
		super(DEFAULT_NAME);
	}
	
	public void SetPrimaryKey(boolean isPrimaryKey) {
		super.SetPrimaryKey(isPrimaryKey);
	}
	
	public void SetName(String name) { super.SetName(name); }

	public String GetName() { return super.GetName(); }

	public void Paint(Graphics g) { super.paint(g);}
	
	public void SetForeignKey(boolean isForeignKey) { super.SetForeignKey(isForeignKey); }
}
