package gui;

import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import data.GuiData;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;

/**
* This class process the elements display and actions on the control panel
* 
* @author Kevin BERNARD, Raphael D'URSO, Laura FUSTINONI, Aelien MOUBECHE
*
*/
public class ControlPanel extends JPanel {
	
	/**
	 * Constructor.
	 * This method allows to build the control panel with buttons and define their action
	 */
	public ControlPanel() {
		
		new JPanel();
		
		JPanel statPanel = new JPanel();
		JPanel payPanel = new JPanel();
		
		GridLayout panelLayout = new GridLayout(1,2);
		GridLayout statPanelLayout = new GridLayout(5,2);
		GridLayout payPanelLayout = new GridLayout(1,1);
		
		setLayout(panelLayout);
		statPanel.setLayout(statPanelLayout);
		payPanel.setLayout(payPanelLayout);
		
		JButton tasksDoneButton = new JButton();
		tasksDoneButton.setText("R�sultats de l'entreprise");
		tasksDoneButton.setBackground(GuiData.BACK_STAT_BUTTON_COLOR);
		tasksDoneButton.setForeground(GuiData.FORE_BUTTON_COLOR);
		statPanel.add(tasksDoneButton);
		tasksDoneButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainPanel.creatTasksDonePanel();
			}
		});
		
		JButton wagesInfosButton = new JButton();
		wagesInfosButton.setText("Moyenne des salaires");
		wagesInfosButton.setBackground(GuiData.BACK_STAT_BUTTON_COLOR);
		wagesInfosButton.setForeground(GuiData.FORE_BUTTON_COLOR);
		statPanel.add(wagesInfosButton);
		wagesInfosButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainPanel.creatWagesInfosPanel();
			}
		});
		
		JButton leaveUsageButton = new JButton();
		leaveUsageButton.setText("Utilisation des cong�s");
		leaveUsageButton.setBackground(GuiData.BACK_STAT_BUTTON_COLOR);
		leaveUsageButton.setForeground(GuiData.FORE_BUTTON_COLOR);
		statPanel.add(leaveUsageButton);
		leaveUsageButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainPanel.creatLeaveUsagePanel();
			}
		});
		
		JButton monthEmpButton = new JButton();
		monthEmpButton.setText("Employ� du mois");
		monthEmpButton.setBackground(GuiData.BACK_STAT_BUTTON_COLOR);
		monthEmpButton.setForeground(GuiData.FORE_BUTTON_COLOR);
		statPanel.add(monthEmpButton);
		monthEmpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainPanel.creatMonthEmpPanel();
			}
		});
		
		JButton expensiveEmpButton = new JButton();
		expensiveEmpButton.setText("Employ� le plus cher");
		expensiveEmpButton.setBackground(GuiData.BACK_STAT_BUTTON_COLOR);
		expensiveEmpButton.setForeground(GuiData.FORE_BUTTON_COLOR);
		statPanel.add(expensiveEmpButton);
		expensiveEmpButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainPanel.creatExpensiveEmpPanel();
			}
		});
		
		JButton formationUtilityButton = new JButton();
		formationUtilityButton.setText(" Employ�s moins performant");
		formationUtilityButton.setBackground(GuiData.BACK_STAT_BUTTON_COLOR);
		formationUtilityButton.setForeground(GuiData.FORE_BUTTON_COLOR);
		statPanel.add(formationUtilityButton);
		formationUtilityButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainPanel.createWortEmployeButton();
			}
		});
		
		JButton resBySeniorityButton = new JButton();
		resBySeniorityButton.setText("R�sultats par anciennet�");
		resBySeniorityButton.setBackground(GuiData.BACK_STAT_BUTTON_COLOR);
		resBySeniorityButton.setForeground(GuiData.FORE_BUTTON_COLOR);
		statPanel.add(resBySeniorityButton);
		resBySeniorityButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainPanel.creatResBySeniorityPanel();
			}
		});
		
		JButton typeContractButton = new JButton();
		typeContractButton.setText("Taux des contrats");
		typeContractButton.setBackground(GuiData.BACK_STAT_BUTTON_COLOR);
		typeContractButton.setForeground(GuiData.FORE_BUTTON_COLOR);
		statPanel.add(typeContractButton);
		typeContractButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainPanel.creatTypeContractPanel();
			}
		});
		
		JButton employCostButton = new JButton();
		employCostButton.setText("Co�t de l'emploi");
		employCostButton.setBackground(GuiData.BACK_STAT_BUTTON_COLOR);
		employCostButton.setForeground(GuiData.FORE_BUTTON_COLOR);
		statPanel.add(employCostButton);
		employCostButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
			MainPanel.creatEmployCostPanel();
			}
		});
		
		JButton salGradesButton = new JButton();
		salGradesButton.setText("Notes des salari�s");
		salGradesButton.setBackground(GuiData.BACK_STAT_BUTTON_COLOR);
		salGradesButton.setForeground(GuiData.FORE_BUTTON_COLOR);
		statPanel.add(salGradesButton);
		salGradesButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainPanel.creatSalGradesPanel();
			}
		});
		
		JButton payButton = new JButton();
		payButton.setText("Fiches de paie");
		payButton.setBackground(GuiData.BACK_PAY_BUTTON_COLOR);
		payButton.setForeground(GuiData.FORE_BUTTON_COLOR);
		payPanel.add(payButton);
		payButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				MainPanel.creatPayPanel();
			}
		});
		
		add(statPanel);
		add(payPanel);
		
		}
	
}
