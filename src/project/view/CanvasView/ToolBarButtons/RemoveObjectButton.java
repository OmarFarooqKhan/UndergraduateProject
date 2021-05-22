package project.view.CanvasView.ToolBarButtons;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JButton;
import project.controller.AttributeController;

public class RemoveObjectButton extends JButton implements ActionListener {
    private AttributeController __Controller;

    public RemoveObjectButton(AttributeController controller){
        super("Remove Item");
        __Controller = controller;
        addActionListener(this);
        setPreferredSize(new Dimension(50,20));
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        __Controller.WhoIsSelected();
        if(__Controller.GetCurrentSelectAttribute() != null){
            setEnabled(true);
            __Controller.RemoveAttribute(__Controller.GetCurrentSelectAttribute());   
        }
        else{
            setEnabled(false);
        }
    }
    
}
