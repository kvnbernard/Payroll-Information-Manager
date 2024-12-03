package data;

/**
 * This class allows to manage data for bar chart graphic
 * 
 * @author Kevin BERNARD, Raphael D'URSO, Laura FUSTINONI, Aelien MOUBECHE
 *
 */
public class DataForBarChartGraphic {
	private int value=0;
	private String  compareValue;
	private String 	WhoHaveValue;
	
	/**
	 * This method allows to get the value of bar chart graphic
	 * 
	 * @return the value in integer
	 */
	public int getValue() {
		return value;
	}
	
	/**
	 * This method allows to get the compare value
	 * 
	 * @return the compare value in String
	 */
	public String getCompareValue() {
		return compareValue;
	}
	
	/**
	 * This method allows to get who have the value
	 * 
	 * @return a String with the answer to the question
	 */
	public String getWhoHaveValue() {
		return WhoHaveValue;
	}
	
	/**
	 * This method allows to set the value of bar chart graphic
	 * 
	 * @param value : the new value in integer
	 */
	public void setValue(int value) {
		this.value = value;
	}
	
	/**
	 * This method allows to set the compare value
	 * 
	 * @param compareValue : the new compare value in String
	 */
	public void setCompareValue(String compareValue) {
		this.compareValue = compareValue;
	}
	
	/**
	 * This method allows to set who have the value
	 * 
	 * @param whoHaveValue : the new string answering to this question
	 */
	public void setWhoHaveValue(String whoHaveValue) {
		WhoHaveValue = whoHaveValue;
	}
	
	/**
	 * Constructor.
	 * This method allows to give data for bar chart graphic
	 * 
	 * @param value : the value of the bar chart graphic in integer
	 * @param compareValue : the compare value of the bar chart graphic in String
	 * @param whoHaveValue : the answer to the question in String
	 */
	public DataForBarChartGraphic(int value, String compareValue, String whoHaveValue) {
		this.value = value;
		this.compareValue = compareValue;
		this.WhoHaveValue = whoHaveValue;
	}
}
