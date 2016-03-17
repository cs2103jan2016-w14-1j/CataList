package ui.Controllers;

import java.io.IOException;
import java.util.ArrayList;

import org.jdom2.JDOMException;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import logic.LogicMain;
import logic.Task;

public class MainGUIController {
	
    @FXML 
    public CommandLineController commandLineController;  
    @FXML 
    public ListInterfaceController todoListController;   
    @FXML 
    public TitleInterfaceController titleController;
    @FXML
    public SupportFeatureController supportFeatureController;
    @FXML 
    public AnchorPane mainAnchorPane;
    
    private LogicMain logic = new LogicMain();
    
    public void initialize() throws IOException, JDOMException {
       commandLineController.init(this);
       titleController.init(this);
       todoListController.init(this);
       supportFeatureController.init(this);
    }
    /*
    public void refreshClassList() {
    	classListController.loopClassList();
    }
    
    public void clearCompleted() {
    	classListController.clearCompletedClassList();
    }
    
    public void loadCompleted() {
    	classListController.initCompletedClassList();
    }
   */ 
    public void refreshToDoList() throws IOException, JDOMException {
    	todoListController.loopTaskList();
    }
    
    public void removeMainPane() {
    	supportFeatureController.removeMainPane();
    }
    
    public void openMainPane() {
    	supportFeatureController.showMainPane();
    }
    /*
    public boolean isClassEmpty() {
    	return classListController.getClasses().isEmpty();
    }
    */
    public boolean isToDoListEmpty() {
    	return todoListController.getTasks().isEmpty();
    }
    
    public boolean isCompletedEmpty() {
    	return todoListController.getCompleted().isEmpty();
    }
    
    public boolean isMainPaneManaged() {
    	return supportFeatureController.getMainPane().isManaged();
    }
    
    public LogicMain getLogic() {
    	return logic;
    }
    
    public String passInputToLogic(String input) {
    	return logic.processCommand(input);
    }
    
    public ArrayList<Task> refreshList() {
    	return logic.getOperatingTasksForUI();
    }
}