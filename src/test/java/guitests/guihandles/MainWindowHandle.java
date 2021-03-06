package guitests.guihandles;

import guitests.GuiRobot;
import javafx.stage.Stage;
import teamthree.twodo.TestApp;

/**
 * Provides a handle for {@code MainWindow}.
 */
public class MainWindowHandle extends GuiHandle {

    private final TaskListPanelHandle taskListPanel;
    private final CategoryListPanelHandle categoryListPanel;
    private final ResultDisplayHandle resultDisplay;
    private final CommandBoxHandle commandBox;
    private final StatusBarFooterHandle statusBarFooter;
    private final MainMenuHandle mainMenu;
    //private final BrowserPanelHandle browserPanel;

    public MainWindowHandle(GuiRobot guiRobot, Stage primaryStage) {
        super(guiRobot, primaryStage, TestApp.APP_TITLE);

        taskListPanel = new TaskListPanelHandle(guiRobot, primaryStage);
        resultDisplay = new ResultDisplayHandle(guiRobot, primaryStage);
        commandBox = new CommandBoxHandle(guiRobot, primaryStage, TestApp.APP_TITLE);
        statusBarFooter = new StatusBarFooterHandle(guiRobot, primaryStage);
        mainMenu = new MainMenuHandle(guiRobot, primaryStage);
        //browserPanel = new BrowserPanelHandle(guiRobot, primaryStage);
        categoryListPanel = new CategoryListPanelHandle(guiRobot, primaryStage);
    }

    public TaskListPanelHandle getTaskListPanel() {
        return taskListPanel;
    }

    public ResultDisplayHandle getResultDisplay() {
        return resultDisplay;
    }

    public CommandBoxHandle getCommandBox() {
        return commandBox;
    }

    public StatusBarFooterHandle getStatusBarFooter() {
        return statusBarFooter;
    }

    public MainMenuHandle getMainMenu() {
        return mainMenu;
    }

    /*public BrowserPanelHandle getBrowserPanel() {
        return browserPanel;
    }*/

    public CategoryListPanelHandle getCategoryListPanel() {
        return categoryListPanel;
    }
}
