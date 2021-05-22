package project.view.CanvasView.ToolBarButtons;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import project.controller.AttributeController;

public class AddStandardAttributeButton extends JButton implements ActionListener {
    
    private AttributeController __Controller;
    public AddStandardAttributeButton(AttributeController controller){
        super("Add Standard Attribute");
        __Controller = controller;
        addActionListener(this);
        setPreferredSize(new Dimension(50,20));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
       __Controller.CreateNewAttribute();
    }
}
