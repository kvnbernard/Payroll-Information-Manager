package process.connexion;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;

import logger.LoggerUtility;
/**
 * This class process the CSV information recuperation
 * 
 * @author Kevin BERNARD, Raphael D'URSO, Laura FUSTINONI, Aelien MOUBECHE
 *
 */
public class CsvRecuperation {
	private static Logger logger = LoggerUtility.getLogger(CsvRecuperation.class, LoggerUtility.LOG_PREFERENCE);
	private List<String> lines = new ArrayList<String>();

	/**
	 * Constructor.
	 * This method allows CSV data recuperation
	 * 
	 * @param PathName : the pathname of the CSV file in a String
	 */
	public CsvRecuperation(String PathName) {
		try {
			File csv = new File(PathName);
			
			if (csv.exists()) {
				logger.info(csv +" Found ");
				FileReader rdcsv = new FileReader(csv);
				BufferedReader brcsv = new BufferedReader(rdcsv);
				for (String line = brcsv.readLine(); line != null; line = brcsv.readLine()) {
					lines.add(line);
				}
				logger.info(csv  + "  data collected");
				brcsv.close();
				rdcsv.close();
			}else {
				logger.info(csv.getAbsolutePath()+": impossible to read file");
			}

		} catch (NullPointerException e) {
			logger.error(PathName + " File not found");
		} catch (IOException e) {
			logger.error("File not read ");
		}
	}

	/**
	 * This method takes the lines and split all line of the FR CSV to have information ready to use
	 * 
	 * @return information about employee of this CSV in a List<List<String>>
	 */
	public List<List<String>> SepareLineFR() {
		List<List<String>> employeList = new ArrayList<List<String>>();
		
		for (int line = 0; line < lines.size(); line++) {
			String[] info = lines.get(line).split(",");
			List<String> employe = new ArrayList<String>();
			for (int word = 0; word < info.length; word++) {
				try {
					employe.add(info[word]);
				} catch (NullPointerException e) {
					logger.error("Error during recuperation data on " + lines.toString());

				}
			}
			employeList.add(employe);
		}
			//System.out.println(employe.toString());
		return employeList;
	}
	/**
	 * This method takes the lines and split all line of the GER CSV to have information ready to use
	 * 
	 * @return information about employee of this CSV in a List<List<String>>
	 */
	public List<List<String>> SepareLineGER() {
		List<List<String>> employeList = new ArrayList<List<String>>();
		
		for (int line = 0; line < lines.size(); line++) {
			String[] info = lines.get(line).split(",");
			List<String> employe = new ArrayList<String>();
			for (int word = 0; word < info.length; word++) {
				try {
					employe.add(info[word]);
				} catch (NullPointerException e) {
					logger.error("Error during recuperation data on " + lines.toString());

				}
			}
			employeList.add(employe);
		}
			//System.out.println(employe.toString());
		return employeList;
	}

}
