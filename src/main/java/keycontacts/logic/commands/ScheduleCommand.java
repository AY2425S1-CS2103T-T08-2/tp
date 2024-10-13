package keycontacts.logic.commands;

import static java.util.Objects.requireNonNull;
import static keycontacts.logic.parser.CliSyntax.PREFIX_DAY;
import static keycontacts.logic.parser.CliSyntax.PREFIX_END_TIME;
import static keycontacts.logic.parser.CliSyntax.PREFIX_START_TIME;

import java.util.List;

import keycontacts.commons.core.index.Index;
import keycontacts.commons.util.ToStringBuilder;
import keycontacts.logic.Messages;
import keycontacts.logic.commands.exceptions.CommandException;
import keycontacts.model.Model;
import keycontacts.model.lesson.RegularLesson;
import keycontacts.model.student.Student;

/**
 * Schedules the regular lesson for a student identified using it's displayed index from the student directory.
 */
public class ScheduleCommand extends Command {

    public static final String COMMAND_WORD = "schedule";

    public static final String MESSAGE_USAGE = COMMAND_WORD
            + ": Schedules the regular lesson for the student identified by the index number"
            + " used in the displayed student list. This will overwrite the student's existing regular lesson,"
            + " if it exists. The scheduled lesson cannot clash with any existing lessons for other students.\n"
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_DAY + "DAY "
            + PREFIX_START_TIME + "START_TIME "
            + PREFIX_END_TIME + "END_TIME\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_DAY + "Monday "
            + PREFIX_START_TIME + "16:00 "
            + PREFIX_END_TIME + "18:00";

    public static final String MESSAGE_SCHEDULE_LESSON_SUCCESS = "Scheduled lesson for student: %1$s";
    public static final String MESSAGE_LESSON_UNCHANGED = "Lesson for the student is already at that time!";
    public static final String MESSAGE_LESSON_CLASH = "There is a clashing lesson at that time!";

    private final Index targetIndex;
    private final RegularLesson regularLesson;

    /**
     * Creates a ScheduleCommand to schedule the given {@code regularLesson}
     */
    public ScheduleCommand(Index targetIndex, RegularLesson regularLesson) {
        this.targetIndex = targetIndex;
        this.regularLesson = regularLesson;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Student> lastShownList = model.getFilteredStudentList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
        }

        Student studentToUpdate = lastShownList.get(targetIndex.getZeroBased());
        Student updatedStudent = studentToUpdate.withRegularLesson(regularLesson);
        if (studentToUpdate.equals(updatedStudent)) {
            throw new CommandException(MESSAGE_LESSON_UNCHANGED);
        }
        model.setStudent(studentToUpdate, updatedStudent);

        return new CommandResult(String.format(MESSAGE_SCHEDULE_LESSON_SUCCESS, Messages.format(studentToUpdate)));
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof ScheduleCommand otherScheduleCommand)) {
            return false;
        }

        return targetIndex.equals(otherScheduleCommand.targetIndex)
                && regularLesson.equals(otherScheduleCommand.regularLesson);
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("targetIndex", targetIndex)
                .add("regularLesson", regularLesson)
                .toString();
    }
}
