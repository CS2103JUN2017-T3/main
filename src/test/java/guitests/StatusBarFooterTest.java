package guitests;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static teamthree.twodo.ui.StatusBarFooter.SYNC_STATUS_INITIAL;
import static teamthree.twodo.ui.StatusBarFooter.SYNC_STATUS_UPDATED;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;
import java.util.Date;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import teamthree.twodo.logic.commands.ListCommand;
import teamthree.twodo.testutil.TaskUtil;
import teamthree.twodo.ui.StatusBarFooter;

public class StatusBarFooterTest extends TaskListGuiTest {

    private Clock originalClock;
    private Clock injectedClock;

    @Before
    public void injectFixedClock() {
        originalClock = StatusBarFooter.getClock();
        injectedClock = Clock.fixed(Instant.now(), ZoneId.systemDefault());
        StatusBarFooter.setClock(injectedClock);
    }

    @After
    public void restoreOriginalClock() {
        StatusBarFooter.setClock(originalClock);
    }

    @Test
    public void syncStatusInitialValue() {
        assertEquals(SYNC_STATUS_INITIAL, statusBarFooter.getSyncStatus());
    }

    @Test
    public void syncStatusMutatingCommandSucceedSyncStatusUpdated() {
        String timestamp = new Date(injectedClock.millis()).toString();
        String expected = String.format(SYNC_STATUS_UPDATED, timestamp);
        assertTrue(commandBox.runCommand(TaskUtil.getAddCommand(td.ida))); // mutating command succeeds
        assertEquals(expected, statusBarFooter.getSyncStatus());
    }

    @Test
    public void syncStatusNonMutatingCommandSucceedsSyncStatusRemainsUnchanged() {
        assertTrue(commandBox.runCommand(ListCommand.COMMAND_WORD)); // non-mutating command succeeds
        assertEquals(SYNC_STATUS_INITIAL, statusBarFooter.getSyncStatus());
    }

    @Test
    public void syncStatusCommandFailsSyncStatusRemainsUnchanged() {
        assertFalse(commandBox.runCommand("invalid command")); // invalid command fails
        assertEquals(SYNC_STATUS_INITIAL, statusBarFooter.getSyncStatus());
    }

}

