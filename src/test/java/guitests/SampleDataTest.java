package guitests;

import teamthree.twodo.model.TaskList;
import teamthree.twodo.testutil.TestUtil;

public class SampleDataTest extends TaskListGuiTest {
    @Override
    protected TaskList getInitialData() {
        // return null to force test app to load data from file only
        return null;
    }

    @Override
    protected String getDataFileLocation() {
        // return a non-existent file location to force test app to load sample data
        return TestUtil.getFilePathInSandboxFolder("SomeFileThatDoesNotExist1234567890.xml");
    }
}

