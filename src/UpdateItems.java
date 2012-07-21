import java.util.TimerTask;


public class UpdateItems extends TimerTask {
	private ToDoView todo;
	
	public UpdateItems(ToDoView todo){
		this.todo = todo;
	}
	public void run() {
		todo.timeUpdate();
	}

}
