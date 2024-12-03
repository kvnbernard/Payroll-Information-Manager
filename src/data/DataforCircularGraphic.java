package data;

/**
 * This class allows to manage data for circular graphic
 * 
 * @author Kevin BERNARD, Raphael D'URSO, Laura FUSTINONI, Aelien MOUBECHE
 *
 */
public class DataforCircularGraphic {
	
	private int values=0;
	private String WhoValues;
	
	/**
	 * This method allows to get the value of circular graphic
	 * 
	 * @return the value in integer
	 */
	public int getValues() {
		
		return values;
		
	}
	
	/**
	 * This method allows to get who have the value
	 * 
	 * @return a String with the answer to the question
	 */
	public String getWhoValues() {
		
		return WhoValues;
		
	}
	
	/**
	 * This method allows to set the value of bar chart graphic
	 * 
	 * @param values : the new value in integer
	 */
	public void setValues(int values) {
		
		this.values = values;
		
	}
	
	/**
	 * This method allows to set who have the value
	 * 
	 * @param whoValues : the new string answering to this question
	 */
	public void setWhoValues(String whoValues) {
		
		WhoValues = whoValues;
		
	}
	
	/**
	 * Constructor.
	 * This method allows to give data for circular graphic
	 * 
	 * @param values : the value of the circular graphic in integer
	 * @param whoValues : the answer to the question in String
	 */
	public DataforCircularGraphic(int values, String whoValues) {
		
		this.values = values;
		this.WhoValues = whoValues;
		
	}
	
}
