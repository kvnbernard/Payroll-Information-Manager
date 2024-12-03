package gui;

import java.awt.CardLayout;
import java.awt.Color;
import java.awt.GridLayout;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;
import org.jfree.data.general.DefaultPieDataset;
import org.jfree.data.xy.XYSeries;
import org.jfree.data.xy.XYSeriesCollection;
import org.apache.log4j.Logger;
import org.jfree.*;

import data.DataForBarChartGraphic;
import data.DataForLinearGraphic;
import data.DataforCircularGraphic;
import data.MediatorResult;
import data.Pedagogy;
import process.Mediator;
import logger.LoggerUtility;

/**
 * This class process the elements display on the dashboard panel
 * 
 * @author Kevin BERNARD, Raphael D'URSO, Laura FUSTINONI, Aelien MOUBECHE
 *
 */
public class DashboardPanel extends JPanel {

	private static Logger logger = LoggerUtility.getLogger(DashboardPanel.class, LoggerUtility.LOG_PREFERENCE);
	private Mediator mediator = new Mediator();
	private MediatorResult result;
	boolean isUser = false;

	/**
	 * Constructor.
	 * This method allows to build the dashboard panel
	 */
	public DashboardPanel() {
		
		JPanel sartPanel = new JPanel();
		sartPanel.setSize(800, 800);
		
	}

	/**
	 * This method creates the tasks done panel elements on the dashboard
	 */
	public void creatTasksDonePanel() {
			result = mediator.tasksDone();
			createCicurlarPanel(result);
	}

	/**
	 * This method creates the wages information panel elements on the dashboard
	 */
	public void creatWagesInfosPanel() {
		result = mediator.wagesInfo();
		createListPanel(result);
	}

	/**
	 * This method creates the leave usage panel elements on the dashboard
	 */
	public void creatLeaveUsagePanel() {
		
		result = mediator.leaveUsage();
		createTextPanel(result);
		
	}

	/**
	 * This method creates the month employee panel elements on the dashboard
	 */
	public void creatMonthEmpPanel() {
		result = mediator.monthEmployee();
		createTextPanel(result);
		
	}

	/**
	 * This method creates the expensive employee panel elements on the dashboard
	 */
	public void creatExpensiveEmpPanel() {
		result = mediator.highestFeesEmployees();
		createListPanel(result);
	}

	/**
	 * This method creates the formation utility panel elements on the dashboard
	 */
	public void createWorthEmployeBoutton() {
		result = mediator.worstEmployee();
		createListPanel(result);
	}

	/**
	 * This method creates the result by seniority panel elements on the dashboard
	 */
	public void creatResBySeniorityPanel() {
		result = mediator.resultsBySeniority();
		createBarCharGraphic(result);
	}

	/**
	 * This method creates the type of contract panel elements on the dashboard
	 */
	public void creatTypeContractPanel() {
		result = mediator.contractTypesCount();
		createBarCharGraphic(result);
	}

	/**
	 * This method creates the employ cost panel elements on the dashboard
	 */
	public void creatEmployCostPanel() {
		result = mediator.employmentCost();
				createCicurlarPanel(result);
	}

	/**
	 * This method creates the salary grades panel elements on the dashboard
	 */
	public void creatSalGradesPanel() {
		
		result = mediator.employeeNote();
		createListPanel(result);
		
	}

	/**
	 * This method creates the pay panel elements on the dashboard
	 */
	public void creatPayPanel() {
		setSize(800, 600);
			result = mediator.PayFich();
			String payslip ="";
			for (String pay : result.getResult()) {
				payslip= payslip+pay;
			}
			JTextArea test =creaeJTextArea("                                                                                                                      "+result.getPedagogie()+"\n"+payslip);
			test.setBounds(0, 0, 780, 600);
			JScrollPane sp = new JScrollPane(test);
			sp.setSize(800, 600);
			add(sp);
	}

	/**
	 * This method allows to create a JTextArea on the dashboard
	 * 
	 * @param text : the text to put in the JTextArea in String
	 * @return JTextArea containing the text
	 */
	public JTextArea creaeJTextArea(String text) {
		
		JTextArea jTextArea = new JTextArea(text);
		jTextArea.setEnabled(false);
		jTextArea.setForeground(Color.WHITE);
		jTextArea.setLineWrap(true);
		jTextArea.setBackground(Color.BLACK);
		return jTextArea;
		
	}
	
	/**
	 * This method allows to create a panel with a circular graphic on the dashboard
	 * 
	 * @param result : result of the mediator
	 */
	public void createCicurlarPanel(MediatorResult result) {
		
		setSize(800, 600);
		GridLayout resultLayout = new GridLayout(1, 2);
		setLayout(resultLayout);
		add(creaeJTextArea(result.getPedagogie()));
		// creation du cammember
		DefaultPieDataset pieDataset = new DefaultPieDataset();
		/*
		 * we use data from MediatorResult for create de cicurla graphic 
		 */
		for(DataforCircularGraphic  data : result.getCicularGraphic() ) {
			pieDataset.setValue(data.getWhoValues(),data.getValues());
		}
		JFreeChart pieChart = ChartFactory.createPieChart(result.getGraphicTitle(), pieDataset, true, false, false);
		ChartPanel cPanel = new ChartPanel(pieChart);
		add(cPanel);
		isUser = true;
		
	}
	
	/**
	 * This method allows to create a panel with a bar char graphic on the dashboard
	 * 
	 * @param result : result of the mediator
	 */
	public void createBarCharGraphic(MediatorResult result) {
		
		setSize(800, 600);
		GridLayout resultLayout = new GridLayout(1, 2);
		setLayout(resultLayout);
		add(creaeJTextArea(result.getPedagogie()));
		 DefaultCategoryDataset dataset = new DefaultCategoryDataset(); 
		 for(DataForBarChartGraphic data : result.getBarChartGraphic()) {
			 dataset.addValue(data.getValue(),data.getCompareValue() ,data.getWhoHaveValue() );

		 }
		 JFreeChart barChart = ChartFactory.createBarChart(result.getGraphicTitle(), "", result.getValueCompare() ,
		 dataset, PlotOrientation.VERTICAL, true, true, false); 
		 ChartPanel chartPanel = new ChartPanel( barChart ); 
		 add(chartPanel);
		 isUser=true;
		 
	}
	
	/**
	 * This method allows to create a text panel with information on the dashboard
	 * 
	 * @param result : result of the mediator
	 */
	public void createTextPanel(MediatorResult result) {
		
		setSize(800, 600);
		GridLayout resultLayout = new GridLayout(1, 2);
		setLayout(resultLayout);
		add(creaeJTextArea(result.getPedagogie()));
		String liString = "";
		for (String  line: result.getResult()) {
			liString =liString+line;
		}
		add(creaeJTextArea(result.getInformation()+liString));
		
	}
	
	/**
	 * This method allows to create a list panel on the dashboard
	 * 
	 * @param result : result of the mediator
	 */
	public void createListPanel(MediatorResult result) {
		
		setSize(800, 600);
		GridLayout resultLayout = new GridLayout(1, 2);
		setLayout(resultLayout);
		add(creaeJTextArea(result.getPedagogie()));
		String liString = "";
		for (String  line: result.getResult()) {
			liString =liString+line;
		}
		add( creaeJTextArea(liString));
		
	}
	
}
