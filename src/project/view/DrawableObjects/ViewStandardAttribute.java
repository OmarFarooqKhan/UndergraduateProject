package project.view.DrawableObjects;

public class ViewStandardAttribute extends DrawStandardAttribute {

	public ViewStandardAttribute(String name) {
		super(name);
	}
	
	public void SetPrimaryKey(boolean isPrimaryKey) {
		super.SetPrimaryKey(isPrimaryKey);
	}
	
	public void SetName(String name) { super.SetName(name); }
	
	public void SetForeignKey(boolean isForeignKey) { super.SetForeignKey(isForeignKey); }
}
