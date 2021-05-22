package project.view.CanvasView;

import project.controller.AttributeController;
import project.model.Model;
import project.view.actions.ExitAction;

import project.view.HVMouseWheelScrollPane;

import javax.swing.*;
import javax.swing.border.EmptyBorder;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * User: Alan P. Sexton Date: 21/06/13 Time: 13:42
 */ 
public class NewView extends JFrame {
    /**
     * 
     */
    private static final long serialVersionUID = -6963519874728205328L;
    private NewCanvas __Canvas;
    private JScrollPane __CanvasScrollPane;
    private NewCanvasToolbar __CanvasToolbar;
    private String filename = "";
    private AttributeController __AttributeController;

    public NewView(Model model, AttributeController controller) {
        super("Database Designer");

        __AttributeController = controller;
        __AttributeController.AddView(this);

        // We will use the default BorderLayout, with a scrolled panel in
        // the centre area, a tool bar in the NORTH area and a menu bar
        
        __CanvasToolbar = new NewCanvasToolbar(model, this, controller);
        __Canvas = new NewCanvas(this, controller);
        __CanvasScrollPane = new HVMouseWheelScrollPane();

        // The default when scrolling is very slow. Set the increment as
        // follows:
        __CanvasScrollPane.getVerticalScrollBar().setUnitIncrement(16);
        __CanvasScrollPane.getHorizontalScrollBar().setUnitIncrement(16);
        __CanvasScrollPane.setViewportView(__Canvas);

        getContentPane().add(__CanvasScrollPane, BorderLayout.CENTER);
        getContentPane().add(__CanvasToolbar, BorderLayout.NORTH);

        // exitAction has to be final because we reference it from within
        // an inner class

        final AbstractAction exitAction = new ExitAction(this, controller);

        setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
        addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent we) {
                exitAction.actionPerformed(null);
            }
        });

        // Set up the menu bar
        JMenu fileMenu;
        fileMenu = new JMenu("File");
        fileMenu.add(exitAction);

        JMenuBar menuBar;

        menuBar = new JMenuBar();
        menuBar.add(fileMenu);

        setJMenuBar(menuBar);

        JToolBar toolBar;
        toolBar = new JToolBar(null, JToolBar.VERTICAL);
        toolBar.setFloatable(false);
        toolBar.setRollover(true);
        toolBar.add(exitAction).setBorder(new EmptyBorder(5, 0, 5, 0));
        pack();

        setBounds(0, 0, 1000, 900);
    }

    public void adaptToNewImage(int num) {
        setCanvasSize(num);
    }

    /**
     * Adapt the settings for the ViewPort and scroll bars to the dimensions
     * required. This needs to be called any time the image changes size.
     */
    protected void setCanvasSize(int num) {
        if (num == 1) {
            Dimension dimens = __Canvas.getPreferredSize();
            dimens.setSize(dimens.getWidth(), dimens.getHeight() + 50);
            __Canvas.setSize(dimens);
            __Canvas.setPreferredSize(dimens);
        } else {
            __Canvas.setSize(new Dimension(800, 705));
            __Canvas.setPreferredSize(new Dimension(800, 705));
        }

        // need this so that the scroll bars knows the size of the __Canvas that
        // has to be
        // scrolled over
        __Canvas.validate();
    }

    public NewCanvas GetCanvas() {
        return __Canvas;
    }

    public JScrollPane GetCanvasScrollPane() {
        return __CanvasScrollPane;
    }

    public String GetFileName() {
        return filename;
    }
}
