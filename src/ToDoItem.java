
import java.util.Calendar;
import java.util.ArrayList;

public class ToDoItem {
	private String item; // name of task
	private Calendar cal; // internal calendar
	private String repeat; // Never, Daily, Weekly
	private ArrayList dateList; //dates to repeat on
	private boolean visible; // Should it be displayed?
//TODO: Update calendar variable	
	
	public ToDoItem(String item, String rep){
		this.item = item; 
		this.cal = Calendar.getInstance();
		repeat = rep;
		visible = true;
		dateList = new ArrayList();
		if (repeat.equals("Daily")){
			dateList.add(new Integer(Calendar.MONDAY));
			dateList.add(new Integer(Calendar.TUESDAY));
			dateList.add(new Integer(Calendar.WEDNESDAY));
			dateList.add(new Integer(Calendar.THURSDAY));
			dateList.add(new Integer(Calendar.FRIDAY));
			dateList.add(new Integer(Calendar.SATURDAY));
			dateList.add(new Integer(Calendar.SUNDAY));
			
		} else if (repeat.equals("Weekly")){
			int day = cal.get(Calendar.DAY_OF_WEEK);
			dateList.add(new Integer(day));
		}
		//System.out.println(dateList.toString());
	}
	public void setRepeat (String rep){
		repeat = rep;
		if (rep.equals("None")) {
			dateList.clear();
		} else if (rep.equals("Daily")) {
			dateList.add(new Integer(Calendar.MONDAY));
			dateList.add(new Integer(Calendar.TUESDAY));
			dateList.add(new Integer(Calendar.WEDNESDAY));
			dateList.add(new Integer(Calendar.THURSDAY));
			dateList.add(new Integer(Calendar.FRIDAY));
			dateList.add(new Integer(Calendar.SATURDAY));
			dateList.add(new Integer(Calendar.SUNDAY));
		}
	}
	public void setRepeat (ArrayList dateList) {
		this.dateList = dateList;
		if (dateList.size() == 1) {
			setRepeat("Weekly");
		} else if (dateList.size() == 7) {
			setRepeat("Daily");
		} else {
			setRepeat("None");
		}
	}
	public void setItem(String item){
		this.item = item;
	}
	public void setVisible(boolean vis){
		visible = vis;
	}
	public boolean getVisible(){
		return visible;
	}
	public String getRepeat(){
		return repeat;
	}
	public ArrayList getDateList() {
		return dateList;
	}
	public void setCal(Calendar cal){
		this.cal = cal;
	}
	public Calendar getCal(){
		return this.cal;
	}
	public String toString(){
		return item;
	}
	

}
