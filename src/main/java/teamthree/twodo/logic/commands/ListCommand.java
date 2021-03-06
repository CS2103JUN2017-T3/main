package teamthree.twodo.logic.commands;

import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_DEADLINE_END;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_DEADLINE_START;
import static teamthree.twodo.logic.parser.CliSyntax.PREFIX_TAG;

import java.util.Set;

import teamthree.twodo.model.tag.Tag;
import teamthree.twodo.model.task.Deadline;

//@@author A0107433N
// Lists all tasks in the Tasklist to the user
public class ListCommand extends Command {

    public static final String COMMAND_WORD = "list";
    public static final String COMMAND_WORD_FAST = "l";
    public static final String COMMAND_WORD_HISTORY = "/h";
    public static final String COMMAND_WORD_FLOATING = "/f";

    public static final String MESSAGE_SUCCESS_INCOMPLETE = "Listed all incomplete tasks";
    public static final String MESSAGE_SUCCESS_INCOMPLETE_FLOATING = "Listed all incomplete floating tasks";
    public static final String MESSAGE_SUCCESS_COMPLETE = "Listed all completed tasks";
    public static final String MESSAGE_SUCCESS_COMPLETE_FLOATING = "Listed all complete floating tasks";
    public static final String MESSAGE_SUCCESS_INCOMPLETE_START = "Listed all incomplete tasks after %1$s";
    public static final String MESSAGE_SUCCESS_COMPLETE_START = "Listed all completed tasks after %1$s";
    public static final String MESSAGE_SUCCESS_INCOMPLETE_END = "Listed all incomplete tasks before %1$s";
    public static final String MESSAGE_SUCCESS_COMPLETE_END = "Listed all completed tasks before %1$s";
    public static final String MESSAGE_SUCCESS_INCOMPLETE_BOTH = "Listed all incomplete tasks between %1$s and %1$s";
    public static final String MESSAGE_SUCCESS_COMPLETE_BOTH = "Listed all completed tasks between %1$s and %1$s";
    public static final String MESSAGE_SUCCESS_INCOMPLETE_TAG = "Listed all incompleted tasks with specified tag(s)";
    public static final String MESSAGE_SUCCESS_COMPLETE_TAG = "Listed all completed tasks with specified tag(s)";
    public static final String MESSAGE_EMPTY_LIST = "No tasks to show\n"
            + "Try adding new tasks or changing the listing criteria";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Lists all incomplete tasks within specified period.\n"
            + "If only start date specified, list all tasks after start date. "
            + "If only end date specified, list all tasks before end date.\n"
            + "If tags are included, only tasks with corresponding tags will be listed\n"
            + "Add " + COMMAND_WORD_HISTORY
            + " to list completed tasks instead of incomplete tasks within specified period.\n"
            + "Add " + COMMAND_WORD_FLOATING + " to list floating tasks instead of tasks with deadlines.\n"
            + "Parameters: [" + COMMAND_WORD_HISTORY + "] " + PREFIX_DEADLINE_START + "[START] "
            + PREFIX_DEADLINE_END + "[END] " + PREFIX_TAG + "[TAG1, TAG2,...]\n"
            + "Example: " + COMMAND_WORD + " " + COMMAND_WORD_HISTORY + " " + PREFIX_DEADLINE_START + "today "
            + PREFIX_DEADLINE_END + "next week" + PREFIX_TAG + "personal";

    private Deadline deadline;
    public enum AttributeInputted { NONE, START, END, BOTH };

    private AttributeInputted attInput;
    private boolean listIncomplete;
    private Set<Tag> tagList;
    private boolean listFloating;

    public ListCommand(Deadline deadline, AttributeInputted attInput,
            boolean listIncomplete, boolean listFloating, Set<Tag> tagList) {
        this.deadline = deadline;
        this.attInput = attInput;
        this.listIncomplete = listIncomplete;
        this.tagList = tagList;
        this.listFloating = listFloating;
    }

    @Override
    public CommandResult execute() {
        if (attInput.equals(AttributeInputted.NONE)) {
            model.updateFilteredTaskListToShowAll(tagList, listFloating, listIncomplete);
        } else {
            model.updateFilteredTaskListToShowPeriod(deadline, attInput, listIncomplete, tagList);
        }
        return messageChooser();
    }

    //Chooses appropriate message based on user input
    private CommandResult messageChooser() {
        if (model.getFilteredAndSortedTaskList().size() == 0) {
            return new CommandResult(MESSAGE_EMPTY_LIST);
        }
        if (attInput.equals(AttributeInputted.NONE)) {
            if (listIncomplete) {
                if (tagListNotEmpty()) {
                    return new CommandResult(MESSAGE_SUCCESS_INCOMPLETE_TAG);
                } else if (listFloating) {
                    return new CommandResult(MESSAGE_SUCCESS_INCOMPLETE_FLOATING);
                } else {
                    return new CommandResult(MESSAGE_SUCCESS_INCOMPLETE);
                }
            } else {
                if (tagListNotEmpty()) {
                    return new CommandResult(MESSAGE_SUCCESS_COMPLETE_TAG);
                } else if (listFloating) {
                    return new CommandResult(MESSAGE_SUCCESS_COMPLETE_FLOATING);
                } else {
                    return new CommandResult(MESSAGE_SUCCESS_COMPLETE);
                }
            }
        } else {
            String message;
            switch (attInput) {
            case START:
                if (listIncomplete) {
                    message = String.format(MESSAGE_SUCCESS_INCOMPLETE_START, deadline.getStartDate());
                } else {
                    message = String.format(MESSAGE_SUCCESS_COMPLETE_START, deadline.getStartDate());
                }
                break;
            case END:
                if (listIncomplete) {
                    message = String.format(MESSAGE_SUCCESS_INCOMPLETE_END, deadline.getEndDate());
                } else {
                    message = String.format(MESSAGE_SUCCESS_COMPLETE_END, deadline.getEndDate());
                }
                break;
            case BOTH:
                if (listIncomplete) {
                    message = String.format(MESSAGE_SUCCESS_INCOMPLETE_BOTH, deadline.getStartDate(),
                            deadline.getEndDate());
                } else {
                    message = String.format(MESSAGE_SUCCESS_COMPLETE_BOTH, deadline.getStartDate(),
                            deadline.getEndDate());
                }
                break;
            default:
                if (listIncomplete) {
                    message = MESSAGE_SUCCESS_INCOMPLETE;
                } else {
                    message = MESSAGE_SUCCESS_COMPLETE;
                }
                break;
            }
            return new CommandResult(message);
        }
    }
    @Override
    public String toString() {
        Boolean incomplete = listIncomplete;
        Boolean floating = listFloating;
        return attInput.name() + incomplete + tagList + floating;
    }

    @Override
    public boolean equals(Object other) {
        // short circuit if same object
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ListCommand)) {
            return false;
        }

        // state check
        return this.toString().equals(other.toString());
    }

    private boolean tagListNotEmpty() {
        return tagList != null && !tagList.isEmpty();
    }
}
