package teamthree.twodo.alarm;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import com.google.common.eventbus.Subscribe;

import teamthree.twodo.commons.core.ComponentManager;
import teamthree.twodo.commons.events.alarm.DeadlineNotificationTimeReachedEvent;
import teamthree.twodo.commons.events.model.TaskListChangedEvent;
import teamthree.twodo.model.Model;
import teamthree.twodo.model.task.Deadline;
import teamthree.twodo.model.task.ReadOnlyTask;
import teamthree.twodo.model.task.TaskWithDeadline;

//@@author A0124399W
// Alarm class to give reminders for tasks with deadline.
public class AlarmManager extends ComponentManager {
    // List of tasks yet to be notified
    private final List<ReadOnlyTask> notificationList = new ArrayList<ReadOnlyTask>();
    // Keeps track of tasks that have been notified
    private final HashSet<ReadOnlyTask> notified = new HashSet<ReadOnlyTask>();

    private final Model model;
    // In charge of scheduling and launching reminders
    private final Timer masterClock = new Timer();
    // Notification time of the next most recent activity
    private Date nextReminderTime;

    public AlarmManager(Model model) {
        this.model = model;
        syncWithMasterTaskList(model.getTaskList().getTaskList());
    }
    /**
     * Synchronizes internal notification list with the main TaskList
     *
     * @param masterList Full list of tasks from the taskList
     */
    private synchronized void syncWithMasterTaskList(List<ReadOnlyTask> masterList) {
        if (masterList == null || masterList.isEmpty()) {
            return;
        }
        // Clear list first to avoid duplicates
        notificationList.clear();
        // Adds tasks which are not in the notified set
        masterList.forEach((t) -> {
            if (t instanceof TaskWithDeadline && !t.isCompleted() && !notified.contains(t)) {
                notificationList.add(t);
            }
        });
        sortNotificationsByDeadline();
        updateNextReminder();
        startTimerTask();
    }

    public void startTimerTask() {

        if (nextReminderTime == null) {
            return;
        }
        masterClock.schedule(new NextReminder(), nextReminderTime);

    }

    private Date getNotificationTime(ReadOnlyTask task) {
        return task.getDeadline().get().getNotificationDate();
    }

    // =====================HELPER CLASS==========================

    private class NextReminder extends TimerTask {

        /**
         * The following command will be run upon reaching the scheduled timing.
         * It will raise a DeadlineNotificationTimeReachedEvent with all the
         * tasks that have reached the notification deadline.
         *
         * After that it will update internal information.
         */
        @Override
        public void run() {
            List<ReadOnlyTask> tasksToRemindOf = new ArrayList<ReadOnlyTask>();
            Date currentDate = new Date();
            notificationList.forEach((t) -> {
                if (getNotificationTime(t).before(currentDate) || getNotificationTime(t).equals(nextReminderTime)) {
                    tasksToRemindOf.add(t);
                }
            });
            if (tasksToRemindOf.size() > 0) {
                raise(new DeadlineNotificationTimeReachedEvent(tasksToRemindOf));
            }

            updateInternalData(tasksToRemindOf);

            startTimerTask();
        }

    }

    // =========================HELPER METHODS=================================

    /**
     * Transfers the most recently reminded tasks from the notification list to
     * the notified set. Updates the nextReminderTime with the notificationDate
     * of the next activity on the notification list. Called only after a
     * DeadlineNotificationTimeReachedEvent.
     *
     * @param notifiedTasks The tasks which were sent with the DeadlineNotificationTimeReachedEvent
     */

    private synchronized void updateInternalData(List<ReadOnlyTask> notifiedTasks) {
        notificationList.removeAll(notifiedTasks);
        notified.addAll(notifiedTasks);
        updateNextReminder();
    }

    // Sorts list by notification date
    private void sortNotificationsByDeadline() {
        notificationList.sort(new Comparator<ReadOnlyTask>() {

            @Override
            public int compare(ReadOnlyTask t, ReadOnlyTask u) {
                return getNotificationTime(t).compareTo(getNotificationTime(u));
            }

        });
    }

    // Updates nextReminderTime to the next one on the notificationList.
    private void updateNextReminder() {
        if (!notificationList.isEmpty()) {
            nextReminderTime = removeInvalidDates() ? null : getNotificationTime(notificationList.get(0));
        } else {
            nextReminderTime = null;
        }
    }

    /**
     * Transfers all invalid dates (i.e Default Dates) from notification list to
     * notified set. This avoids an invalid date exception from being thrown at
     * startTimerTask. Returns whether notificationList is empty after
     * operation.
     *
     * @return true if notificationList is empty after removing all invalid
     *         dates.
     */
    private boolean removeInvalidDates() {
        while (!getNotificationTime(notificationList.get(0)).after(Deadline.DEFAULT_DATE)) {
            notified.add(notificationList.remove(0));
        }
        return notificationList.isEmpty();
    }

    // =======================EVENT HANDLERS===========================

    // Synchronizes the notification list with the master list when there is a change
    @Subscribe
    public void handleTaskBookChangedEvent(TaskListChangedEvent event) {
        syncWithMasterTaskList(model.getTaskList().getTaskList());
    }

}
