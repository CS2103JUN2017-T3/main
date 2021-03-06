package teamthree.twodo.logic.commands;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;

import teamthree.twodo.commons.exceptions.IllegalValueException;
import teamthree.twodo.logic.CommandHistory;
import teamthree.twodo.logic.UndoCommandHistory;
import teamthree.twodo.logic.commands.exceptions.CommandException;
import teamthree.twodo.model.Model;
import teamthree.twodo.model.ModelManager;
import teamthree.twodo.model.UserPrefs;
import teamthree.twodo.model.tag.Tag;
import teamthree.twodo.model.task.Task;
import teamthree.twodo.testutil.TypicalTask;

//@@author A0107433N
public class FindCommandTest {

    private Model model;
    private Model expectedModel;

    @Before
    public void setUp() throws IllegalValueException {
        model = new ModelManager(new TypicalTask().getTypicalTaskList(), new UserPrefs());
        expectedModel = new ModelManager(model.getTaskList(), new UserPrefs());
    }

    @Test
    public void executeFindCorrectIncompleteTaskByName() throws Exception {
        boolean listIncomplete = true;
        FindCommand findCommand = new FindCommand(new HashSet<>(Arrays.asList(
                new TypicalTask().cs2103.getName().fullName.split("\\s+"))), listIncomplete);
        findCommand.setData(model, new CommandHistory(), new UndoCommandHistory());
        Set<String> keyWords = new HashSet<>(Arrays.asList(new TypicalTask().cs2103.getName().fullName.split("\\s+")));

        expectedModel.updateFilteredTaskListByKeywords(keyWords, listIncomplete);
        assertCommandSuccess(findCommand, model, String.format(FindCommand.MESSAGE_SUCCESS_INCOMPLETE,
                expectedModel.getFilteredAndSortedTaskList().size()) , expectedModel);
    }

    @Test
    public void executeFindCorrectIncompleteTaskByTag() throws Exception {
        boolean listIncomplete = true;
        Set<Tag> testTagSet = new TypicalTask().cs2103.getTags();
        Set<String> testKeywordSet = new HashSet<>();
        Iterator<Tag> tagIterator = testTagSet.iterator();
        while (tagIterator.hasNext()) {
            Tag tag = tagIterator.next();
            testKeywordSet.add(tag.tagName);
        }
        FindCommand findCommand = new FindCommand(testKeywordSet, listIncomplete);
        findCommand.setData(model, new CommandHistory(), new UndoCommandHistory());

        expectedModel.updateFilteredTaskListByKeywords(testKeywordSet, listIncomplete);
        assertCommandSuccess(findCommand, model, String.format(FindCommand.MESSAGE_SUCCESS_INCOMPLETE,
                expectedModel.getFilteredAndSortedTaskList().size()) , expectedModel);
    }

    @Test
    public void executeFindCorrectIncompleteTaskByDescription() throws Exception {
        boolean listIncomplete = true;
        FindCommand findCommand = new FindCommand(new HashSet<>(Arrays.asList(
                new TypicalTask().cs2103.getDescription().value.split("\\s+"))), listIncomplete);
        findCommand.setData(model, new CommandHistory(), new UndoCommandHistory());
        Set<String> keyWords = new HashSet<>(Arrays.asList(
                new TypicalTask().cs2103.getDescription().value.split("\\s+")));

        expectedModel.updateFilteredTaskListByKeywords(keyWords, listIncomplete);
        assertCommandSuccess(findCommand, model, String.format(FindCommand.MESSAGE_SUCCESS_INCOMPLETE,
                expectedModel.getFilteredAndSortedTaskList().size()) , expectedModel);
    }

    @Test
    public void executeFindCorrectCompleteTask() throws Exception {
        boolean listIncomplete = false;
        Task typicalCompletedTask = new TypicalTask().partyCompleted;
        FindCommand findCommand = new FindCommand(new HashSet<>(Arrays.asList(
                typicalCompletedTask.getName().fullName.split("\\s+"))), listIncomplete);
        findCommand.setData(model, new CommandHistory(), new UndoCommandHistory());
        Set<String> keyWords = new HashSet<>(Arrays.asList(
                typicalCompletedTask.getName().fullName.split("\\s+")));

        expectedModel.updateFilteredTaskListByKeywords(keyWords, listIncomplete);
        assertCommandSuccess(findCommand, model, String.format(FindCommand.MESSAGE_SUCCESS_COMPLETE,
                expectedModel.getFilteredAndSortedTaskList().size()) , expectedModel);
    }

    @Test
    public void executeFindNonExistentTask() throws Exception {
        boolean listIncomplete = true;
        FindCommand findCommand = new FindCommand(new HashSet<>(Arrays.asList(
                new TypicalTask().supermarket.getName().fullName.split("\\s+"))), listIncomplete);
        findCommand.setData(model, new CommandHistory(), new UndoCommandHistory());
        Set<String> keyWords = new HashSet<>(Arrays.asList(
                new TypicalTask().supermarket.getName().fullName.split("\\s+")));

        expectedModel.updateFilteredTaskListByKeywords(keyWords, listIncomplete);
        assertCommandSuccess(findCommand, model, FindCommand.MESSAGE_EMPTY_LIST , expectedModel);
    }

    /**
     * Executes the given {@code command}, confirms that <br>
     * - the result message matches {@code expectedMessage} <br>
     * - the address book and the filtered person list in the {@code model} matches that of {@code expectedModel}
     */

    public static void assertCommandSuccess(Command command, Model model, String expectedMessage, Model expectedModel)
            throws CommandException {
        CommandResult result = command.execute();
        assertEquals(expectedMessage, result.feedbackToUser);
        assertEquals(expectedModel, model);
    }

    @Test
    public void equalsReturnsTrueSuccessfully() {
        Set<String> keyWords = new HashSet<>(Arrays.asList(new TypicalTask().cs2103.getName().fullName.split("\\s+")));
        FindCommand command = new FindCommand(keyWords, true);
        assertTrue(command.equals(command));
        FindCommand other = new FindCommand(keyWords, true);
        assertTrue(command.equals(other));
    }
}
