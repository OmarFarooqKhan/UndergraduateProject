package project.view.CanvasView;

import javax.swing.BorderFactory;
import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JToolBar;

import java.awt.Color;

import java.awt.BorderLayout;
import java.awt.Dimension;


import project.controller.AttributeController;
import project.model.Model;
import project.view.CanvasView.ToolBarButtons.AddCompositeAttributeButton;
import project.view.CanvasView.ToolBarButtons.AddStandardAttributeButton;
import project.view.CanvasView.ToolBarButtons.RemoveObjectButton;

public class NewCanvasToolbar extends JPanel {
    
    Model model;
	NewView view;
	AttributeController __Controller;
    public JTextField textArea;
    JToolBar tb;
	JButton exit;
	JButton attributeButton;
	JButton delete;
	JPanel jp;
	// JComboBox<DrawStandardAttribute> attributeList;  // Used to show attribute

    public NewCanvasToolbar(Model model, NewView view, AttributeController controller) {
		this.view = view;
		this.model = model;
		__Controller = controller;
		InitialiseCanvasToolbar();
    }

	private void InitialiseCanvasToolbar(){
		setBorder(BorderFactory.createLineBorder(Color.BLACK));
		setLayout(new BorderLayout());
		setPreferredSize(new Dimension(150, 60));

		SetUpButtons();
	}

	private void SetUpButtons(){
		AddStandardAttributeButton _StandardAttributeButton = new AddStandardAttributeButton(__Controller);
		AddCompositeAttributeButton _CompositeAttributeButton = new AddCompositeAttributeButton(__Controller);
		RemoveObjectButton _RemoveObjectButton = new RemoveObjectButton(__Controller);

		add(_StandardAttributeButton, BorderLayout.NORTH);
		add(_CompositeAttributeButton, BorderLayout.CENTER);
		add(_RemoveObjectButton, BorderLayout.SOUTH);
	}
}
