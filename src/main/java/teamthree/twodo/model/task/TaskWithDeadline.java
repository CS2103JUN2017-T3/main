package teamthree.twodo.model.task;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.Set;

import teamthree.twodo.model.tag.Tag;
//@@author A0124399W
public class TaskWithDeadline extends Task implements ReadOnlyTask {

    private Deadline deadline;

    public TaskWithDeadline(Name name, Deadline deadline, Description description, Set<Tag> tags, boolean isComplete) {
        super(name, description, tags, isComplete);
        this.deadline = deadline;
    }

    public TaskWithDeadline(ReadOnlyTask source) {
        this(source.getName(), source.getDeadline().get(), source.getDescription(), source.getTags(),
                source.isCompleted());
    }

    public void setDeadline(Deadline deadline) {
        this.deadline = deadline;
    }

    @Override
    public Optional<Deadline> getDeadline() {
        return Optional.of(deadline);
    }

    /**
     * Formats the TaskWithDeadline as text
     */
    @Override
    public String getAsText() {
        assert (deadline != null);
        final StringBuilder builder = new StringBuilder();
        builder.append(getName() + "\n").append(getDeadline().get()).append("Description: ")
                .append(getDescription()).append("Completed: ").append(isCompleted() + "\n").append("Tags: ");
        getTags().forEach(builder::append);
        builder.append("\n");
        return builder.toString();
    }

    /**
     * Updates this task with the details of {@code replacement}.
     */
    @Override
    public void resetData(ReadOnlyTask replacement) {
        requireNonNull(replacement);

        this.setName(replacement.getName());
        this.setDeadline(replacement.getDeadline().get());
        this.setDescription(replacement.getDescription());
        this.setTags(replacement.getTags());
        completed = replacement.isCompleted();
        if (completed) {
            this.markCompleted();
        } else {
            this.markIncompleted();
        }
    }

}
