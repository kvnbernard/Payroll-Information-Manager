package data;

/**
 * This class gives information about several software process for pedagogy
 * 
 * @author Kevin BERNARD, Raphael D'URSO, Laura FUSTINONI, Aelien MOUBECHE
 * @version
 *
 */
public class Pedagogy {
    
    // Statistics Pedagogies
    public static String statTasksDones=queryComplexity(5,"Group By")+csvTC(5,"2 Sources")+mediatorTreatment(3,null)+dispImportance(6,"Bar Graph")+delim()+"This statistic is provided by the 4 sources :\nFor the CSVs, all departments are treated separately with the achievements of their members added up. Then, a total of all these is proceded. For the Databases, the same method is applied, but with the sums already done by the query by department. Then, the totals are compared to know which succursale achieved more contracts.";
    public static String statLeaveUsage=queryComplexity(2,"2 simple queries")+csvTC(2,"2 methods")+mediatorTreatment(6,"Averages and comparisons")+dispImportance(2,null)+delim()+"This statistic is provided by the 4 sources :\nLeave Usage is simply taken from the data sources, then it is proceded, summed and divided by the number of employees, itself taken from the data sources, then compared to the global average.\nThe branch with the maximum leave usage is displayed ";
    public static String statSalaryNote=queryComplexity(3,"Calculates the note")+csvTC(2,null)+mediatorTreatment(3,null)+dispImportance(2,null)+delim()+"This statistic is provided by the 4 sources :\nThe note of each salary is calculated as its number of achievement minus its number of blames, then regrouped for all sources and simply displayed";
    public static String statMonthEmployee=queryComplexity(5,"Calculates the note and takes the maximum")+csvTC(4,"Maximum")+mediatorTreatment(4,"Maximum of the 4 sources")+dispImportance(2,null)+delim()+"This statistic is provided by the 4 sources :\nWith the Salary notes, a 'best employee' is found, then a 'global best employee' is determined with all 4 sources";
    public static String statWorstEmployee=queryComplexity(5,"Calculates the note and takes the minimum")+csvTC(4,"Minimum")+mediatorTreatment(4,"Minimum of the 4 sources")+dispImportance(2,null)+delim()+"This statistic is provided by the 4 sources :\nWith the Salary notes, a 'worst employee' is found, then a 'global worst employee' is determined with all 4 sources";
    public static String statHighestFeesEmployee=queryComplexity(4,"Orders decreasingly and limits to 1")+csvTC(4,"Maximum")+mediatorTreatment(3,"Regroups and explains")+dispImportance(2,null)+delim()+"This statistic is provided by the 4 sources :\nThe fees are taken, an 'employee with highest fees' is found, then all 4 sources are regrouped";
    public static String statResultBySeniority=queryComplexity(8,"Calcultes seniority with dates and divisions, groups by seniority, orders by seniority and different syntax between databases")+csvTC(7,"Intermediate method to calculte seniority, regrouped in HashMaps by seniority")+mediatorTreatment(5,"Creates text and Graph")+dispImportance(7,"Bar Graph by Seniority")+delim()+"This statistic is provided by the 4 sources :\nSeniority is calculated with current date and hiring date, then regrouping employees with X years of seniority together, a Bar Graph is created";
    public static String statContractTypes=queryComplexity(5,"Group By")+csvTC(6,"2 Sources and regroups by Types")+mediatorTreatment(7,"Interprets language differences, regroups and creates Graph")+dispImportance(6,"Bar Graph by contract types")+delim()+"This statistic is provided by the 4 sources :\nContract types are regrouped and counted in the stat builder, then reinterpreted to be fused together";
    public static String statWagesInfo=queryComplexity(5,"Precise calculations")+csvTC(5, "2 Sources and calculations")+mediatorTreatment(2,null)+dispImportance(3, null)+delim()+"This statistic is provided by the 4 sources :\nWages are calculated as such : (salary * hours_worked * 4) + (overtime * salary * 1.25) + soc_benefits + bonus";
    public static String statEmploymentCost=queryComplexity(2, null)+csvTC(2, null)+mediatorTreatment(4, "Pie Chart creation")+dispImportance(5,"Pie Chart")+delim()+"This statistic is provided by the 4 sources :\nEmployment cost is calculted by adding all raw wages, then displayed in a pie chart";
    
    public static String queryComplexity(Integer note, String details) {
    	String text = "Query Complexity : "+note+"/10";
    	
    	if (details != null) {
    		text = text+" | "+details+"\n";
    	} else {
    		text = text+"\n";
    	}
    			
    	return text;
    }
    
    public static String csvTC(Integer note, String details) {
    	String text = "CSV Treatment Complexity : "+note+"/10";
    	
    	if (details != null) {
    		text = text+" | "+details+"\n";
    	} else {
    		text = text+"\n";
    	}
    			
    	return text;
    }
    
    public static String mediatorTreatment(Integer note, String details) {
    	String text = "Mediator Treatment Complexity : "+note+"/10";
    	
    	if (details != null) {
    		text = text+" | "+details+"\n";
    	} else {
    		text = text+"\n";
    	}
    			
    	return text;
    }
    
    public static String dispImportance(Integer note, String details) {
    	String text = "Display Importance : "+note+"/10";
    	
    	if (details != null) {
    		text = text+" | "+details+"\n";
    	} else {
    		text = text+"\n";
    	}
    			
    	return text;
    }
    
    public static String delim() {
    	return "\n --------\n";
    }
    
}