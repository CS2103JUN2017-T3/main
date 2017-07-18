package teamthree.twodo.commons.core.options;

import static teamthree.twodo.commons.util.CollectionUtil.requireAllNonNull;

//@@author A0139267W
//Represents an auto-mark setting for tasks
public class AutoMark {
    public static final String EMPTY_AUTOMARK = " ";

    public final String autoMark;

    public AutoMark(String autoMark) {
        requireAllNonNull(autoMark);
        this.autoMark = autoMark;
    }

    @Override
    public String toString() {
        requireAllNonNull(autoMark);
        return "Automark: " + autoMark + "\n";
    }


    public String getValue() {
        return autoMark;
    }

    @Override
    public boolean equals(Object other) {
        requireAllNonNull(autoMark);
        return other == this // short circuit if same object
                || (other instanceof AutoMark // instanceof handles nulls
                && this.autoMark == ((AutoMark) other).autoMark); // state check
    }

    public boolean isEmpty() {
        return autoMark.equals(EMPTY_AUTOMARK);
    }

}