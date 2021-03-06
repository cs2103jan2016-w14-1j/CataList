//@@author A0112204E
package ui.Controllers;

import java.io.IOException;

import javafx.util.Duration;

import java.util.ArrayList;
import java.util.logging.Logger;

import javafx.animation.FadeTransition;
import javafx.fxml.FXML;
import javafx.scene.control.TextField;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.text.Text;
import shared.LogHandler;

public class CommandLineController {

	/**
	 * 	ConmmandLineController controls the to-do list
	 * 	Primarily functions to retrieve inputs on the command line (both keystrokes and hotkeys) 
	 *  and manipulates the command line
	 * 	It also auto-completes/-corrects user input, display feedback and set previous/recent user commands
	 * 	into the command line by calling other sub classes
	 * 
	 */
	
	private static final String TUTORIAL_OFF_STRING = "OFF";
	private static final String TUTORIAL_ON_STRING = "ON";
	private static final boolean TUTORIAL_ON = true;
	private static final boolean TUTORIAL_OFF = false;

	private static final String SPACE_REGEX = " ";
	private static final String INITIALIZE = "";

	private static final String COMMAND_UNDO = "undo";
	private static final String COMMAND_UNMARK = "unmark";
	private static final String COMMAND_SEARCH = "search";
	private static final String COMMAND_REDO = "redo";
	private static final String COMMAND_MARK = "mark";
	private static final String COMMAND_EDIT = "edit";
	private static final String COMMAND_DISPLAY = "display";
	private static final String COMMAND_DELETE = "delete";
	private static final String COMMAND_ADD = "add";
	private static final String COMMAND_SAVE = "save";

	private static final int START_INPUT_INDEX = 0;
	private static final int FIRST_TAB = 0;
	private static final int COMPLETE_TAB = 1;
	private static final int MIN_TASK_LIST_SIZE = 1;
	private static final int FEEDBACK_ANIMATION_DURATION = 200;
	
	private boolean tutorialToggle = TUTORIAL_ON;

	@FXML private Text feedbackMain;
	@FXML private Text feedbackHelp;
	@FXML public TextField userInput;
	
	private String command = INITIALIZE;
	private int index = START_INPUT_INDEX;
	private int tabToggle = COMPLETE_TAB;

	private ArrayList<String> inputArray;
	private Logger log = LogHandler.retrieveLog();

	private ColorRenderer backgroundColor;
	private SupportFeaturesHandler supportFeaturesHandler;
	private MainGUIController main;

	/**
	 * Constructor method
	 * @param mainController The primary controller linking this and the other controllers
	 */
	public void init(MainGUIController mainController) {
		main = mainController;
		inputArray = new ArrayList<String>();
		backgroundColor = new ColorRenderer();
		supportFeaturesHandler = new SupportFeaturesHandler(main);
		FeedbackGenerator.generateDefaultFeedback(feedbackMain);

		initCommandLineListener();
	}

	private void initCommandLineListener() {
		userInput.textProperty().addListener((observable, oldValue, newValue) -> {
			if(newValue.trim().isEmpty()) {
				FeedbackGenerator.clearFeedback(feedbackHelp);
			} else if(newValue.split(SPACE_REGEX)[START_INPUT_INDEX].equalsIgnoreCase(COMMAND_ADD)) {
				FeedbackGenerator.generateAddFeedback(feedbackHelp);
			} else if (newValue.split(SPACE_REGEX)[START_INPUT_INDEX].equalsIgnoreCase(COMMAND_DELETE)) {
				FeedbackGenerator.generateDeleteFeedback(feedbackHelp);
			} else if (newValue.split(SPACE_REGEX)[START_INPUT_INDEX].equalsIgnoreCase(COMMAND_EDIT)) {
				FeedbackGenerator.generateEditFeedback(feedbackHelp);
			} else if (newValue.split(SPACE_REGEX)[START_INPUT_INDEX].equalsIgnoreCase(COMMAND_MARK)) {
				FeedbackGenerator.generateMarkFeedback(feedbackHelp);
			} else if (newValue.split(SPACE_REGEX)[START_INPUT_INDEX].equalsIgnoreCase(COMMAND_UNMARK)) {
				FeedbackGenerator.generateUnmarkFeedback(feedbackHelp);
			} else if (newValue.split(SPACE_REGEX)[START_INPUT_INDEX].equalsIgnoreCase(COMMAND_SEARCH)) {
				FeedbackGenerator.generateSearchFeedback(feedbackHelp);
			} else if (newValue.split(SPACE_REGEX)[START_INPUT_INDEX].equalsIgnoreCase(COMMAND_REDO)) {
				if(inputArray.size() >= 2 && inputArray.get(inputArray.size()-1).equalsIgnoreCase(COMMAND_UNDO)) {
					FeedbackGenerator.generateRedoFeedback(feedbackHelp, inputArray.get(inputArray.size()-2));
				} else {
					FeedbackGenerator.generateInvalidHelpFeedback(feedbackHelp);
				}
			} else if (newValue.split(SPACE_REGEX)[START_INPUT_INDEX].equalsIgnoreCase(COMMAND_UNDO)) {
				if(inputArray.size() >= 1) {
					FeedbackGenerator.generateUndoFeedback(feedbackHelp, inputArray.get(inputArray.size()-1));
				} else {
					FeedbackGenerator.generateInvalidHelpFeedback(feedbackHelp);
				}
			} else if (newValue.split(SPACE_REGEX)[START_INPUT_INDEX].equalsIgnoreCase(COMMAND_DISPLAY)) {
				FeedbackGenerator.generateHelpFeedback(feedbackHelp);
			} else if (newValue.split(SPACE_REGEX)[START_INPUT_INDEX].equalsIgnoreCase(COMMAND_SAVE)) {
				FeedbackGenerator.generateHelpFeedback(feedbackHelp);
			} else {
				FeedbackGenerator.generateHelpFeedback(feedbackHelp);
				FeedbackGenerator.generateTipFeedback(feedbackMain);
			}

		});
	}

	@FXML 
	private void handleSubmitButtonAction(KeyEvent event) throws IOException {
		parseKeyEvent(event);
	}

	private void parseKeyEvent(KeyEvent event) throws IOException {
		if (event.getCode() == KeyCode.ENTER) {
			processUserInput(event);
		} else if(event.getCode() == KeyCode.UP) {
			getPreviousCommand();
		} else if(event.getCode() == KeyCode.DOWN) {
			getNextCommand();	
		} else if(event.getCode() == KeyCode.RIGHT) {
			if(event.isAltDown()) {
				changeBackgroundColorIncrementally();
			} else if(event.isShiftDown()) {
				updateTutorialToggle();
			} else {
				nextTutorialPage();
			}
		} else if(event.getCode() == KeyCode.LEFT) {
			if(event.isAltDown()) {
				changeBackgroundColorDecrementally();
			}
		} else if (event.getCode() == KeyCode.F1) {
			changeTabFocus();
		} else if(event.getCode() == KeyCode.SPACE) {
			AutoCompleteCommands.autoComplete(userInput, feedbackMain);
		}
	}

	private void changeBackgroundColorDecrementally() {
		backgroundColor.toggleColorMinus(main.getBackgroundPane());
	}

	private void changeBackgroundColorIncrementally() {
		backgroundColor.toggleColorPlus(main.getBackgroundPane());
	}

	private void processUserInput(KeyEvent event) throws IOException {
		event.consume();
		readUserInput();

		if(!supportFeaturesHandler.isSupportFeaturesLoaded(feedbackMain)) {
			main.refreshToDoList();
		}
	}

	private void changeTabFocus() {
		main.getTabPane().getSelectionModel().select(tabToggle);
		tabToggle++;
		if (tabToggle == main.getTabPane().getTabs().size()) {
			tabToggle = FIRST_TAB;
		}
	}

	private void nextTutorialPage() throws IOException {
		if(tutorialToggle == TUTORIAL_ON && main.getTaskList().isEmpty()) {
			main.refreshToDoList();
			main.startTutorialMode();
			tutorialToggle = TUTORIAL_OFF;
		}
	}
	
	/**
	 * Passes the user input to Logic
	 * @param input This is the user input
	 * @return String This is the feedback from Logic
	 */
	public String uiToLogic(String input) {
		return main.passInputToLogic(input);
	}

	private void readUserInput() {
		command = userInput.getText();
		log.info("User Input: " + command);
		
		loadFeedback();
		userInput.clear();
		inputArray.add(command);
		index++;
	}

	private void loadFeedback() {
		feedbackMain.setText(uiToLogic(command));
		log.info("Feedback from Logic: " + feedbackMain.getText());

		FadeTransition ft = new FadeTransition(Duration.millis(FEEDBACK_ANIMATION_DURATION), feedbackMain);
		ft.setFromValue(0);
		ft.setToValue(1);
		ft.play();
	}

	private void updateTutorialToggle() {
		if(main.getTaskList().size() == MIN_TASK_LIST_SIZE) {
			if(tutorialToggle) {
				tutorialToggle = TUTORIAL_OFF;
			} else {
				tutorialToggle = TUTORIAL_ON;
			}
			main.openMainPane();
		}
	}

	private void getNextCommand() {
		if(index >= 0 && index < inputArray.size()-1) {
			userInput.setText(inputArray.get(++index));
			if(index == inputArray.size()-1) {
				index++;
			}
		} else if(index >= inputArray.size()-1) {
			userInput.clear();
			index = inputArray.size();
		}
	}

	private void getPreviousCommand() {
		if(index > 0 && index <= inputArray.size()) {
			userInput.setText(inputArray.get(--index));
		}
	}
	
	/**
	 * Gets user input field from commandLineController
	 * @return TextField This is the user input field
	 */
	public TextField getCommandLine() {
		return userInput;
	}

	/**
	 * Gets main feedback from commandLineController
	 * @return Text main feedback
	 */
	public Text getMainFeedback() {
		return feedbackMain;
	}

	/**
	 * Gets secondary feedback from commandLineController
	 * @return Text secondary feedback
	 */
	public Text getHelpFeedback() {
		return feedbackHelp;
	}
	
	/**
	 * Gets tutorial flag in string from commandLineController
	 * @return String Flag for indication of whether tutorial is ON or OFF
	 */
	public String getTutorialToggle() {
		if(tutorialToggle) {
			return TUTORIAL_ON_STRING;
		} else {
			return TUTORIAL_OFF_STRING;
		}
	}
}