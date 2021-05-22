package project.controller;

import java.util.ArrayList;
import java.util.HashMap;
import project.model.Model;
import project.view.CanvasView.NewView;
import project.view.DrawableObjects.DrawableAttribute;
import project.view.DrawableObjects.ViewCompositeAttribute;
import project.view.DrawableObjects.ViewStandardAttribute;
import project.model._Attribute;
import project.model._CompositeAttribute;

public class AttributeController {

    private Model __Model;
    private NewView __View;
    public DrawableAttribute __CurrentSelectedAttribute;

    public ArrayList<ViewStandardAttribute> __DrawableObjects;
    public HashMap<ViewStandardAttribute, _Attribute> __AttributeLinkMap;

    public ArrayList<ViewCompositeAttribute> __DrawableCompositeAttributes;
    public HashMap<ViewCompositeAttribute, _CompositeAttribute> __CompositeAttributeLinkMap;

    public ArrayList<DrawableAttribute> __DrawableItems;

    public AttributeController(Model model, NewView view) {
        __Model = model;
        __View = view;

        __DrawableItems = new ArrayList<>();
        __DrawableObjects = new ArrayList<ViewStandardAttribute>();
        __DrawableCompositeAttributes = new ArrayList<>();
        __AttributeLinkMap = new HashMap<>();
        __CompositeAttributeLinkMap = new HashMap<>();
    }

    public AttributeController(Model model) {
        __Model = model;
    }

    public void AddView(NewView view) {
        __View = view;
        
        __DrawableItems = new ArrayList<>();
        __DrawableObjects = new ArrayList<ViewStandardAttribute>();
        __DrawableCompositeAttributes = new ArrayList<>();
        __AttributeLinkMap = new HashMap<>();
        __CompositeAttributeLinkMap = new HashMap<>();
    }

    public void Exit(int exitStatus) {
        System.exit(exitStatus);
    }

    public void CreateNewAttribute() {
        ViewStandardAttribute _ViewAttribute = new ViewStandardAttribute();
        _Attribute _ModelAttribute = new _Attribute(_ViewAttribute.GetName(), java.util.UUID.randomUUID().toString());

        __AttributeLinkMap.put(_ViewAttribute, _ModelAttribute);
        __DrawableObjects.add(_ViewAttribute);
        __View.GetCanvas().repaint();
    }

    public void RemoveAttribute(DrawableAttribute attribute) {
        if (attribute != null) {
            switch (attribute.GetDesignEnum()) {
                case Attribute:
                    __DrawableObjects.contains((ViewStandardAttribute) attribute);

                case CompositeAttribute:
                    __DrawableCompositeAttributes.contains((ViewCompositeAttribute) attribute);

                default:
                    break;
            }

        }
        __AttributeLinkMap.remove(attribute);
        __DrawableObjects.remove(attribute);
    }

    public ArrayList<ViewStandardAttribute> GetDrawableObjects() {
        return __DrawableObjects;
    }

    public ArrayList<DrawableAttribute> GetSelectableObjects() {
        __DrawableItems.clear();
        __DrawableItems.addAll(__DrawableCompositeAttributes);
        __DrawableItems.addAll(__DrawableObjects);

        return __DrawableItems;
    }

    public void CreateCompositeAttribute() {
        ViewStandardAttribute _ViewFirstAttribute = new ViewStandardAttribute();
        _Attribute _ModelFirstAttribute = new _Attribute(_ViewFirstAttribute.GetName(),
                java.util.UUID.randomUUID().toString());

        ViewStandardAttribute _ViewSecondAttribute = new ViewStandardAttribute();
        _Attribute _ModelSecondAttribute = new _Attribute(_ViewSecondAttribute.GetName(),
                java.util.UUID.randomUUID().toString());

        ViewCompositeAttribute _ViewCompositeAttribute = new ViewCompositeAttribute(_ViewFirstAttribute,
                _ViewSecondAttribute);
        _CompositeAttribute _ModelCompositeAttribute = new _CompositeAttribute(_ViewCompositeAttribute.GetName(),
                java.util.UUID.randomUUID().toString());

        _ModelCompositeAttribute.addAdditionalAttribute(_ModelFirstAttribute);
        _ModelCompositeAttribute.addAdditionalAttribute(_ModelSecondAttribute);

        __DrawableObjects.add(_ViewFirstAttribute);
        __DrawableObjects.add(_ViewSecondAttribute);
        __DrawableCompositeAttributes.add(_ViewCompositeAttribute);

        __View.GetCanvas().repaint();
    }

    public DrawableAttribute GetCurrentSelectAttribute() {
        return __CurrentSelectedAttribute;
    }

    public void SetCurrentSelectedAttribute(DrawableAttribute attribute) {
        __CurrentSelectedAttribute = attribute;
    }

    public void WhoIsSelected() {
        GetSelectableObjects().forEach(x -> {
            if (x.IsSelected()) {
                SetCurrentSelectedAttribute(x);
                return;
            }
        });
    }

    public ArrayList<ViewCompositeAttribute> GetCompositeAttributes() {
        return __DrawableCompositeAttributes;
    }
}
