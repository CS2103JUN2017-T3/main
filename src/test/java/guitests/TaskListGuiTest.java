
package guitests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.concurrent.TimeoutException;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.rules.TestName;
import org.testfx.api.FxToolkit;

import guitests.guihandles.BrowserPanelHandle;
import guitests.guihandles.CategoryListPanelHandle;
import guitests.guihandles.CommandBoxHandle;
import guitests.guihandles.MainMenuHandle;
import guitests.guihandles.MainWindowHandle;
import guitests.guihandles.ResultDisplayHandle;
import guitests.guihandles.StatusBarFooterHandle;
import guitests.guihandles.TaskCardHandle;
import guitests.guihandles.TaskListPanelHandle;
import javafx.application.Platform;
import javafx.stage.Stage;
import teamthree.twodo.TestApp;
import teamthree.twodo.commons.core.EventsCenter;
import teamthree.twodo.commons.events.BaseEvent;
import teamthree.twodo.logic.commands.ListCommand;
import teamthree.twodo.model.TaskList;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.testutil.TestUtil;
import teamthree.twodo.testutil.TypicalTask;

/**
 * A GUI Test class for TaskList.
 */
public abstract class TaskListGuiTest {

    /**
     * The TestName Rule makes the current test name available inside test
     * methods
     */

    @Rule

    public TestName name = new TestName();

    protected TypicalTask td = new TypicalTask(TypicalTask.TaskType.FLOATING);

    /**
     * Handles to GUI elements present at the start up are created in advance
     * for easy access from child classes.
     */

    protected MainWindowHandle mainGui;
    protected MainMenuHandle mainMenu;
    protected TaskListPanelHandle taskListPanel;
    protected CategoryListPanelHandle categoryListPanel;
    protected ResultDisplayHandle resultDisplay;
    protected CommandBoxHandle commandBox;
    protected BrowserPanelHandle browserPanel;
    protected StatusBarFooterHandle statusBarFooter;

    protected Stage stage;
    protected final String clear = "clear";
    protected final String listFloating = ListCommand.COMMAND_WORD + " " + ListCommand.COMMAND_WORD_FLOATING;

    @BeforeClass
    public static void setupSpec() {
        try {
            FxToolkit.registerPrimaryStage();
            FxToolkit.hideStage();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }

    @Before
    public void setup() throws Exception {
        FxToolkit.setupStage((stage) -> {
            mainGui = new MainWindowHandle(new GuiRobot(), stage);
            mainMenu = mainGui.getMainMenu();
            taskListPanel = mainGui.getTaskListPanel();
            categoryListPanel = mainGui.getCategoryListPanel();
            resultDisplay = mainGui.getResultDisplay();
            commandBox = mainGui.getCommandBox();
            statusBarFooter = mainGui.getStatusBarFooter();
            this.stage = stage;
        });
        EventsCenter.clearSubscribers();
        FxToolkit.setupApplication(() -> new TestApp(this::getInitialData, getDataFileLocation()));
        FxToolkit.showStage();
        while (!stage.isShowing())
            ;
        mainGui.focusOnMainApp();
        mainGui.pressEnter();
    }

    /**
     * Override this in child classes to set the initial local data. Return null
     * to use the data in the file specified in {@link #getDataFileLocation()}
     */

    protected TaskList getInitialData() {
        TaskList tl = new TaskList();
        TypicalTask typicalTask = new TypicalTask();
        typicalTask.loadTaskListWithSampleData(tl);
        return tl;
    }

    /**
     * Override this in child classes to set the data file location.
     */

    protected String getDataFileLocation() {
        return TestApp.SAVE_LOCATION_FOR_TESTING;
    }

    @After
    public void cleanup() throws TimeoutException {
        FxToolkit.cleanupStages();
    }

    /**
     * Asserts the task shown in the card is same as the given task
     */
    public void assertMatching(ReadOnlyTask task, TaskCardHandle card) {
        assertTrue(TestUtil.compareCardAndTask(card, task));
    }

    /**
     * Asserts the size of the task list is equal to the given number.
     */

    protected void assertListSize(int size) {
        int numberOfPeople = taskListPanel.getNumberOfTasks();
        assertEquals(size, numberOfPeople);
    }

    /**
     * Asserts the message shown in the Result Display area is same as the given
     * string.
     */
    protected void assertResultMessage(String expected) {
        assertEquals(expected, resultDisplay.getText());
    }

    public void raise(BaseEvent e) {
        /*
         * JUnit doesn't run its test cases on the UI thread. Platform.runLater
         * is used to post event on the UI thread.
         */
        Platform.runLater(() -> EventsCenter.getInstance().post(e));
    }
}
