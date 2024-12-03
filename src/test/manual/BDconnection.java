package test.manual;

import gui.ControlPanel;
import gui.MainPanel;
import process.Mediator;

/**
 * This class contains the main program for tests
 * 
 * @author Kevin BERNARD, Raphael D'URSO, Laura FUSTINONI, Aelien MOUBECHE
 * @version
 *
 */
public class BDconnection {

	public static void main(String[] args) {
		
		MainPanel mainPanel = new MainPanel("Global Human Resources");
		Thread mainPanelThread = new Thread(mainPanel);
		mainPanelThread.start();
		
	}

}
