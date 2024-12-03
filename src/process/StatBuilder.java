package process;

/**
 * This class builds the statistics
 * 
 * @author Kevin BERNARD, Raphael D'URSO, Laura FUSTINONI, Aelien MOUBECHE
 *
 */

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.format.DateTimeFormatter;
import java.time.LocalDateTime;

import org.apache.log4j.Logger;

import java.sql.Connection;
import data.CSV_Information;
import data.SQLQuery;
import logger.LoggerUtility;
import process.connexion.CsvRecuperation;
import process.connexion.Database_Connection;

public class StatBuilder {
	
	private static Logger logger = LoggerUtility.getLogger(StatBuilder.class, LoggerUtility.LOG_PREFERENCE);
	private Database_Connection dataBase_MySQL;
	private Database_Connection dataBase_POSTGRE;
	private static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
	private LocalDateTime now;
	private String timestamp;

	/**
	 * Constructor
	 * This method allows to build the statistics
	 * 
	 * @param db1 : information connection of the first database
	 * @param db2 : information connection of the second database
	 */
	public StatBuilder(Database_Connection db1, Database_Connection db2) {
		dataBase_MySQL = db1;
		dataBase_POSTGRE = db2;
	}

	/**
	 * This method is used to return the number of employees in CSV files for statistics
	 * 
	 * @param information : a list of data of CSV in a List<List<String>>
	 * @return the result in an integer
	 */
	public Integer numberOfEmployeesCSV(List<List<String>> information) {
		Integer nbr = 0;

		for (List<String> list : information) {
			nbr++;
		}

		return nbr;
	}

	/**
	 * This method is used to return the number of employees in databases for statistics
	 * 
	 * @param branch : the branch of the company in a String
	 * @return the result in an integer
	 * @throws SQLException
	 */
	public Integer numberOfEmployeesBD(String branch) throws SQLException {
		Integer nbr = 0;

		ResultSet count;

		if (branch.equals("Chn")) {
			// Get the result of Chinese query
			count = dataBase_MySQL.Query(SQLQuery.NUMBER_OF_EMPLOYEES_MYSQL);
			while (count.next()) {
				nbr = count.getInt("nbrempl");
			}
		} else {
			// Get the result of American query
			count = dataBase_POSTGRE.Query(SQLQuery.NUMBER_OF_EMPLOYEES_POSTGRESQL);
			while (count.next()) {
				nbr = count.getInt("nbrempl");
			}
		}

		return nbr;
	}

	/**
	 * This method is used to find the best employee from CSV data
	 * 
	 * @param information : a list of data from CSV in a List<List<String>>
	 * @param otherInformation : an other a list of data from CSV in a List<List<String>>
	 * @param typeCSV : the FR CSV or GER CSV in a String
	 * @return the result in a HashMap<String, Integer>
	 */
	public HashMap<String, Integer> monthEmployeeCSV(List<List<String>> information,
			List<List<String>> otherInformation, String typeCSV) {
		HashMap<String, Integer> bestNote = new HashMap<String, Integer>();
		String employeeName = "no employee";
		int noteBestEmployee = 0;
		int noteEmployee = 0;
		if (typeCSV.equals(CSV_Information.fR_CSV)) {
			for (List<String> employeeList : information) {
				try {
					noteEmployee = Integer.parseInt(employeeList.get(CSV_Information.ACHIEVEMENTS_FRANCE))
							- Integer.parseInt(employeeList.get(CSV_Information.BLAME_FRANCE));
					if (noteBestEmployee < noteEmployee) {

						noteBestEmployee = noteEmployee;
						employeeName = employeeList.get(CSV_Information.FAMILY_NAME_FRANCE) + " "
								+ employeeList.get(CSV_Information.FIRST_NAME_FRANCE);
					}
				} catch (Exception e) {
					logger.error("error during convertion of achievement and blame for best month employe FR");
				}
			}

		} else {
			for (List<String> employeListGER : otherInformation) {
				try {
					noteEmployee = Integer.parseInt(employeListGER.get(CSV_Information.ACHIEVEMENTS_GER))
							- Integer.parseInt(employeListGER.get(CSV_Information.BLAME_GER));
					if (noteBestEmployee < noteEmployee) {
						noteBestEmployee = noteEmployee;
						employeeName = employeListGER.get(CSV_Information.ID_GER);
					}
				} catch (Exception e) {
					logger.error("error during convertion of achievement and blame for best month employe GER");
				}
			}
			for (List<String> employe : information) {
				if ((employe.get(CSV_Information.ID_GER)).equals(employeeName)) {
					employeeName = employe.get(CSV_Information.FAMILY_NAME_GER) + " "
							+ employe.get(CSV_Information.FIRST_NAME_GER);
				}
			}
		}

		bestNote.put(employeeName, noteBestEmployee);
		return bestNote;
	}

 	/**
 	 * This method is used to find the best employee from databases data
 	 * 
 	 * @param branch : the branch of the company in a String
 	 * @return the result in a HashMap<String, Integer>
 	 * @throws SQLException
 	 */
	public HashMap<String, Integer> monthEmployeeBD(String branch) throws SQLException {
		HashMap<String, Integer> bestemployee = new HashMap<String, Integer>();
		ResultSet monthempl;

		Integer note_mthempl = 0;
		String name_mthempl = "default";

		if (branch.equals("Chn")) {
			// Get the result of Chinese query
			monthempl = dataBase_MySQL.Query(SQLQuery.MONTH_EMPLOYEE_MYSQL);
		} else {
			// Get the result of American query
			monthempl = dataBase_POSTGRE.Query(SQLQuery.MONTH_EMPLOYEE_POSTGRESQL);
		}
		while (monthempl.next()) {
			note_mthempl = monthempl.getInt("maxperf");
			name_mthempl = monthempl.getString("name") + " " + monthempl.getString("f_name");
		}

		bestemployee.put(name_mthempl, note_mthempl);
		return bestemployee;
	}

	/**
	 * This method is used to get the employees notes from CSV
	 * 
	 * @param information : a list of data from CSV in a List<List<String>>
	 * @param otherInformation : an other list of data from CSV in a List<List<String>>
	 * @param typeCSV : the FR CSV or GER CSV in a String
	 * @return the result in a HashMap<String, Integer>
	 */
	public HashMap<String, Integer> noteEmployeeCSV(List<List<String>> information, List<List<String>> otherInformation,
			String typeCSV) {
		HashMap<String, Integer> notes = new HashMap<String, Integer>();
		String employeeName = "no employe ";
		int noteEmployee = 0;
		if (typeCSV.equals(CSV_Information.fR_CSV)) {
			for (List<String> employeList : information) {
				try {
					noteEmployee = Integer.parseInt(employeList.get(CSV_Information.ACHIEVEMENTS_FRANCE))
							- Integer.parseInt(employeList.get(CSV_Information.BLAME_FRANCE));

					employeeName = employeList.get(CSV_Information.FAMILY_NAME_FRANCE) + " "
							+ employeList.get(CSV_Information.FIRST_NAME_FRANCE);

				} catch (Exception e) {
					logger.error("error during convertion of achievement and blame for best month employee FR ");
				}
			}
		} else {
			for (List<String> employeListGER : otherInformation) {
				try {
					noteEmployee = Integer.parseInt(employeListGER.get(CSV_Information.ACHIEVEMENTS_GER))
							- Integer.parseInt(employeListGER.get(CSV_Information.BLAME_GER));
					employeeName = employeListGER.get(CSV_Information.ID_GER);
					for (List<String> employe : information) {
						if ((employe.get(CSV_Information.ID_GER)).equals(employeeName)) {
							employeeName = employe.get(CSV_Information.FIRST_NAME_GER);
						}
					}
				} catch (Exception e) {
					logger.error("error during convert archivement and blame for best month employee GER ");
				}
			}
		}

		notes.put(employeeName, noteEmployee);
		return notes;
	}

	/**
	 * This method is used to get the employees notes from databases
	 * 
	 * @param branch : the branch of the company in a String
	 * @return the result in a HashMap<String, Integer>
	 * @throws SQLException
	 */
	public HashMap<String, Integer> noteEmployeeBD(String branch) throws SQLException {
		HashMap<String, Integer> notes = new HashMap<String, Integer>();
		ResultSet resultnotes;

		if (branch.equals("Chn")) {
			// Get the result of Chinese query
			resultnotes = dataBase_MySQL.Query(SQLQuery.NOTE_EMPLOYEES_MYSQL);
		} else {
			// Get the result of American query
			resultnotes = dataBase_POSTGRE.Query(SQLQuery.NOTE_EMPLOYEES_POSTGRESQL);
		}
		while (resultnotes.next()) {
			int note = resultnotes.getInt("note");
			String name = resultnotes.getString("name") + " " + resultnotes.getString("f_name");
			notes.put(name, note);
		}

		return notes;
	}

	/**
	 * This method is used to get the leave usage of employees from CSV
	 * 
	 * @param information : a list of data from CSV in a List<List<String>>
	 * @param typeCsv : the FR CSV or GER CSV in a String
	 * @return the result in an integer
	 */
	public Integer leaveUsageCSV(List<List<String>> information, String typeCsv) {
		int leaveUsageCount = 0;

		if (typeCsv.equals(CSV_Information.fR_CSV)) {
			for (List<String> list : information) {
				try {
					leaveUsageCount += Integer.parseInt(list.get(CSV_Information.LEAVE_FRANCE));
				} catch (Exception e) {
					logger.error("error during recuperation of French succursale leave usage");
				}

			}
		} else {
			for (List<String> list : information) {
				try {
					leaveUsageCount += Integer.parseInt(list.get(CSV_Information.LEAVE_GER));
				} catch (Exception e) {
					logger.error("error during recuperation of German succursale leave usage");
				}

			}
		}

		return leaveUsageCount;
	}


	/**
	 * This method is used to get the leave usage of employees from databases
	 * 
	 * @param branch : the branch of the company in a String
	 * @return the result in an integer
	 * @throws SQLException
	 */
	public Integer leaveUsageBD(String branch) throws SQLException {
		int leaveUsageCount = 0;

		ResultSet leave;

		if (branch.equals("Chn")) {
			// Get the result of Chinese query
			leave = dataBase_MySQL.Query(SQLQuery.LEAVE_USAGE_MYSQL);
			while (leave.next()) {
				leaveUsageCount = leave.getInt("vacationusage");
			}
		} else {
			// Get the result of American query
			leave = dataBase_POSTGRE.Query(SQLQuery.LEAVE_USAGE_POSTGRESQL);
			while (leave.next()) {
				leaveUsageCount = leave.getInt("leaveusage");
			}
		}

		return leaveUsageCount;
	}

	/**
	 * This method is used to get total of achievements by branch from CSV
	 * 
	 * @param information : a list of data from CSV in a List<List<String>>
	 * @param typeCsv : the FR CSV or GER CSV in a String
	 * @return the result in a HashMap<String, Integer>
	 */
	public HashMap<String, Integer> taskDoneCSV(List<List<String>> information, String typeCsv) {
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		String department = "";
		int add;
		if (typeCsv.equals(CSV_Information.fR_CSV)) {
			result.put("totalFr", 0);
			for (List<String> list : information) {
				try {
					department = list.get(CSV_Information.DEPARTMENT_FRANCE);
					if (result.containsKey(department)) {
						add = result.get(department);
						result.put(department, add + Integer.parseInt(list.get(CSV_Information.ACHIEVEMENTS_FRANCE)));
					} else {
						result.put(department, Integer.parseInt(list.get(CSV_Information.ACHIEVEMENTS_FRANCE)));
					}

					add = result.get("totalFr");
					result.put("total" + typeCsv,
							add + Integer.parseInt(list.get(CSV_Information.ACHIEVEMENTS_FRANCE)));

				} catch (Exception e) {
					logger.error("error during recuperation of achievements's French succursale ");

				}
			}
		} else {
			result.put("totalGer", 0);
			for (List<String> list : information) {
				try {
					department = list.get(CSV_Information.DEPARTMENT_GER);
					if (result.containsKey(department)) {
						add = result.get(department);
						result.put(department, add + Integer.parseInt(list.get(CSV_Information.ACHIEVEMENTS_GER)));
					} else {
						result.put(department, Integer.parseInt(list.get(CSV_Information.ACHIEVEMENTS_GER)));
					}
					add = result.get("totalGer");
					result.put("total" + typeCsv, add + Integer.parseInt(list.get(CSV_Information.ACHIEVEMENTS_GER)));
				} catch (Exception e) {
					// logger.error("error during recuperation of achievements's German succursale
					// ");
					// System.out.println(e.toString());
				}
			}
		}
		return result;
	}

	/**
	 * This method is used to get total of achievements by branch from databases
	 * 
	 * @param branch : the branch of the company in a String
	 * @return the result in a HashMap<String, Integer>
	 * @throws SQLException
	 */
	public HashMap<String, Integer> taskDoneBD(String branch) throws SQLException {
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		ResultSet resulttasks;

		if (branch.equals("Chn")) {
			// Get the result of Chinese query
			resulttasks = dataBase_MySQL.Query(SQLQuery.TASKS_DONE_MYSQL);
		} else {
			// Get the result of American query
			resulttasks = dataBase_POSTGRE.Query(SQLQuery.TASKS_DONE_POSTGRESQL);
		}

		// Save achievements for total sum
		int sum_achievements = 0;

		// Browse the query result and get data
		while (resulttasks.next()) {
			int tmp_achv = resulttasks.getInt("achievements");
			String dep = resulttasks.getString("department");
			result.put(dep, tmp_achv);
			sum_achievements = sum_achievements + tmp_achv;
		}
		// Total
		result.put("total" + branch, sum_achievements);

		return result;
	}

	/**
	 * This method is used to get fees by employee from CSV
	 * 
	 * @param information : a list of data from CSV in a List<List<String>>
	 * @param otherInformation : an other list of data from CSV in a List<List<String>>
	 * @param typeCsv : the FR CSV or GER CSV in a String
	 * @return the result in a HashMap<String, Integer>
	 */
	public HashMap<String, Integer> feesEmployeesCSV(List<List<String>> information,
			List<List<String>> otherInformation, String typeCsv) {
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		String employeeID = "";
		String name = "";
		Integer fees = 0;

		String maxname = "";
		Integer maxfees = 0;

		if (typeCsv.equals(CSV_Information.fR_CSV)) {
			for (List<String> employefeesList : information) {
				try {
					name = employefeesList.get(CSV_Information.FAMILY_NAME_FRANCE) + " "
							+ employefeesList.get(CSV_Information.FIRST_NAME_FRANCE);

					fees = Integer.parseInt(employefeesList.get(CSV_Information.FEES_FRANCE));

					if (fees >= maxfees) {
						maxname = name;
						maxfees = fees;
					}
				} catch (Exception e) {
					logger.error("Error during recuperation of employee fees for French succursale");
					// System.out.println(e.toString());
				}
			}
		} else {
			for (List<String> employeListGER : otherInformation) {
				try {
					employeeID = employeListGER.get(CSV_Information.ID_GER);
					fees = Integer.parseInt(employeListGER.get(CSV_Information.FEES_GER));
					for (List<String> employe : information) {
						if ((employe.get(CSV_Information.ID_GER)).equals(employeeID)) {
							name = employe.get(CSV_Information.FAMILY_NAME_GER) + " "
									+ employe.get(CSV_Information.FIRST_NAME_GER);

							if (fees >= maxfees) {
								maxname = name;
								maxfees = fees;
							}
						}

					}

				} catch (Exception e) {
					logger.error("error during convert archivement and blame for best Month employe ");
				}
			}

		}

		result.put(maxname, maxfees);

		return result;
	}

	/**
	 * This method is used to get fees by employee from databases
	 * 
	 * @param branch : the branch of the company in a String
	 * @return the result in a HashMap<String, Integer>
	 * @throws SQLException
	 */
	public HashMap<String, Integer> feesEmployeesBD(String branch) throws SQLException {
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		ResultSet resultfees;

		if (branch.equals("Chn")) {
			// Get the result of Chinese query
			resultfees = dataBase_MySQL.Query(SQLQuery.EXPENSIVE_EMPLOYEES_MYSQL);
		} else {
			// Get the result of American query
			resultfees = dataBase_POSTGRE.Query(SQLQuery.EXPENSIVE_EMPLOYEES_POSTGRESQL);
		}

		// Browse the query result and get data
		while (resultfees.next()) {
			result.put(resultfees.getString("employee_id"), resultfees.getInt("fees"));
		}

		return result;
	}

	/**
	 * This method is used to get result of employee by seniority from CSV
	 * 
	 * @param information : a list of data from CSV in a List<List<String>>
	 * @param otherInformation : an other list of data from CSV in a List<List<String>>
	 * @param typeCSV : the FR CSV or GER CSV in a String
	 * @return the result in a HashMap<String, Integer>
	 */
	public HashMap<Integer, Integer> resultBySeniorityCSV(List<List<String>> information,
			List<List<String>> otherInformation, String typeCSV) {
		HashMap<Integer, Integer> seniorityResults = new HashMap<Integer, Integer>();
		// HashMap key and value template variables
		Integer seniority;
		Integer results;

		String hiring_date;
		String employeeID;

		Integer formerValue;

		if (typeCSV.equals(CSV_Information.fR_CSV)) {
			for (List<String> list : information) {
				try {
					hiring_date = list.get(CSV_Information.HIRING_DATE_FRANCE);
					seniority = seniorityCalc(hiring_date);

					results = Integer.parseInt(list.get(CSV_Information.ACHIEVEMENTS_FRANCE));

					formerValue = seniorityResults.get(seniority);
					if (formerValue == null) {
						formerValue = 0;
					}

					seniorityResults.put(seniority, formerValue + results);

				} catch (Exception e) {
					logger.error("error during recuperation of French succursale's results by seniority");
				}
			}
		} else {
			for (List<String> list : information) {
				try {
					hiring_date = list.get(CSV_Information.HIRING_DATE_GER);
					seniority = seniorityCalc(hiring_date);

					employeeID = list.get(CSV_Information.ID_GER);

					for (List<String> employe : otherInformation) {
						if ((employe.get(CSV_Information.ID_GER)).equals(employeeID)) {
							results = Integer.parseInt(employe.get(CSV_Information.ACHIEVEMENTS_GER));

							formerValue = seniorityResults.get(seniority);
							if (formerValue == null) {
								formerValue = 0;
							}

							seniorityResults.put(seniority, formerValue + results);
						}

					}

				} catch (Exception e) {
					logger.error("error during recuperation of German succursale's results by seniority");
				}
			}
		}

		return seniorityResults;
	}

	/**
	 * This method is used to calculate the seniority of employee
	 * 
	 * @param hiring_date : the hiring date of the employee in a String
	 * @return the result of the calculation in an integer
	 */
	public Integer seniorityCalc(String hiring_date) {
		Integer seniority;

		// current date year, month and day
		now = LocalDateTime.now();
		timestamp = dtf.format(now);
		String[] timestamparray = timestamp.split("\\s+");
		String date = timestamparray[0];
		String[] splitdate = date.split("-");
		Integer currentYear = Integer.parseInt(splitdate[0]);
		Integer currentMonth = Integer.parseInt(splitdate[1]);
		Integer currentDay = Integer.parseInt(splitdate[2]);

		String delim = "/";

		String[] split_hd;

		// hiring date year, month and day
		Integer hdYear;
		Integer hdMonth;
		Integer hdDay;

		// differences between hiring and current dates
		Integer diffYear;
		Integer diffMonth;
		Integer diffDay;

		Integer dayDiffCount;

		// ------

		split_hd = hiring_date.split(delim);

		// French and German syntax : dd/MM/yyyy
		hdYear = Integer.parseInt(split_hd[2]);
		hdMonth = Integer.parseInt(split_hd[1]);
		hdDay = Integer.parseInt(split_hd[0]);

		diffYear = currentYear - hdYear;
		diffMonth = currentMonth - hdMonth;
		diffDay = currentDay - hdDay;

		dayDiffCount = diffYear * 365 + diffMonth * 30 + diffDay;

		seniority = dayDiffCount / 365;

		return seniority;
	};

	/**
	 * This method is used to get result of employee by seniority from databases
	 * 
	 * @param branch : the branch of the company in a String
	 * @return the result in a HashMap<Integer, Integer>
	 * @throws SQLException
	 */
	public HashMap<Integer, Integer> resultBySeniorityBD(String branch) throws SQLException {
		HashMap<Integer, Integer> seniorities = new HashMap<Integer, Integer>();
		ResultSet resultseniority;

		if (branch.equals("Chn")) {
			// Get the result of Chinese query
			resultseniority = dataBase_MySQL.Query(SQLQuery.RESULT_BY_SENIORITY_MYSQL);
		} else {
			// Get the result of American query
			resultseniority = dataBase_POSTGRE.Query(SQLQuery.RESULT_BY_SENIORITY_POSTGRESQL);
		}

		// Browse the query result and get data
		while (resultseniority.next()) {
			seniorities.put(resultseniority.getInt("seniority"), resultseniority.getInt("results"));
		}

		return seniorities;
	}

	/**
	 * This method is used to get result of the employment cost from CSV
	 * 
	 * @param information : a list of data from CSV in a List<List<String>>
	 * @param typeCSV : the FR CSV or GER CSV in a String
	 * @return the result in an integer
	 */
	public Integer employmentCostCSV(List<List<String>> information, String typeCSV) {
		Integer monthlyCost = 0;
		Integer weeklyCost = 0;

		Integer employeeCost = 0;

		if (typeCSV.equals(CSV_Information.fR_CSV)) {
			for (List<String> list : information) {
				try {
					employeeCost = Integer.parseInt(list.get(CSV_Information.HOURS_WORKED_BY_WEEK_FRANCE))
							* Integer.parseInt(list.get(CSV_Information.HOURLY_RATE_FRANCE));
					weeklyCost += employeeCost;
				} catch (Exception e) {
					logger.error(e.getMessage());
					logger.error("error during recuperation of French succursale's contract types");
				}
			}
		} else {
			for (List<String> list : information) {
				try {
					employeeCost = Integer.parseInt(list.get(CSV_Information.HOURS_WORKED_BY_WEEK_GER))
							* Integer.parseInt(list.get(CSV_Information.HOURLY_RATE_GER));
					weeklyCost += employeeCost;
				} catch (Exception e) {
					logger.error(e.getMessage());
					logger.error("error during recuperation of German succursale's contract types");
				}
			}
		}

		monthlyCost = 4 * weeklyCost;

		return monthlyCost;
	}

	/**
	 * This method is used to get result of the employment cost from databases
	 * 
	 * @param branch : the branch of the company in a String
	 * @return the result in an integer
	 * @throws SQLException
	 */
	public Integer employmentCostBD(String branch) throws SQLException {
		Integer monthlyCost = 0;
		Integer weeklyCost = 0;
		ResultSet employmentCostResult;

		if (branch.equals("Chn")) {
			// Get the result of Chinese query
			employmentCostResult = dataBase_MySQL.Query(SQLQuery.COST_OF_EMPLOYMENT_MYSQL);
		} else {
			// Get the result of American query
			employmentCostResult = dataBase_POSTGRE.Query(SQLQuery.COST_OF_EMPLOYMENT_POSTGRESQL);
		}

		// Browse the query result and get data
		while (employmentCostResult.next()) {
			weeklyCost = employmentCostResult.getInt("weeklycost");
		}

		monthlyCost = 4 * weeklyCost;

		return monthlyCost;

	}

	/**
	 * This method is used to determine the worst month employee from CSV
	 * 
	 * @param information : a list of data from CSV in a List<List<String>>
	 * @param otherInformation : an other list of data from CSV in a List<List<String>>
	 * @param typeCSV : the FR CSV or GER CSV in a String
	 * @return the result in a HashMap<String, Integer>
	 */
	public HashMap<String, Integer> worstMonthEmployeeCSV(List<List<String>> information,
			List<List<String>> otherInformation, String typeCSV) {
		HashMap<String, Integer> worstNote = new HashMap<String, Integer>();
		String employeeName = "no employee";
		int noteWorstEmployee = 0;
		int noteEmployee = 0;
		if (typeCSV.equals(CSV_Information.fR_CSV)) {
			for (List<String> employeeList : information) {
				try {
					noteEmployee = Integer.parseInt(employeeList.get(CSV_Information.ACHIEVEMENTS_FRANCE))
							- Integer.parseInt(employeeList.get(CSV_Information.BLAME_FRANCE));
					if (noteWorstEmployee > noteEmployee) {

						noteWorstEmployee = noteEmployee;
						employeeName = employeeList.get(CSV_Information.FAMILY_NAME_FRANCE) + " "
								+ employeeList.get(CSV_Information.FIRST_NAME_FRANCE);
					}
				} catch (Exception e) {
					logger.error("error during convertion of achievement and blame for worst month employe FR");
				}
			}

		} else {
			for (List<String> employeListGER : otherInformation) {
				try {
					noteEmployee = Integer.parseInt(employeListGER.get(CSV_Information.ACHIEVEMENTS_GER))
							- Integer.parseInt(employeListGER.get(CSV_Information.BLAME_GER));
					if (noteWorstEmployee > noteEmployee) {
						noteWorstEmployee = noteEmployee;
						employeeName = employeListGER.get(CSV_Information.ID_GER);
					}
				} catch (Exception e) {
					logger.error("error during convertion of achievement and blame for worst month employe GER");
				}
			}
			for (List<String> employe : information) {
				if ((employe.get(CSV_Information.ID_GER)).equals(employeeName)) {
					employeeName = employe.get(CSV_Information.FAMILY_NAME_GER) + " "
							+ employe.get(CSV_Information.FIRST_NAME_GER);
				}
			}
		}

		worstNote.put(employeeName, noteWorstEmployee);
		return worstNote;
	}

	/**
	 * This method is used to determine the worst month employee from databases
	 * 
	 * @param branch : the branch of the company in a String
	 * @return the result in an HashMap<String, Integer>
	 * @throws SQLException
	 */
	public HashMap<String, Integer> worstMonthEmployeeBD(String branch) throws SQLException {
		HashMap<String, Integer> worstemployee = new HashMap<String, Integer>();
		ResultSet wrstempl;

		Integer note_wrstempl = 0;
		String name_wrstempl = "default";

		if (branch.equals("Chn")) {
			// Get the result of Chinese query
			wrstempl = dataBase_MySQL.Query(SQLQuery.WORST_EMPLOYEE_MYSQL);
		} else {
			// Get the result of American query
			wrstempl = dataBase_POSTGRE.Query(SQLQuery.WORST_EMPLOYEE_POSTGRESQL);
		}
		while (wrstempl.next()) {
			note_wrstempl = wrstempl.getInt("minperf");
			name_wrstempl = wrstempl.getString("name") + " " + wrstempl.getString("f_name");
		}

		worstemployee.put(name_wrstempl, note_wrstempl);
		return worstemployee;
	}


	/**
	 * This method is used to get the different contract types from CSV
	 * 
	 * @param information : a list of data from CSV in a List<List<String>>
	 * @param typeCSV : the FR CSV or GER CSV in a String
	 * @return the result in a HashMap<String, Integer>
	 */
	public HashMap<String, Integer> contractTypesCSV(List<List<String>> information, String typeCSV) {
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		String contract = "";
		Integer formerValue;

		if (typeCSV.equals(CSV_Information.fR_CSV)) {
			for (List<String> list : information) {
				try {
					contract = list.get(CSV_Information.CONTRACT_FRANCE);

					formerValue = result.get(contract);
					if (formerValue == null) {
						formerValue = 0;
					}

					result.put(contract, formerValue + 1);
				} catch (Exception e) {
					logger.error(e.getMessage());
					logger.error("error during recuperation of French succursale's contract types");
				}
			}
		} else {
			for (List<String> list : information) {
				try {
					contract = list.get(CSV_Information.CONTRACT_GER);
					formerValue = result.get(contract);
					if (formerValue == null) {
						formerValue = 0;
					}

					result.put(contract, formerValue + 1);
				} catch (Exception e) {
					logger.error(e.getMessage());
					logger.error("error during recuperation of German succursale's contract types");
				}
			}
		}

		return result;
	}

	/**
 	 * This method is used to get the different contract types from databases
 	 * 
 	 * @param branch : the branch of the company in a String
 	 * @return the result in a HashMap<String, Integer>
 	 * @throws SQLException
 	 */
	public HashMap<String, Integer> contractTypesBD(String branch) throws SQLException {
		HashMap<String, Integer> contracts = new HashMap<String, Integer>();

		ResultSet resultcontract;

		if (branch.equals("Chn")) {
			// Get the result of Chinese query
			resultcontract = dataBase_MySQL.Query(SQLQuery.TYPE_OF_CONTRACT_MYSQL);
		} else {
			// Get the result of American query
			resultcontract = dataBase_POSTGRE.Query(SQLQuery.TYPE_OF_CONTRACT_POSTGRESQL);
		}

		// Browse the query result and get data
		while (resultcontract.next()) {
			contracts.put(resultcontract.getString("contract"), resultcontract.getInt("contractnb"));
		}

		return contracts;
	}

	/**
	 * This method allows to determine wages information from databases
	 * 
	 * @param branch : the branch of the company in a String
	 * @return the result in a HashMap<String, Integer>
	 * @throws SQLException
	 */
	public HashMap<String, Integer> wagesInfoBd(String branch) throws SQLException {
		HashMap<String, Integer> wagesinfo = new HashMap<String, Integer>();

		ResultSet resultwagesinfo;

		if (branch.equals("Chn")) {
			// Get the result of Chinese query
			resultwagesinfo = dataBase_MySQL.Query(SQLQuery.WAGES_INFORMATIONS_MYSQL);
		} else {
			// Get the result of American query
			resultwagesinfo = dataBase_POSTGRE.Query(SQLQuery.WAGES_INFORMATIONS_POSTGRESQL);
		}

		// Browse the query result and get data
		while (resultwagesinfo.next()) {

			wagesinfo.put(resultwagesinfo.getString("name") + resultwagesinfo.getString("f_name"),
					resultwagesinfo.getInt("wages"));
		}
		return wagesinfo;
	}

	/**
	 * This method allows to determine wages information from CSV
	 * 
	 * @param information : a list of data from CSV in a List<List<String>>
	 * @param otherInformation : an other list of data from CSV in a List<List<String>>
	 * @param typeCSV : the FR CSV or GER CSV in a String
	 * @return the result in an HashMap<String, Integer>
	 */
	public HashMap<String, Integer> wagesInforCSV(List<List<String>> information, List<List<String>> otherInformation,
			String typeCSV) {
		HashMap<String, Integer> result = new HashMap<String, Integer>();
		String people = "";
		Integer wages = 0;
		String employeeID;

		if (typeCSV.equals(CSV_Information.fR_CSV)) {
			for (List<String> list : information) {
				try {
					people = list.get(CSV_Information.FAMILY_NAME_FRANCE) + "  "
							+ list.get(CSV_Information.FIRST_NAME_FRANCE);
					try {
						wages = 0;
						wages = (int) ((int) Integer.parseInt(list.get(CSV_Information.HOURLY_RATE_FRANCE))
								* Integer.parseInt(list.get(CSV_Information.HOURS_WORKED_BY_WEEK_FRANCE)) * 4
								+ 1.25 * Integer.parseInt(list.get(CSV_Information.OVERTIME_FRANCE))
										* Integer.parseInt(list.get(CSV_Information.HOURLY_RATE_FRANCE))
								+ Integer.parseInt(list.get(CSV_Information.SOCIAL_BENEFITS_FRANCE))
								+ Integer.parseInt(list.get(CSV_Information.BONUS_FRANCE)));
					} catch (Exception e) {
						logger.error("error during convertion of achievement and blame for worst month employe GER");
					}
					result.put(people, wages);
				} catch (Exception e) {
					logger.error(e.getMessage());
					logger.error("error during recuperation of French succursale's contract types");
				}
			}
		} else {
			for (List<String> list : information) {
				try {
					people = list.get(CSV_Information.FAMILY_NAME_GER) + " " + list.get(CSV_Information.FIRST_NAME_GER);
					try {
						employeeID = list.get(CSV_Information.ID_GER);

						for (List<String> employe : otherInformation) {
							if ((employe.get(CSV_Information.ID_GER)).equals(employeeID)) {
								wages=0;
								wages = (int) ((int) Integer.parseInt(list.get(CSV_Information.HOURLY_RATE_GER))
										* Integer.parseInt(list.get(CSV_Information.HOURS_WORKED_BY_WEEK_GER)) * 4
										+ 1.25 * Integer.parseInt(employe.get(CSV_Information.OVERTIME_GER))
												* Integer.parseInt(list.get(CSV_Information.HOURLY_RATE_GER))
										+ Integer.parseInt(employe.get(CSV_Information.SOCIAL_BENEFITS_GER))
										+ Integer.parseInt(employe.get(CSV_Information.BONUS_GER)));

							}
						}
					} catch (Exception e) {
						logger.error("error during convertion of achievement and blame for worst month employe GER");
					}
					result.put(people, wages);
				} catch (Exception e) {
					logger.error(e.getMessage());
					logger.error("error during recuperation of German succursale's contract types");
				}

			}
		}
		return result;
	}

	/**
	 * This method allows to get information for pay slip from CSV
	 * 
	 * @param information : a list of data from CSV in a List<List<String>>
	 * @param otherInformation : an other list of data from CSV in a List<List<String>>
	 * @param typeCSV : the FR CSV or GER CSV in a String
	 * @return the result in a List<String>
	 */
	public List<String> payFichCSV(List<List<String>> information, List<List<String>> otherInformation,
			String typeCSV) {
		List<String> result = new ArrayList<String>();
		int wagesBrut = 0;
		int finalWages = 0;
		String employeeID;
		String people=null;
		if (typeCSV.equals(CSV_Information.fR_CSV)) {
			for (List<String> list : information) {
				try {

					try {
						wagesBrut = 0;
						wagesBrut = (int) ((int) Integer.parseInt(list.get(CSV_Information.HOURLY_RATE_FRANCE))
								* Integer.parseInt(list.get(CSV_Information.HOURS_WORKED_BY_WEEK_FRANCE)) * 4
								+ 1.25 * Integer.parseInt(list.get(CSV_Information.OVERTIME_FRANCE))
										* Integer.parseInt(list.get(CSV_Information.HOURLY_RATE_FRANCE))
								+ Integer.parseInt(list.get(CSV_Information.SOCIAL_BENEFITS_FRANCE))
								+ Integer.parseInt(list.get(CSV_Information.BONUS_FRANCE)));

						finalWages = wagesBrut - Integer.parseInt(list.get(CSV_Information.TAX_FRANCE));
					} catch (Exception e) {
						logger.error("error during convertion of achievement and blame for worst month employe GER");
					}
					 people = list.get(CSV_Information.FAMILY_NAME_FRANCE) + " "
							+ list.get(CSV_Information.FIRST_NAME_FRANCE) + " "
							+ list.get(CSV_Information.ADRESSE_FRANCE) + " "
							+ list.get(CSV_Information.SOCIAL_SEC_NB_FRANCE) + " "
							+ list.get(CSV_Information.POSITION_FRANCE) + " "
							+ list.get(CSV_Information.DEPARTMENT_FRANCE) + " " + list.get(CSV_Information.GROUP_FRANCE)
							+ " " + list.get(CSV_Information.CONTRACT_FRANCE) + " "
							+ list.get(CSV_Information.HIRING_DATE_FRANCE) + " "
							+ list.get(CSV_Information.CONTRACT_LENGTH_FRANCE) + " "
							+ list.get(CSV_Information.HOURS_WORKED_BY_WEEK_FRANCE) + " "
							+ list.get(CSV_Information.BANK_ACCOUNT_FRANCE) + " gross salary "+wagesBrut+" net salary "+finalWages+"\n";
					
					result.add(people);
				} catch (Exception e) {
					logger.error(e.getMessage());
					logger.error("error during recuperation of French succursale's contract types");
				}
			}
		} else {
			for (List<String> list : information) {
				try {
					try {
						employeeID = list.get(CSV_Information.ID_GER);

						for (List<String> employe : otherInformation) {
							if ((employe.get(CSV_Information.ID_GER)).equals(employeeID)) {
								wagesBrut = 0;
								wagesBrut = (int) ((int) Integer.parseInt(list.get(CSV_Information.HOURLY_RATE_GER))
										* Integer.parseInt(list.get(CSV_Information.HOURS_WORKED_BY_WEEK_GER)) * 4
										+ 1.25 * Integer.parseInt(employe.get(CSV_Information.OVERTIME_GER))
												* Integer.parseInt(list.get(CSV_Information.HOURLY_RATE_GER))
										+ Integer.parseInt(employe.get(CSV_Information.SOCIAL_BENEFITS_GER))
										+ Integer.parseInt(employe.get(CSV_Information.BONUS_GER)));
								finalWages = wagesBrut - Integer.parseInt(employe.get(CSV_Information.TAX_GER));
								
								 people = list.get(CSV_Information.FAMILY_NAME_GER) + " "
										+ list.get(CSV_Information.FIRST_NAME_GER) + " "
										+ list.get(CSV_Information.ADRESSE_GER) + " "
										+ list.get(CSV_Information.SOCIAL_SEC_NB_GER) + " "
										+ employe.get(CSV_Information.POSITION_GER) + " "
										+ employe.get(CSV_Information.DEPARTMENT_GER) + " " + employe.get(CSV_Information.GROUP_GER)
										+ " " + list.get(CSV_Information.CONTRACT_GER) + " "
										+ list.get(CSV_Information.HIRING_DATE_GER) + " "
										+ list.get(CSV_Information.CONTRACT_LENGTH_GER) + " "
										+ list.get(CSV_Information.HOURS_WORKED_BY_WEEK_GER) + " "
										+ list.get(CSV_Information.BANK_ACCOUNT_GER) + " gross salary "+wagesBrut+" net salary "+finalWages+"\n";
							}
						}
					} catch (Exception e) {
						logger.error("error during convertion of achievement and blame for worst month employe GER");
					}
					result.add(people);
				} catch (Exception e) {
					logger.error(e.getMessage());
					logger.error("error during recuperation of German succursale's contract types");
				}

			}
		}

		return result;
	}
}
