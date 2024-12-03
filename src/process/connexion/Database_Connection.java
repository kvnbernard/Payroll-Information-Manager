package process.connexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import data.DataForRecuperation;
import org.apache.log4j.Logger;

import logger.LoggerUtility;

/**
 * This class process the database connection
 * 
 * @author Kevin BERNARD, Raphael D'URSO, Laura FUSTINONI, Aelien MOUBECHE
 *
 */
public class Database_Connection {
	
	private Connection connection;
	private static Logger logger = LoggerUtility.getLogger(Database_Connection.class, LoggerUtility.LOG_PREFERENCE);
	private String name;
	
	/**
	 * This method allows to connect the java application to the database
	 * 
	 * @param url : the URL of the database in a String
	 * @param user : the user of the database in a String
	 * @param password : the password of the database in a String
	 * @param database : the type of database (MySQL or PostgreSQL)
	 * @throws SQLException
	 */
	public Database_Connection(String url, String user, String password, String database) throws SQLException {
		
		if(database.equals(DataForRecuperation.DATABASE_POSTGRESQL) ) {
		logger.info("Start connection to " + url);
		connection = DriverManager.getConnection("jdbc:postgresql://" + url, user, password);
		//if we are here, we are connected
		logger.info("Database "+database+" connected !");
		}
		else {
			logger.info("Start connection to " + url);
			connection = DriverManager.getConnection("jdbc:mysql://" +url , user, password);
			//if we are here, we are connected
			logger.info("Database "+database+" connected !");
		}
		name = database;
		
	}
	
	/**
	 * This method is used to take some data from database
	 * 
	 * @param query : the query to the database in a String
	 * @return ResultSet used to take information about workers or create statistics
	 * @throws SQLException
	 */
	public ResultSet Query(String query) throws SQLException {
		
		PreparedStatement preparedStatement = connection.prepareStatement(query);
		return preparedStatement.executeQuery();
		
	}
	
	/**
	 * This method allows to get the name of the database
	 * 
	 * @return the name of the database in a String
	 */
	public String getName() {
		
		return name;
		
	}
	
}
