package logger;

import org.apache.log4j.Logger;
import org.apache.log4j.PropertyConfigurator;

/**
 * Utility class used to generate Log4j logger.
 * 
 * We can generate logs in a text or a html file.
 * 
 * @author Tianxiao.Liu@u-cergy.fr
 */
public class LoggerUtility {
	private static final String TEXT_LOG_CONFIG = "log/log4j-text.properties";
	private static final String HTML_LOG_CONFIG = "log/log4j-html.properties";
	
	/**
	 * Change this value in order to change the log type ("text" or "html")
	 */
	public static final String LOG_PREFERENCE = "text";

	public static Logger getLogger(Class<?> logClass, String logFileType) {
		if (logFileType.equals("text")) {
			PropertyConfigurator.configure(TEXT_LOG_CONFIG);
		} else if (logFileType.equals("html")) {
			PropertyConfigurator.configure(HTML_LOG_CONFIG);
		} else {
			throw new IllegalArgumentException("Unknown log file type !");
		}

		String className = logClass.getName();
		return Logger.getLogger(className);
	}
}