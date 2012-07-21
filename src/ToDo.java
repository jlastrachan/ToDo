
import java.util.*;

public class ToDo {

	/**
	 * @param args
	 */
	
	public static void main(String[] args) 
	{
		ToDoView todo = new ToDoView();
		UpdateItems runnable = new UpdateItems(todo); // updates ToDoView
		Calendar cal = Calendar.getInstance();
		Timer timer = new Timer();
	    timer.scheduleAtFixedRate(
	      runnable,
	      1000, //1000 * 60 * 60 * 24,
	      10000
	    );
	    System.out.println("Timer scheduled");	
	}
	

}

