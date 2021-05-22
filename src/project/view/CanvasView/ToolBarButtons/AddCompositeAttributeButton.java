package project.view.CanvasView.ToolBarButtons;

import javax.swing.JButton;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import project.controller.AttributeController;

public class AddCompositeAttributeButton extends JButton implements ActionListener {
    private AttributeController __Controller;

    public AddCompositeAttributeButton(AttributeController controller){
        super("Add Composite Attribute");
        __Controller = controller;
        addActionListener(this);
        setPreferredSize(new Dimension(50,20));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       __Controller.CreateCompositeAttribute();
    }
}
