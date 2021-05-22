package project.view.DrawableObjects;

public interface IDrawableObject {
	public boolean IsSelected();
	public boolean SetSelected(boolean selected);
	public String GetName();
	public void SetName(String name);
	public DesignEnums GetDesignEnum();
	public void SetDesignEnum(DesignEnums designEnums);

}
