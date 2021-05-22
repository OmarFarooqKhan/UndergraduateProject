package project.view.DrawableObjects;

import java.awt.Graphics;
import java.util.ArrayList;
import java.util.List;

public class ViewCompositeAttribute extends DrawCompositeAttribute {

    public List<ViewStandardAttribute> _Attributes;

    public ViewCompositeAttribute(String name) {
        super(name);
        _Attributes = new ArrayList<ViewStandardAttribute>();
    }

    public ViewCompositeAttribute(){
        super("Unmapped");
        _Attributes = new ArrayList<ViewStandardAttribute>();
    }

    public ViewCompositeAttribute(ViewStandardAttribute firstAttribute, ViewStandardAttribute secondAttribute){
        super("Unmapped");
        _Attributes = new ArrayList<ViewStandardAttribute>();
        _Attributes.add(firstAttribute);
        _Attributes.add(secondAttribute);

        AddNewAttribute(firstAttribute);
        AddNewAttribute(secondAttribute);
    }
    
    public void SetPrimaryKey(boolean isPrimaryKey) {
		super.SetPrimaryKey(isPrimaryKey);
	}
	
    public void AddNewAttribute(ViewStandardAttribute attribute){
        super.AddAttribute(attribute);
    }

	public void SetName(String name) { super.SetName(name); }

	public String GetName() { return super.GetName(); }

	public void Paint(Graphics g) { super.Paint(g);}
	
	public void SetForeignKey(boolean isForeignKey) { super.SetForeignKey(isForeignKey); }
}
