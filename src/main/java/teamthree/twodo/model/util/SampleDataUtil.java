package teamthree.twodo.model.util;

import java.util.HashSet;
import java.util.Set;

import teamthree.twodo.commons.exceptions.IllegalValueException;
import teamthree.twodo.model.ReadOnlyTaskList;
import teamthree.twodo.model.TaskList;
import teamthree.twodo.model.tag.Tag;
import teamthree.twodo.model.task.Deadline;
import teamthree.twodo.model.task.Description;
import teamthree.twodo.model.task.Name;
import teamthree.twodo.model.task.Task;
import teamthree.twodo.model.task.TaskWithDeadline;
import teamthree.twodo.model.task.exceptions.DuplicateTaskException;

public class SampleDataUtil {
    public static Task[] getSampleTasks() {
        try {
            Task firstTask = new Task(new Name("CS2103 Tutorial"),
                    new Description("Software Engineering"), getTagSet("NUS"), false);
            TaskWithDeadline secondTask = new TaskWithDeadline(new Name("CS2103 Project"),
                    new Deadline("next Monday 10am", "next Monday 10am", Deadline.NULL_VALUE),
                    new Description("Final Submission V0.5rc"), getTagSet("NUS"), false);
            TaskWithDeadline thirdTask = new TaskWithDeadline(new Name("Dinner"),
                    new Deadline("fri 7pm", "fri 10pm", Deadline.NULL_VALUE),
                    new Description(" Ang Mo Kio"), getTagSet("Friends", "coursemates"), false);
            TaskWithDeadline fourthTask = new TaskWithDeadline(new Name("Shopping"),
                    new Deadline("next month 2pm", "next month 2pm", Deadline.NULL_VALUE),
                    new Description("New Clothes"), getTagSet("family", "friends"), false);
            Task fifthTask = new Task(new Name("cca Meeting"), new Description("NUS Tues 3pm"),
                    getTagSet("cca"), false);
            Task sixthTask = new Task(new Name("BuyLotion"), new Description("NTUC"), getTagSet(), false);
            Task[] sampleTaskList = new Task[] { firstTask, secondTask, thirdTask, fourthTask, fifthTask, sixthTask };
            return sampleTaskList;
        } catch (IllegalValueException e) {
            throw new AssertionError("sample data cannot be invalid", e);
        }
    }

    public static ReadOnlyTaskList getSampleTaskList() {
        try {
            TaskList sampleAb = new TaskList();
            for (Task sampleTask : getSampleTasks()) {
                sampleAb.addTask(sampleTask);
            }
            return sampleAb;
        } catch (DuplicateTaskException e) {
            throw new AssertionError("sample data cannot contain duplicate tasks", e);
        }
    }

    /**
     * Returns a tag set containing the list of strings given.
     */
    public static Set<Tag> getTagSet(String... strings) throws IllegalValueException {
        HashSet<Tag> tags = new HashSet<>();
        for (String s : strings) {
            tags.add(new Tag(s));
        }

        return tags;
    }

}
