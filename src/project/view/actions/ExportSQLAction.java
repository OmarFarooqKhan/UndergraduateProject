package project.view.actions;

import java.awt.Desktop;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.IOException;

import javax.swing.AbstractAction;
import javax.swing.ImageIcon;

import project.controller.Controller;
import project.utils.SqlGenerator;

public class ExportSQLAction extends AbstractAction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 5206587360437869536L;
	private Controller controller;
	SqlGenerator generate;

	{
		putValue(NAME, "Export SQL");
		putValue(SMALL_ICON, new ImageIcon(getClass().getResource("/project/icons/sql.png")));
		putValue(SHORT_DESCRIPTION, " Exports sql");
	}

	public ExportSQLAction(Controller controller) {
		this.controller = controller;
		this.generate = new SqlGenerator(controller.getModel());
	}

	@Override
	public void actionPerformed(ActionEvent e) {
		if(generate.genSql()) {

			File file = new File("Exported_SQL/sql.txt");
			
			if(!Desktop.isDesktopSupported()) {//check if Desktop is supported by Platform or not  
				
				System.out.println("not supported");  
				return;  
			}  
			Desktop desktop = Desktop.getDesktop();  
			if(file.exists()) {         //checks file exists or not  
				try {
					desktop.open(file);
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}              //opens the specified file  
			} 	
		}
		else {
			controller.displayAllErrors(generate.getThingsToFix());
			generate.getThingsToFix().clear();
		}
}

}
