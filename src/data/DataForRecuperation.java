package data;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

/**
 * This class gives data for information recuperation in databases and CSV files
 * 
 * @author Kevin BERNARD, Raphael D'URSO, Laura FUSTINONI, Aelien MOUBECHE
 *
 */
public class DataForRecuperation {

	// static variable for connection on mysql database
	public static String DATABASE_URL_MYSQL = "mysql-globalresourcehuman.alwaysdata.net:3306/globalresourcehuman_data_ch";
	public static String DATABASE_USER_MYSQL = "225252_admin";
	public static String DATABASE_PASSWORD_MYSQL = "ResourceHuman123456*";
	public static String DATABASE_MYSQL = "MYSQL";

	// for PosgreSQL
	public static String DATABASE_URL_POSTGRESQL = "postgresql-globalresourcehuman.alwaysdata.net:5432/globalresourcehuman_data_us";
	public static String DATABASE_USER_POSTGRESQL = "globalresourcehuman_admin";
	public static String DATABASE_PASSWORD_POSTGRESQL = "ResourceHuman123456*";
	public static String DATABASE_POSTGRESQL = "POSTGRESQL";
	
	// for csv file
	public static String CSV_FR ="src/CSV/france.csv";
	public static String CSV_ALL ="src/CSV/germany_1.csv";
	public static String CSV_ALL2 ="src/CSV/germany_2.csv";
}