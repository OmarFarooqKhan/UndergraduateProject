package project.view.DrawableObjects;

import javax.swing.event.MouseInputListener;

public abstract class DrawableAttribute implements IDrawableObject, MouseInputListener {

	public boolean __PrimaryKey = false;
	public boolean __ForeignKey = false;
	public String __NameOfAttribute = "";
	public boolean __Selected = false;
	private DesignEnums __ChosenDesignEnum;

	public boolean IsPrimaryKey() {
		return __PrimaryKey;
	}

	public boolean IsForeignKey() {
		return __ForeignKey;
	}

	public void SetForeignKey(boolean isForeignKey) {
		__ForeignKey = isForeignKey;
	}

	public void SetPrimaryKey(boolean isPrimaryKey) {
		__PrimaryKey = isPrimaryKey;
	}

	public boolean IsSelected() {
		return __Selected;
	}

	public boolean SetSelected(boolean isSelected) {
		return __Selected = isSelected;
	}

	public String GetName() {
		return __NameOfAttribute;
	}

	public void SetName(String name) {
		__NameOfAttribute = name;
	}

	@Override
	public DesignEnums GetDesignEnum() {
		return __ChosenDesignEnum;
	}

	@Override
	public void SetDesignEnum(DesignEnums designEnums) {
		__ChosenDesignEnum = designEnums;		
	}
}
