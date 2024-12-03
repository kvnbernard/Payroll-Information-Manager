package gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.GraphicsDevice;
import java.awt.GraphicsDevice.WindowTranslucency;
import java.awt.GraphicsEnvironment;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.ObjectInputFilter.Config;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;
import javax.swing.JFrame;

import data.GuiData;

/**
 * This class process the elements display on the main panel and the application window
 * 
 * @author Kevin BERNARD, Raphael D'URSO, Laura FUSTINONI, Aelien MOUBECHE
 *
 */
public class MainPanel extends JFrame implements Runnable {
	
	private static final long serialVersionUID = 1L;
	private ControlPanel controlPanel;	
	private static DashboardPanel dashboardPanel;
	private final static Dimension preferredSize = new Dimension(GuiData.WINDOW_WIDTH, GuiData.WINDOW_HEIGHT);
	private final static Dimension dashboardSize = new Dimension(GuiData.DASHBOARD_PANEL_WIDTH,GuiData.DASHBOARD_PANEL_HEIGHT);
	private final static Dimension controlPanelSize = new Dimension(GuiData.CONTROL_PANEL_WIDTH,GuiData.CONTROL_PANEL_HEIGHT);
	private Container contentPane;
	
	/**
	 * Constructor.
	 * This method allows to build the main panel
	 * 
	 * @param title : the title of the application window
	 */
	public MainPanel(String title) {
		
		super(title);
		this.setPreferredSize(preferredSize);
		init();
		
	}

	/**
	 * This method initialize the elements of the main panel
	 */
	private void init() {
		
		contentPane = getContentPane();
		contentPane.setLayout(new BorderLayout());
		
		dashboardPanel = new DashboardPanel();
		controlPanel = new ControlPanel();
		
		dashboardPanel.setPreferredSize(dashboardSize);
		controlPanel.setPreferredSize(controlPanelSize);
		
		contentPane.add(dashboardPanel, BorderLayout.NORTH);
		contentPane.add(controlPanel, BorderLayout.SOUTH);
		
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		pack();
		setPreferredSize(preferredSize);
		setVisible(true);
		setResizable(false);

	}
	
	/**
	 * This method is an overriding of the java method run() to process the thread and to refresh the main panel
	 */
	@Override	
	public void run() {
		
		// TODO Auto-generated method stub
		// actualization(map);
		while (true) {
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				System.out.println(e.getMessage());
			}
			dashboardPanel.revalidate();
			// dashboardPanel.repaint();
		}
		
	}
	
	/**
	 * This method allows to create the tasks done panel elements
	 */
	public static void creatTasksDonePanel() {
		
		removePanel();
		dashboardPanel.creatTasksDonePanel();
		
	}
	
	/**
	 * This method allows to create the wages information panel elements
	 */
	public static void creatWagesInfosPanel() {
		
		removePanel();
		dashboardPanel.creatWagesInfosPanel();

	}
	
	/**
	 * This method allows to create the leave usage panel elements
	 */
	public static void creatLeaveUsagePanel() {
		
		removePanel();
		dashboardPanel.creatLeaveUsagePanel();

	}
	
	/**
	 * This method allows to create the month employee panel elements
	 */
	public static void creatMonthEmpPanel() {
		
		removePanel();
		dashboardPanel.creatMonthEmpPanel();

	}
	
	/**
	 * This method allows to create the expensive employee panel elements
	 */
	public static void creatExpensiveEmpPanel() {
		
		removePanel();
		dashboardPanel.creatExpensiveEmpPanel();

	}
	
	/**
	 * This method allows to create the formation utility panel elements
	 */
	public static void createWortEmployeButton() {
		
		removePanel();
		dashboardPanel.createWorthEmployeBoutton();

	}
	
	/**
	 * This method allows to create the result by seniority panel elements
	 */
	public static void creatResBySeniorityPanel() {
		
		removePanel();
		dashboardPanel.creatResBySeniorityPanel();

	}
	
	/**
	 * This method allows to create the type of contract panel elements
	 */
	public static void creatTypeContractPanel() {
		
		removePanel();
		dashboardPanel.creatTypeContractPanel();

	}
	
	/**
	 * This method allows to create the employ cost panel elements
	 */
	public static void creatEmployCostPanel() {
		
		removePanel();
		dashboardPanel.creatEmployCostPanel();

	}
	
	/**
	 * This method allows to create the salary grades panel elements
	 */
	public static void creatSalGradesPanel() {
		
		removePanel();
		dashboardPanel.creatSalGradesPanel();

	}
	
	/**
	 * This method allows to create the pay panel elements
	 */
	public static void creatPayPanel() {
		
		removePanel();
		dashboardPanel.creatPayPanel();

	}
	
	/**
	 * This method allows to remove all panels on the dashboard
	 */
	public static void removePanel() {
		
		dashboardPanel.removeAll();
	}
}