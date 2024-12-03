package data;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * This class organize data results of mediator
 * 
 * @author Kevin BERNARD, Raphael D'URSO, Laura FUSTINONI, Aelien MOUBECHE
 * @version
 *
 */
public class MediatorResult {
	private List<String> result;
	// is use for the all graphic could be create 
	private String pedagogie;
	private String information;
	private String graphicTitle;

	// is use for circular graphic
	List<DataforCircularGraphic> circularGraphic = new ArrayList<DataforCircularGraphic>();
	
	// is use for linear graphic 
	List<DataForLinearGraphic> linearGraphics = new ArrayList<DataForLinearGraphic>();
	private String valueX;
	private String valueY;
	private String nameCourbe;
	
	// is use for BarChart graphic 
	List<DataForBarChartGraphic> BarChartGraphic = new ArrayList<DataForBarChartGraphic>();
	private String valueCompare;
	
	/**
	 * This method allows to get information about mediator
	 * 
	 * @return information in a String
	 */
	public String getInformation() {
		return information;
	}

	/**
	 * This method allows to get the data for circular graphic
	 * 
	 * @return the data in a List<DataforCircularGraphic>
	 */
	public List<DataforCircularGraphic> getCicularGraphic() {
		return circularGraphic;
	}

	/**
	 * This method allows to get the data for linear graphic
	 * 
	 * @return the data in a List<DataForLinearGraphic>
	 */
	public List<DataForLinearGraphic> getLinearGraphics() {
		return linearGraphics;
	}

	/**
	 * This method allows to get the value of the X axis
	 * 
	 * @return the value in a String
	 */
	public String getValueX() {
		return valueX;
	}

	/**
	 * This method allows to get the value of the Y axis
	 * 
	 * @return the value in a String
	 */
	public String getValueY() {
		return valueY;
	}

	/**
	 * This method allows to get the name of the curve
	 * 
	 * @return the name in a String
	 */
	public String getNameCourbe() {
		return nameCourbe;
	}

	/**
	 * This method allows to get the data for bar chart graphic
	 * 
	 * @return the data in a List<DataForBarChartGraphic>
	 */
	public List<DataForBarChartGraphic> getBarChartGraphic() {
		return BarChartGraphic;
	}

	/**
	 * This method allows to get the compare value
	 * 
	 * @return the value in a String
	 */
	public String getValueCompare() {
		return valueCompare;
	}

	/**
	 * This method allows to set the data of circular graphic
	 * 
	 * @param cicularGraphic : the new data in a List<DataforCircularGraphic>
	 */
	public void setCicularGraphic(List<DataforCircularGraphic> cicularGraphic) {
		this.circularGraphic = cicularGraphic;
	}

	/**
	 * This method allows to set the data of linear graphic
	 * 
	 * @param linearGraphics : the new data in a List<DataForLinearGraphic>
	 */
	public void setLinearGraphics(List<DataForLinearGraphic> linearGraphics) {
		this.linearGraphics = linearGraphics;
	}

	/**
	 * This method allows to set the value of the X axis
	 * 
	 * @param valueX : the new value in a String
	 */
	public void setValueX(String valueX) {
		this.valueX = valueX;
	}

	/**
	 * This method allows to set the value of the Y axis
	 * 
	 * @param valueY : the new value in a String
	 */
	public void setValueY(String valueY) {
		this.valueY = valueY;
	}

	/**
	 * This method allows to set the name of the curve
	 * 
	 * @param nameCourbe : the new name of the curve in a String
	 */
	public void setNameCourbe(String nameCourbe) {
		this.nameCourbe = nameCourbe;
	}

	/**
	 * This method allows to set the data of bar chart graphic
	 * 
	 * @param barChartGraphic : the new data in a List<DataForBarChartGraphic>
	 */
	public void setBarChartGraphic(List<DataForBarChartGraphic> barChartGraphic) {
		BarChartGraphic = barChartGraphic;
	}

	/**
	 * This method allows to set the compare value
	 * 
	 * @param valueCompare : the new compare value in a String
	 */
	public void setValueCompare(String valueCompare) {
		this.valueCompare = valueCompare;
	}

	/**
	 * Constructor
	 * This method allows to organize data results of mediator
	 * 
	 * @param pedagogie : a String explaining the results
	 */
	public MediatorResult( String pedagogie) {
		this.result= new  ArrayList<String>();
		this.pedagogie = pedagogie;
	}
	
	/**
	 * This method allows to get the results of mediator
	 * 
	 * @return the results in a List<String>
	 */
	public List<String> getResult() {
		return result;
	}
	
	/**
	 * This method allow to get user pedagogy
	 * 
	 * @return the pedagogy in a String
	 */
	public String getPedagogie() {
		return pedagogie+"\n ---\n"+information;
	}
	
	/**
	 * This method allows to set information about mediator
	 * 
	 * @param information : the new information in a String
	 */
	public void setInformation(String information) {
		this.information = information;
	}
	
	/**
	 * This method allows to set result of mediator
	 * 
	 * @param result : the new result in a List<String>
	 */
	public void setResult(List<String> result) {
		this.result = result;
	}
	
	/**
	 * This method allows to put/add a result
	 * 
	 * @param value : the value to put/add in a String
	 */
	public void put (String value) {
		result.add(value);
	}
	
	/**
	 * This method allows to set user pedagogy
	 * 
	 * @param pedagogie : the new pedagogy in a String
	 */
	public void setPedagogie(String pedagogie) {
		this.pedagogie = pedagogie;
	}
	
	/**
	 * This method add data to circular graphic
	 * 
	 * @param data : the data in DataforCircularGraphic type
	 */
	public void addTocircularGraphic(DataforCircularGraphic data) {
		circularGraphic.add(data);
	}
	
	/**
	 * This method allows to get the title of the graphic
	 * 
	 * @return the title in a String
	 */
	public String getGraphicTitle() {
		return graphicTitle;
	}

	/**
	 * This method allows to set the title of the graphic
	 * 
	 * @param graphicTitle : the new title of the graphic in a String
	 */
	public void setGraphicTitle(String graphicTitle) {
		this.graphicTitle = graphicTitle;
	}
	
}
