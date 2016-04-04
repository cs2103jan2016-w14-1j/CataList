package ui.Controllers;

import java.util.ArrayList;

import org.joda.time.LocalDateTime;
import org.joda.time.LocalTime;
import org.joda.time.format.DateTimeFormat;

import javafx.collections.ObservableList;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import logic.Task;

public class TaskFilter {
	private static final String FILTER_PLACEHOLDER = " : ";
	private static final String DATE_FORMAT = "dd/MM/yy";
	private static final String TIME_FORMAT = "HHmm";

	private static final String FLOAT_ID = "classFloat";
	private static final String OTHERS_ID = "classOthers";
	private static final String TOMORROW_ID = "classTomorrow";
	private static final String TODAY_ID = "classToday";
	private static final String OVERDUE_ID = "classOverdue";
	private static final String NUM_ID = "numTask";
	
	private static final String FLOAT_TASK_ID = "taskFloat";
	private static final String OTHERS_TASK_ID = "taskOthers";
	private static final String TOMORROW_TASK_ID = "taskTomorrow";
	private static final String TODAY_TASK_ID = "taskToday";
	private static final String OVERDUE_TASK_ID = "taskOverdue";

	private static final int CLASS_SPACE = 20;

	private static final String TODAY_CLASS = "Today";
	private static final String TOMORROW_CLASS = "Tomorrow";
	private static final String FLOAT_CLASS = "Tentative";
	private static final String OTHERS_CLASS = "Upcoming";
	private static final String OVERDUE_CLASS = "Overdue";

	private ArrayList<HBox> tasksToday;
	private ArrayList<HBox> tasksTomorrow;
	private ArrayList<HBox> tasksOthers;
	private ArrayList<HBox> tasksFloat;
	private ArrayList<HBox> tasksOverdue;

	private HBox taskClassToday;
	private HBox taskClassTomorrow;
	private HBox taskClassOthers;
	private HBox taskClassFloat;
	private HBox taskClassOverdue;
	

	public TaskFilter() {
		tasksToday = new ArrayList<HBox>();
		tasksTomorrow = new ArrayList<HBox>();
		tasksOthers= new ArrayList<HBox>();
		tasksFloat = new ArrayList<HBox>();
		tasksOverdue = new ArrayList<HBox>();

		initClasses();
	}
	
	public void getTasksToday(ObservableList<HBox> list) {
		list.clear();
		for(int i = 1; i < tasksToday.size(); i++) {
			list.add(tasksToday.get(i));
		}
	}
	
	public void getTasksTomorrow(ObservableList<HBox> list) {
		list.clear();
		for(int i = 1; i < tasksTomorrow.size(); i++) {
			list.add(tasksTomorrow.get(i));
		}
	}
	
	public void getTasksOthers(ObservableList<HBox> list) {
		list.clear();
		for(int i = 1; i < tasksOthers.size(); i++) {
			list.add(tasksOthers.get(i));
		}
	}
	
	public void getTasksOverdue(ObservableList<HBox> list) {
		list.clear();
		for(int i = 1; i < tasksOverdue.size(); i++) {
			list.add(tasksOverdue.get(i));
		}
	}
	
	public void getTasksFloat(ObservableList<HBox> list) {
		list.clear();
		for(int i = 1; i < tasksFloat.size(); i++) {
			list.add(tasksFloat.get(i));
		}
	}

	private void initClasses() {
		taskClassToday = new HBox(CLASS_SPACE);
		Label taskToday = new Label(TODAY_CLASS);
		taskToday.setId(TODAY_ID);
		taskClassToday.getChildren().add(taskToday);

		taskClassTomorrow = new HBox(CLASS_SPACE);
		Label taskTomorrow = new Label(TOMORROW_CLASS);
		taskTomorrow.setId(TOMORROW_ID);
		taskClassTomorrow.getChildren().add(taskTomorrow);

		taskClassOthers = new HBox(CLASS_SPACE);
		Label taskOthers = new Label(OTHERS_CLASS);
		taskOthers.setId(OTHERS_ID);
		taskClassOthers.getChildren().add(taskOthers);

		taskClassFloat = new HBox(CLASS_SPACE);
		Label taskFloat = new Label(FLOAT_CLASS);
		taskFloat.setId(FLOAT_ID);
		taskClassFloat.getChildren().add(taskFloat);
		
		taskClassOverdue = new HBox(CLASS_SPACE);
		Label taskOverdue = new Label(OVERDUE_CLASS);
		taskOverdue.setId(OVERDUE_ID);
		taskClassOverdue.getChildren().add(taskOverdue);
	}
	
	private void clearAll() {
		tasksToday.clear();
		tasksTomorrow.clear();
		tasksOthers.clear();
		tasksFloat.clear();
		tasksOverdue.clear();
	}

	public void addSortedClasses(ObservableList<HBox> list) {
		int taskNum = 1;
		while(taskNum <= tasksToday.size() + tasksTomorrow.size() +
				tasksFloat.size() + tasksOthers.size() + tasksOverdue.size()) {
			
			final int overdueSize = tasksOverdue.size();
			final int overdueTodaySize = tasksOverdue.size() + tasksToday.size();
			final int overdueTodayTomorrowSize = tasksOverdue.size() + tasksToday.size() + tasksTomorrow.size();
			final int overdueTodayTomorrowOthersSize = tasksOverdue.size() + tasksToday.size() + tasksTomorrow.size() + tasksOthers.size();
			
			if(taskNum <= overdueSize) {
				addOverdueToList(list, taskNum);	
			} else if(taskNum >= overdueSize && taskNum <= overdueTodaySize) {
				addTodayToList(list, taskNum);
			} else if(taskNum >= overdueTodaySize && taskNum <= overdueTodayTomorrowSize) {
				addTomorrowToList(list, taskNum);
			} else if(taskNum >= overdueTodayTomorrowSize && taskNum <= overdueTodayTomorrowOthersSize) {
				addOthersToList(list, taskNum);
			} else if(taskNum >= overdueTodayTomorrowOthersSize) {
				addFloatToList(list, taskNum);
			} 
			taskNum++;
		}
		clearAll();
	}
	
	/*
	 * add to respective lists
	 */

	private void addFloatToList(ObservableList<HBox> list, int taskNum) {
		final int overdueTodayTomorrowOthersSize = tasksOverdue.size() + tasksToday.size() + tasksTomorrow.size() + tasksOthers.size();
		
		if(taskNum == overdueTodayTomorrowOthersSize + 1) {
			updateFloatCount(taskNum);
		}
		list.add(tasksFloat.get(taskNum-overdueTodayTomorrowOthersSize-1));
	}
	
	private void addOthersToList(ObservableList<HBox> list, int taskNum) {
		final int overdueTodayTomorrowSize = tasksOverdue.size() + tasksToday.size() + tasksTomorrow.size();
		
		if(taskNum == overdueTodayTomorrowSize + 1) {
			updateOthersCount(taskNum);
		}
		list.add(tasksOthers.get(taskNum- overdueTodayTomorrowSize-1));
	}

	private void addTomorrowToList(ObservableList<HBox> list, int taskNum) {
		final int overdueTodaySize = tasksOverdue.size() + tasksToday.size();
		
		if(taskNum == overdueTodaySize + 1) {
			updateTomorrowCount(taskNum);
		}
		list.add(tasksTomorrow.get(taskNum-overdueTodaySize-1));
	}
	
	private void addTodayToList(ObservableList<HBox> list, int taskNum) {
		final int overdueSize = tasksOverdue.size();
		
		if(taskNum == overdueSize + 1) {
			updateTodayCount(taskNum);
		}
		list.add(tasksToday.get(taskNum-overdueSize-1));
	}
	
	private void addOverdueToList(ObservableList<HBox> list, int taskNum) {
		if(taskNum == 1) {
			updateOverdueCount(taskNum);
		}
		list.add(tasksOverdue.get(taskNum-1));
	}
	
	private void updateFloatCount(int taskNum) {
		final int overdueTodayTomorrowOthersSize = tasksOverdue.size() + tasksToday.size() + tasksTomorrow.size() + tasksOthers.size();
		
		Label taskFloatNum = new Label(FILTER_PLACEHOLDER + (tasksFloat.size()-1));
		taskFloatNum.setId(NUM_ID);
		
		if(tasksFloat.get(taskNum-overdueTodayTomorrowOthersSize-1).getChildren().size() == 2) {
			tasksFloat.get(taskNum-overdueTodayTomorrowOthersSize-1).getChildren().set(1, taskFloatNum);
		} else {
			tasksFloat.get(taskNum-overdueTodayTomorrowOthersSize-1).getChildren().add(taskFloatNum);
		}
	}

	/*
	 * update count of respective category
	 */
	
	private void updateOthersCount(int taskNum) {
		final int overdueTodayTomorrowSize = tasksOverdue.size() + tasksToday.size() + tasksTomorrow.size();
		
		Label taskOthersNum = new Label(FILTER_PLACEHOLDER + (tasksOthers.size()-1));
		taskOthersNum.setId(NUM_ID);
		
		if(tasksOthers.get(taskNum-overdueTodayTomorrowSize-1).getChildren().size() == 2) {
			tasksOthers.get(taskNum-overdueTodayTomorrowSize-1).getChildren().set(1, taskOthersNum);
		} else {
			tasksOthers.get(taskNum-overdueTodayTomorrowSize-1).getChildren().add(taskOthersNum);
		}
	}

	private void updateTomorrowCount(int taskNum) {
		final int overdueTodaySize = tasksOverdue.size() + tasksToday.size();
		
		Label taskTomorrowNum = new Label(FILTER_PLACEHOLDER + (tasksTomorrow.size()-1));
		taskTomorrowNum.setId(NUM_ID);
		if(tasksTomorrow.get(taskNum-overdueTodaySize-1).getChildren().size() == 2) {
			tasksTomorrow.get(taskNum-overdueTodaySize-1).getChildren().set(1, taskTomorrowNum);
		} else {
			tasksTomorrow.get(taskNum-overdueTodaySize-1).getChildren().add(taskTomorrowNum);
		}
	}

	private void updateTodayCount(int taskNum) {
		final int overdueSize = tasksOverdue.size();
		
		Label taskTodayNum = new Label(FILTER_PLACEHOLDER + (tasksToday.size()-1));
		taskTodayNum.setId(NUM_ID);
		if(tasksToday.get(taskNum-overdueSize-1).getChildren().size() == 2) {
			tasksToday.get(taskNum-overdueSize-1).getChildren().set(1, taskTodayNum);
		} else {
			tasksToday.get(taskNum-overdueSize-1).getChildren().add(taskTodayNum);
		}
	}

	private void updateOverdueCount(int taskNum) {
		Label taskOverdueNum = new Label(FILTER_PLACEHOLDER + (tasksOverdue.size()-1));
		taskOverdueNum.setId(NUM_ID);
		if(tasksOverdue.get(taskNum-1).getChildren().size() == 2) {
			tasksOverdue.get(taskNum-1).getChildren().set(1, taskOverdueNum);
		} else {
			tasksOverdue.get(taskNum-1).getChildren().add(taskOverdueNum);
		}
	}

	public void sortTasksByClasses(Task taskObj, HBox taskRow) {
		LocalDateTime localDate = new LocalDateTime();
		String dateToday = localDate.toString(DATE_FORMAT);
		String dateTomorrow = localDate.plusDays(1).toString(DATE_FORMAT);
		
		LocalDateTime taskDate = new LocalDateTime();
		LocalTime taskTime = new LocalTime();
		LocalTime timeNow = new LocalTime();
		
		if(taskObj.get_date() != "") {
			taskDate = LocalDateTime.parse(taskObj.get_date(), DateTimeFormat.forPattern(DATE_FORMAT));
			if(taskObj.get_time() != "") {
				taskTime = LocalDateTime.parse(taskObj.get_time(), DateTimeFormat.forPattern(TIME_FORMAT)).toLocalTime();
			}
		}
		
		if(taskDate.plusDays(1).isBefore(localDate) || 
				(taskObj.get_date().equals(dateToday) && taskTime.isBefore(timeNow))) {
			if(!tasksOverdue.contains(taskClassOverdue)) {
				tasksOverdue.add(taskClassOverdue);
			}
			taskRow.setId(OVERDUE_TASK_ID);
			tasksOverdue.add(taskRow);
		}
		else if(taskObj.get_date().equals(dateToday) 
				|| (!taskObj.get_time().isEmpty() && taskObj.get_date().isEmpty())) {
			if(!tasksToday.contains(taskClassToday)) {
				tasksToday.add(taskClassToday);	
			}
			taskRow.setId(TODAY_TASK_ID);
			tasksToday.add(taskRow);
		} else if(taskObj.get_date().equals(dateTomorrow)) {
			if(!tasksTomorrow.contains(taskClassTomorrow)) {
				tasksTomorrow.add(taskClassTomorrow);
			}
			taskRow.setId(TOMORROW_TASK_ID);
			tasksTomorrow.add(taskRow);
		} else if(taskObj.get_time().isEmpty() && taskObj.get_date().isEmpty()) {
			if(!tasksFloat.contains(taskClassFloat)) {
				tasksFloat.add(taskClassFloat);
			}
			taskRow.setId(FLOAT_TASK_ID);
			tasksFloat.add(taskRow);
		} else {
			if(!tasksOthers.contains(taskClassOthers)) {
				tasksOthers.add(taskClassOthers);
			}
			taskRow.setId(OTHERS_TASK_ID);
			tasksOthers.add(taskRow);
		}
	}
}
