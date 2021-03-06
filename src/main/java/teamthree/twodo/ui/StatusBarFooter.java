package teamthree.twodo.ui;

import java.time.Clock;
import java.util.Date;
import java.util.logging.Logger;

import org.controlsfx.control.StatusBar;

import com.google.common.eventbus.Subscribe;

import javafx.fxml.FXML;
import javafx.scene.layout.Region;
import teamthree.twodo.commons.core.LogsCenter;
import teamthree.twodo.commons.events.model.TaskListChangedEvent;
import teamthree.twodo.commons.events.storage.TaskListStorageChangedEvent;

// A ui for the status bar that is displayed at the footer of the application.
public class StatusBarFooter extends UiPart<Region> {

    public static final String SYNC_STATUS_INITIAL = "Not updated yet in this session";
    public static final String SYNC_STATUS_UPDATED = "Last Updated: %s";

    /**
     * Used to generate time stamps.
     *
     */
    private static Clock clock = Clock.systemDefaultZone();

    private static final Logger logger = LogsCenter.getLogger(StatusBarFooter.class);

    private static final String FXML = "StatusBarFooter.fxml";

    @FXML
    private StatusBar syncStatus;
    @FXML
    private StatusBar saveLocationStatus;

    public StatusBarFooter(String saveLocation) {
        super(FXML);
        setSyncStatus(SYNC_STATUS_INITIAL);
        setSaveLocation("./" + saveLocation);
        registerAsAnEventHandler(this);
    }

    // Sets the clock used to determine the current time.
    public static void setClock(Clock clock) {
        StatusBarFooter.clock = clock;
    }

    // Returns the clock currently in use.
    public static Clock getClock() {
        return clock;
    }

    private void setSaveLocation(String location) {
        this.saveLocationStatus.setText(location);
    }

    private void setSyncStatus(String status) {
        this.syncStatus.setText(status);
    }

    @Subscribe
    public void handleTaskListChangedEvent(TaskListChangedEvent abce) {
        long now = clock.millis();
        String lastUpdated = new Date(now).toString();
        logger.info(LogsCenter.getEventHandlingLogMessage(abce, "Setting last updated status to " + lastUpdated));
        setSyncStatus(String.format(SYNC_STATUS_UPDATED, lastUpdated));
    }

    //@@author A0162253M
    @Subscribe
    public void handleTaskListStorageChangedEvent(TaskListStorageChangedEvent e) {
        logger.info(LogsCenter.getEventHandlingLogMessage(e, "Task List saved to " + e.filePath));
        setSaveLocation(e.filePath);
    }
}
