package process;

import data.CSV_Information;
import data.DataForBarChartGraphic;
import data.DataForRecuperation;
import data.DataforCircularGraphic;
import data.MediatorResult;
import data.Pedagogy;
import data.SQLQuery;
import logger.LoggerUtility;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.apache.log4j.Logger;

import com.mysql.jdbc.PreparedStatement;
import com.mysql.jdbc.ResultSetMetaData;

import process.connexion.CsvRecuperation;
import process.connexion.Database_Connection;

/**
 * This class contains the heterogeneous data mediator
 * 
 * @author Kevin BERNARD, Raphael D'URSO, Laura FUSTINONI, Aelien MOUBECHE
 *
 */
public class Mediator {
	
	private static Logger logger = LoggerUtility.getLogger(Mediator.class, LoggerUtility.LOG_PREFERENCE);
	// we start the connection with the database
	private Database_Connection dataBase_MySQL;
	private Database_Connection dataBase_POSTGRE;
	private List<List<String>> csv_fr;
	private List<List<String>> csv_ALL1;
	private List<List<String>> csv_ALL2; // variable for
	private CsvRecuperation csv;
	private ResultSet resultSetMYSQL;
	private ResultSet resultSetPOSTGRESQL;
	private MediatorResult result = new MediatorResult("");
	private String whoRequest = "";
	private StatBuilder stat;


	/**
	 * Constructor.
	 * This method allows to connect to the different sources of data
	 */
	public Mediator() {
		
		try {
			dataBase_MySQL = new Database_Connection(DataForRecuperation.DATABASE_URL_MYSQL,
					DataForRecuperation.DATABASE_USER_MYSQL, DataForRecuperation.DATABASE_PASSWORD_MYSQL,
					DataForRecuperation.DATABASE_MYSQL);
			
		} catch (SQLException e) {
			logger.error("Database connection on MYSQL is failed");
			System.out.println(e.toString());
		}
		System.out.println(dataBase_MySQL.getName());
		try {
			dataBase_POSTGRE = new Database_Connection(DataForRecuperation.DATABASE_URL_POSTGRESQL,
					DataForRecuperation.DATABASE_USER_POSTGRESQL, DataForRecuperation.DATABASE_PASSWORD_POSTGRESQL,
					DataForRecuperation.DATABASE_POSTGRESQL);

		} catch (SQLException e) {
			logger.error("Database connection on POSTGRESQL is failed");

		}
		System.out.println(dataBase_POSTGRE.getName());
		System.out.println(dataBase_MySQL.getName());
		
		stat = new StatBuilder(dataBase_MySQL, dataBase_POSTGRE);
		csv = new CsvRecuperation(DataForRecuperation.CSV_FR);
		csv_fr = csv.SepareLineFR();

		csv = new CsvRecuperation(DataForRecuperation.CSV_ALL);
		csv_ALL1 = csv.SepareLineGER();
		csv = new CsvRecuperation(DataForRecuperation.CSV_ALL2);
		csv_ALL2 = csv.SepareLineGER();
		// we supprime the first line is not a usable data
		csv_ALL1.remove(0);
		csv_ALL2.remove(0);
		csv_fr.remove(0);
		
	}

	/**
	 * This method is used for telling how many leaves are taken branch by branch
	 * 
	 * @return the result in a MediatorResult
	 */
	public MediatorResult leaveUsage() {
		
		Integer leaveUsageGer = stat.leaveUsageCSV(csv_ALL2, CSV_Information.GER_CSV);
		Integer leaveUsageFr = stat.leaveUsageCSV(csv_fr, CSV_Information.fR_CSV);
		Integer leaveUsageUsa = 0;
		Integer leaveUsageChn = 0;
		
		Integer nbrEmplGer = stat.numberOfEmployeesCSV(csv_ALL1);
		Integer nbrEmplFr = stat.numberOfEmployeesCSV(csv_fr);
		Integer nbrEmplUsa = 0;
		Integer nbrEmplChn = 0;
		
		try {
			leaveUsageUsa = stat.numberOfEmployeesBD("Usa");
			leaveUsageChn = stat.numberOfEmployeesBD("Chn");
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.error("Could not get the leave usage of Chinese or Usa succursale");
		}
		
		try {
			nbrEmplUsa = stat.leaveUsageBD("Usa");
			nbrEmplChn = stat.leaveUsageBD("Chn");
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.error("Could not get the total number of employees for Chinese or Usa succursale");
		}
		
		Integer sumLeaveUsage = leaveUsageGer + leaveUsageFr + leaveUsageUsa + leaveUsageChn;
		Integer sumEmployees = nbrEmplGer + nbrEmplFr + nbrEmplUsa + nbrEmplChn;
		
		float averageLeaveUsage = sumLeaveUsage / sumEmployees;
		
		float averageLUGer = leaveUsageGer / nbrEmplGer;
		float averageLUFr = leaveUsageFr / nbrEmplFr;
		float averageLUUsa = leaveUsageUsa / nbrEmplUsa;
		float averageLUChn = leaveUsageChn / nbrEmplChn;
		
		
		float highest_avg = averageLUFr;
		String laziest_succursale = "French";
		
		if (averageLUGer > highest_avg) {
			highest_avg =averageLUGer;
			laziest_succursale = "German";
					
		} else if (averageLUChn > highest_avg) {
			highest_avg =averageLUChn;
			laziest_succursale = "Chinese";
			
		} else if (averageLUUsa > highest_avg) {
			highest_avg =averageLUUsa;
			laziest_succursale = "USA";
		}
		
		result.setInformation(laziest_succursale+" succursale is the succursale with the most leave usage out of the 4 with an average of "+ highest_avg +" for a global average of "+ averageLeaveUsage);
		result.setPedagogie(Pedagogy.statLeaveUsage);
		
		
		return result;
		
	}
	
	/**
	 * This method is used to get a List with the name and note of all salaries from the different branches
	 * 
	 * @return the result in a MediatorResult
	 */
	public MediatorResult employeeNote() {
		
		List<String> noteList = new ArrayList<String>();
		HashMap<String, Integer> notesFr = stat.noteEmployeeCSV(csv_fr, null, CSV_Information.fR_CSV);
		HashMap<String, Integer> notesGer = stat.noteEmployeeCSV(csv_ALL1, csv_ALL2, CSV_Information.GER_CSV);
		HashMap<String, Integer> notesUsa = null;
		HashMap<String, Integer> notesChn = null;
		
		HashMap<String, Integer> notes = new HashMap<String, Integer>();
		
		try {
			notesChn = stat.noteEmployeeBD("Chn");
			notesUsa = stat.noteEmployeeBD("Usa");
			
			for (String key: notesChn.keySet()) {
				notes.put(key, notesChn.get(key));
				noteList.add(key+": "+notesChn.get(key)+"\n");
			}
			
			for (String key: notesUsa.keySet()) {
				notes.put(key, notesUsa.get(key));
				noteList.add(key+": "+notesUsa.get(key)+"\n");
			}
			
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.error("Could not get the notes for Chinese of USA succursale");
		}
		
		for (String key: notesFr.keySet()) {
			notes.put(key, notesFr.get(key));
			noteList.add(key+": "+notesFr.get(key)+"\n");
		}
		
		for (String key: notesGer.keySet()) {
			noteList.add(key+": "+notesGer.get(key)+"\n");
		}
		result.setResult(noteList);
		result.setPedagogie(Pedagogy.statSalaryNote); 
		return result;
		
	}

	/**
	 * This method is used for telling the employee of the month for each branch and global
	 * 
	 * @return the result in a MediatorResult
	 */
	public MediatorResult monthEmployee() {
		
		HashMap<String, Integer> mthemplFr = stat.monthEmployeeCSV(csv_fr, null, CSV_Information.fR_CSV);
		HashMap<String, Integer> mthemplGer = stat.monthEmployeeCSV(csv_ALL1, csv_ALL2, CSV_Information.GER_CSV);
		HashMap<String, Integer> mthemplUsa = null;
		HashMap<String, Integer> mthemplChn = null;
		List<String>  allMonthEmployee =  new ArrayList<String>();
		try {
			mthemplChn = stat.monthEmployeeBD("Chn");
			mthemplUsa = stat.monthEmployeeBD("Usa");
			
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.error("Could not get the notes for Chinese of USA succursale");
		}
		
		Integer noteBest = 0 ;
		
		String best_branch = "French";
		for (String key: mthemplFr.keySet()) {
			allMonthEmployee.add("French  "+mthemplFr.keySet()+ " : "+noteBest+"\n");
			if (noteBest < mthemplFr.get(key)) {
				noteBest = mthemplFr.get(key);	
				result.setInformation(mthemplFr.keySet()+"  "+ best_branch + " : "+noteBest+"\n\n\n\n" );
			}	

		}
		
		for (String key: mthemplGer.keySet()) {
			allMonthEmployee.add("Germany  "+mthemplGer.keySet()+ " : "+noteBest+"\n");
			if (noteBest < mthemplGer.get(key)) {
				noteBest = mthemplGer.get(key);
				best_branch="Germany";
				result.setInformation(mthemplGer.keySet()+"  "+ best_branch + " : "+noteBest+"\n\n\n\n" );
			}
		}
		
		for (String key: mthemplUsa.keySet()) {
			allMonthEmployee.add("USA "+mthemplUsa.keySet()+ " : "+noteBest+"\n");
			if( noteBest < mthemplUsa.get(key)){
				noteBest = mthemplUsa.get(key);
				best_branch="USA";

				result.setInformation(mthemplUsa.keySet()+"  "+ best_branch + " : "+noteBest+"\n\n\n\n" );
				}
		}
		
		for (String key: mthemplChn.keySet()) {
			allMonthEmployee.add("Chinese "+mthemplChn.keySet()+ " : "+noteBest+"\n");
			if(noteBest < mthemplChn.get(key)) {
				noteBest = mthemplChn.get(key);
				best_branch ="Chinese ";
				result.setInformation(mthemplChn.keySet()+"  "+ best_branch + " : "+noteBest+"\n\n\n\n" );
			}
		}
		result.setResult(allMonthEmployee);
		result.setPedagogie(Pedagogy.statMonthEmployee); 
		return result;
	}
	
	/**
	 * This method is used to determine the worst employee
	 * 
	 * @return the result in a MediatorResult
	 */
	public MediatorResult worstEmployee() {
		HashMap<String, Integer> wrstemplFr = stat.monthEmployeeCSV(csv_fr, null, CSV_Information.fR_CSV);
		HashMap<String, Integer> wrstemplGer = stat.monthEmployeeCSV(csv_ALL1, csv_ALL2, CSV_Information.GER_CSV);
		HashMap<String, Integer> wrstemplUsa = null;
		HashMap<String, Integer> wrstemplChn = null;
		List<String>  allWorstEmployee =  new ArrayList<String>();
		try {
			wrstemplChn = stat.monthEmployeeBD("Chn");
			wrstemplUsa = stat.monthEmployeeBD("Usa");
			
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.error("Could not get the notes for Chinese of USA succursale");
		}
		
		Integer noteWorst = 0 ;
		
		String worst_branch = "French";
		for (String key: wrstemplFr.keySet()) {
			allWorstEmployee.add("French  "+wrstemplFr.keySet()+ " : "+noteWorst+"\n");
			if (noteWorst > wrstemplFr.get(key)) {
				noteWorst = wrstemplFr.get(key);	
				result.setInformation(wrstemplFr.keySet()+"  "+ worst_branch + " : "+noteWorst+"\n\n\n\n" );
			}	

		}
		
		for (String key: wrstemplGer.keySet()) {
			allWorstEmployee.add("Germany  "+wrstemplGer.keySet()+ " : "+noteWorst+"\n");
			if (noteWorst > wrstemplGer.get(key)) {
				noteWorst = wrstemplGer.get(key);
				worst_branch="Germany";
				result.setInformation(wrstemplGer.keySet()+"  "+ worst_branch + " : "+noteWorst+"\n\n\n\n" );
			}
		}
		
		for (String key: wrstemplUsa.keySet()) {
			allWorstEmployee.add("USA "+wrstemplUsa.keySet()+ " : "+noteWorst+"\n");
			if( noteWorst > wrstemplUsa.get(key)){
				noteWorst = wrstemplUsa.get(key);
				worst_branch="USA";

				result.setInformation(wrstemplUsa.keySet()+"  "+ worst_branch + " : "+noteWorst+"\n\n\n\n" );
				}
		}
		
		for (String key: wrstemplChn.keySet()) {
			allWorstEmployee.add("Chinese "+wrstemplChn.keySet()+ " : "+noteWorst+"\n");
			if(noteWorst > wrstemplChn.get(key)) {
				noteWorst = wrstemplChn.get(key);
				worst_branch ="Chinese ";
				result.setInformation(wrstemplChn.keySet()+"  "+ worst_branch + " : "+noteWorst+"\n\n\n\n" );
			}
		}
		result.setResult(allWorstEmployee);
		result.setPedagogie(Pedagogy.statWorstEmployee); 
		return result;
	};

	
	/**
	 * This method is used to determine the highest fees employees
	 * 
	 * @return the result in a MediatorResult
	 */
	public MediatorResult highestFeesEmployees() {
		HashMap<String, Integer> hfEmployeesGer = stat.feesEmployeesCSV(csv_ALL2, csv_ALL1, CSV_Information.GER_CSV);
		HashMap<String, Integer> hfEmployeesFr = stat.feesEmployeesCSV(csv_fr, null, CSV_Information.fR_CSV);
		HashMap<String, Integer> hfEmployeesChn;
		HashMap<String, Integer> hfEmployeesUsa;
		
		List<String> hfEmployees = new ArrayList<String>();
		
		
		
		try {
			hfEmployeesChn = stat.noteEmployeeBD("Chn");
			hfEmployeesUsa = stat.noteEmployeeBD("Usa");
			
			for (String key: hfEmployeesChn.keySet()) {
				hfEmployees.add(key+" has for fees : "+hfEmployeesChn.get(key)+"$\n");
			}
			
			for (String key: hfEmployeesUsa.keySet()) {
				hfEmployees.add(key+" has for fees : "+hfEmployeesUsa.get(key)+"$\n");
			}
			
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.error("Could not get the notes for Chinese of USA succursale");
		}
		
		for (String key: hfEmployeesFr.keySet()) {
			hfEmployees.add(key+" has for fees : "+hfEmployeesFr.get(key)+"$\n");
		}
		
		for (String key: hfEmployeesGer.keySet()) {
			hfEmployees.add(key+" has for fees : "+hfEmployeesGer.get(key)+"$\n");
		}
		
		result.setResult(hfEmployees);
		result.setPedagogie(Pedagogy.statHighestFeesEmployee); 		
		
		return result;
	}
	
	/**
	 * This method is used to determine the number of contract types
	 * 
	 * @return the result in a MediatorResult
	 */
	public MediatorResult contractTypesCount() {
		HashMap<String, Integer> ctcEmployeesGer = stat.contractTypesCSV(csv_ALL1, CSV_Information.GER_CSV);
		HashMap<String, Integer> ctcEmployeesFr = stat.contractTypesCSV(csv_fr, CSV_Information.fR_CSV);
		HashMap<String, Integer> ctcEmployeesChn = null;
		HashMap<String, Integer> ctcEmployeesUsa = null;
		
		HashMap<String, Integer> groupedResults = new HashMap<String, Integer>();
		
		List<DataForBarChartGraphic> graphics = new ArrayList<DataForBarChartGraphic>();
		
		Integer formerValue;
		
		String actualKey;
		
		try {
			ctcEmployeesChn = stat.contractTypesBD("Chn");
			ctcEmployeesUsa = stat.contractTypesBD("Usa");
			
			for (String key: ctcEmployeesChn.keySet()) {
				
				formerValue = groupedResults.get(key);
				if (formerValue == null) {
					formerValue = 0;
				}
				
				groupedResults.put(key, formerValue + ctcEmployeesChn.get(key));
			}
			
			for (String key: ctcEmployeesUsa.keySet()) {
				
				formerValue = groupedResults.get(key);
				if (formerValue == null) {
					formerValue = 0;
				}
				
				groupedResults.put(key, formerValue + ctcEmployeesUsa.get(key));
			}
			
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.error("Could not get the results by seniority for Chinese or Usa succursale");
		}
		
		for (String key: ctcEmployeesFr.keySet()) {
			
			if (key.equals("CDI")){
				actualKey = "Permanent";
			} else {
				actualKey = "Fixed-Term";
			}
			
			formerValue = groupedResults.get(actualKey);
			if (formerValue == null) {
				formerValue = 0;
			}
			
			groupedResults.put(actualKey, formerValue + ctcEmployeesFr.get(key));
		}
		
		for (String key: ctcEmployeesGer.keySet()) {
			
			if (key.equals("Unbefristet")){
				actualKey = "Permanent";
			} else {
				actualKey = "Fixed-Term";
			}
			
			formerValue = groupedResults.get(actualKey);
			if (formerValue == null) {
				formerValue = 0;
			}
			
			groupedResults.put(actualKey, formerValue + ctcEmployeesGer.get(key));
		}
		
		
		for (String key: groupedResults.keySet())
		{
			graphics.add(new DataForBarChartGraphic(groupedResults.get(key), "Type ",key));
			
		}
		
		result.setGraphicTitle("Number of limited time contracts and permanent ones");
		result.setValueCompare("Contrat number");
		result.setBarChartGraphic(graphics);
		result.setPedagogie(Pedagogy.statContractTypes); 
		return result;

	}
	
	/**
	 * This method is used for giving information about total tasks done
	 * 
	 * @return the information in a MediatorResult
	 */
	public MediatorResult tasksDone() {
		
		HashMap<String, Integer> tasksDoneFr = stat.taskDoneCSV(csv_fr, CSV_Information.fR_CSV);
		HashMap<String, Integer> tasksDoneGer = stat.taskDoneCSV(csv_ALL2, CSV_Information.GER_CSV);
		HashMap<String, Integer> tasksDoneChn = null;
		HashMap<String, Integer> tasksDoneUsa = null;
		try {
			tasksDoneChn = stat.taskDoneBD("Chn");
			tasksDoneUsa = stat.taskDoneBD("Usa");
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.error("Could not get the achievements of Chinese or Usa succursale");
		}
		
		int max_achv = tasksDoneFr.get("total"+CSV_Information.fR_CSV);
		String best_succursale = "France";
		
		int achv_Ger = tasksDoneGer.get("total"+CSV_Information.GER_CSV);

		int achv_Chn = tasksDoneChn.get("totalChn");
		int achv_Usa = tasksDoneUsa.get("totalUsa");
		
		result.setPedagogie(Pedagogy.statTasksDones);
		result.setInformation("FR"+ " succursale is the succursale with the most achievements : "+String.valueOf(max_achv));
		List<DataforCircularGraphic> graphics = new ArrayList<DataforCircularGraphic>();
		graphics.add(new DataforCircularGraphic(tasksDoneFr.get("totalFr"),"FR"));
		graphics.add(new DataforCircularGraphic(tasksDoneGer.get("totalGer"),"GER"));
		graphics.add(new DataforCircularGraphic(tasksDoneChn.get("totalChn"),"CHN"));
		graphics.add(new DataforCircularGraphic(tasksDoneUsa.get("totalUsa"),"USA"));
		result.setCicularGraphic(graphics);
		result.setGraphicTitle("Tasks done per Succursale");
		
		if (achv_Ger > max_achv) {
			max_achv = achv_Ger;
			best_succursale = "Germany";
			result.setInformation(best_succursale+ " succursale is the succursale with the most achievements : "+String.valueOf(max_achv));
		} else if (achv_Chn > max_achv) {
			max_achv = achv_Chn;
			best_succursale = "China";
			result.setInformation(best_succursale+ " succursale is the succursale with the most achievements : "+String.valueOf(max_achv));
		} else if (achv_Usa > max_achv) {
			max_achv = achv_Usa;
			best_succursale = "USA";
			result.setInformation(best_succursale+ " succursale is the succursale with the most achievements : "+String.valueOf(max_achv));
		}
		return result;
		
	}
	
	/**
	 * This method is used for giving information about the results group by seniority
	 * 
	 * @return the information in a MediatorResult
	 */
	public MediatorResult resultsBySeniority() {
		HashMap<Integer, Integer> seniorityFr = stat.resultBySeniorityCSV(csv_fr, null, CSV_Information.fR_CSV);
		HashMap<Integer, Integer> seniorityGer = stat.resultBySeniorityCSV(csv_ALL1, csv_ALL2, CSV_Information.GER_CSV);
		HashMap<Integer, Integer> seniorityChn = null;
		HashMap<Integer, Integer> seniorityUsa= null;
		
		HashMap<Integer, Integer> groupedResults = new HashMap<Integer, Integer>();
		
		List<String> seniorityInformations = new ArrayList<String>();
		List<DataForBarChartGraphic> graphics = new ArrayList<DataForBarChartGraphic>();
		
		Integer formerValue;
		
		try {
			seniorityChn = stat.resultBySeniorityBD("Chn");
			seniorityUsa = stat.resultBySeniorityBD("Usa");
			
			for (Integer key: seniorityChn.keySet()) {
				
				formerValue = groupedResults.get(key);
				if (formerValue == null) {
					formerValue = 0;
				}
				
				groupedResults.put(key, formerValue + seniorityChn.get(key));
			}
			
			for (Integer key: seniorityUsa.keySet()) {
				
				formerValue = groupedResults.get(key);
				if (formerValue == null) {
					formerValue = 0;
				}
				
				groupedResults.put(key, formerValue + seniorityUsa.get(key));
			}
			
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.error("Could not get the results by seniority for Chinese or Usa succursale");
		}
		
		for (Integer key: seniorityFr.keySet()) {
			
			formerValue = groupedResults.get(key);
			if (formerValue == null) {
				formerValue = 0;
			}
			
			groupedResults.put(key, formerValue + seniorityFr.get(key));
		}
		
		for (Integer key: seniorityGer.keySet()) {
			
			formerValue = groupedResults.get(key);
			if (formerValue == null) {
				formerValue = 0;
			}
			
			groupedResults.put(key, formerValue + seniorityGer.get(key));
		}
		
		String textResults;
		
		for (Integer key: groupedResults.keySet())
		{
			textResults = "The mean results for employees with "+key+" year(s) of seniority are "+groupedResults.get(key)+"\n";
			graphics.add(new DataForBarChartGraphic(groupedResults.get(key), "years",key+"years"));
			
			seniorityInformations.add(textResults);
		}
		
		result.setGraphicTitle("graphic of efficaty by ancienty");
		result.setValueCompare("mean result");
		result.setBarChartGraphic(graphics);
		result.setResult(seniorityInformations);
		result.setPedagogie(Pedagogy.statResultBySeniority); 
		return result;
	}

	/**
	 * This method is used to determine the employment cost
	 * 
	 * @return the result in a MediatorResult
	 */
	public MediatorResult employmentCost() {
		int employmentcostFr = stat.employmentCostCSV(csv_fr, CSV_Information.fR_CSV);
		int employmentcostGer = stat.employmentCostCSV(csv_ALL2, CSV_Information.GER_CSV);
		int employmentcostChn = 0;
		int employmentcostUSA = 0;
		try {
			employmentcostChn = stat.employmentCostBD("Chn");
			employmentcostFr = stat.employmentCostBD("Usa");
		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.error("Could not get the achievements of Chinese or Usa succursale");
		}
		List<DataforCircularGraphic> graphics = new ArrayList<DataforCircularGraphic>();
		graphics.add(new DataforCircularGraphic(employmentcostFr,"FR"));
		graphics.add(new DataforCircularGraphic(employmentcostGer,"GER"));
		graphics.add(new DataforCircularGraphic(employmentcostChn,"CHN"));
		graphics.add(new DataforCircularGraphic(employmentcostUSA,"USA"));
		int mostCost =employmentcostChn;
			mostCost =+ employmentcostFr;
		
			mostCost =+ employmentcostUSA;

			mostCost =+ employmentcostGer;

			result.setPedagogie(Pedagogy.statEmploymentCost); 
		result.setInformation("total employment cost : "+mostCost);
		result.setCicularGraphic(graphics);
		result.setGraphicTitle("Employment cost comparison for the four branches");
		return result;
	}
	
	/**
	 * This method is used to get and determine wages info about employees
	 * 
	 * @return the result in a MediatorResult
	 */
	public MediatorResult wagesInfo() {
		HashMap<String, Integer> wagesinfofr = stat.wagesInforCSV(csv_fr,null, CSV_Information.fR_CSV);
		HashMap<String, Integer> wagesinfoGer = stat.wagesInforCSV( csv_ALL1,csv_ALL2, CSV_Information.GER_CSV);
		HashMap<String, Integer> wagesinfoChn = null;
		HashMap<String, Integer> wagesinfoUse = null;

		List<String> wage = new ArrayList<String>();
		try {
			wagesinfoChn = stat.wagesInfoBd("Chn");
			wagesinfoUse = stat.wagesInfoBd("Usa");

		} catch (SQLException e) {
			logger.error(e.getMessage());
			logger.error("Could not get the achievements of Chinese or Usa succursale");
		}
		for(String key : wagesinfoChn.keySet()) {
			wage.add(key+" "+wagesinfoChn.get(key)+"\n");
		}
		for(String key : wagesinfofr.keySet()) {
			wage.add(key+" "+wagesinfofr.get(key)+"\n");
		}
		for(String key : wagesinfoGer.keySet()) {
			wage.add(key+" "+wagesinfoGer.get(key)+"\n");
		}
		for(String key : wagesinfoUse.keySet()) {
			wage.add(key+" "+wagesinfoUse.get(key)+"\n");
		}
		result.setPedagogie(Pedagogy.statWagesInfo); 
		result.setResult(wage);
		return result;
	}
	
	/**
	 * This method is used to build pay slip
	 * 
	 * @return the result in a MediatorResult
	 */
	public MediatorResult PayFich() {
		
		List<String> PayFichfr  = stat.payFichCSV(csv_fr, null, CSV_Information.fR_CSV);
		List<String> PayFichGer  = stat.payFichCSV( csv_ALL1,csv_ALL2, CSV_Information.GER_CSV);
		List<String> PayFichUSA = null;
		List<String> PayFichCHN = null;
		
		List<String> finalPayFich  =PayFichfr;
		finalPayFich.addAll(PayFichGer);
		result.setPedagogie("PAY SLIP");
		result.setResult(finalPayFich);
		return result;
		
	}
	
}
