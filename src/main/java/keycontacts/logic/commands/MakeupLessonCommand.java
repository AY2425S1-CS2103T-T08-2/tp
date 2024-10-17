package keycontacts.logic.commands;

import static java.util.Objects.requireNonNull;
import static keycontacts.commons.util.CollectionUtil.requireAllNonNull;
import static keycontacts.logic.parser.CliSyntax.PREFIX_DATE;
import static keycontacts.logic.parser.CliSyntax.PREFIX_END_TIME;
import static keycontacts.logic.parser.CliSyntax.PREFIX_START_TIME;

import java.util.List;

import keycontacts.commons.core.index.Index;
import keycontacts.logic.Messages;
import keycontacts.logic.commands.exceptions.CommandException;
import keycontacts.model.Model;
import keycontacts.model.lesson.MakeupLesson;
import keycontacts.model.student.Student;

/**
 * Creates a new makeup lesson for a student identified using its displayed
 * index from the student directory
 */

public class MakeupLessonCommand extends Command {

    public static final String COMMAND_WORD = "makeup";

    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Creates a new makeup lesson "
            + "for a student identified by the index number used in the displayed student list. "
            + "Parameters: INDEX (must be a positive integer) "
            + PREFIX_DATE + "DATE "
            + PREFIX_START_TIME + "START_TIME "
            + PREFIX_END_TIME + "END_TIME\n"
            + "Example: " + COMMAND_WORD + " 1 "
            + PREFIX_DATE + "01-07-2025 "
            + PREFIX_START_TIME + "12:00 "
            + PREFIX_END_TIME + "13:00";

    public static final String MESSAGE_SUCCESS = "Makeup lesson created.";

    private final Index targetIndex;
    private final MakeupLesson makeupLesson;

    /**
     * @param date      of the makeup lesson
     * @param startTime of the makeup lesson
     * @param endTime   of the makeup lesson
     */
    public MakeupLessonCommand(Index targetIndex, MakeupLesson makeupLesson) {
        requireAllNonNull(targetIndex, makeupLesson);
        this.targetIndex = targetIndex;
        this.makeupLesson = makeupLesson;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        requireNonNull(model);
        List<Student> lastShownList = model.getFilteredStudentList();

        if (targetIndex.getZeroBased() >= lastShownList.size()) {
            throw new CommandException(Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX);
        }

        Student studentToUpdate = lastShownList.get(targetIndex.getZeroBased());
        Student updatedStudent = studentToUpdate.withAddedMakeupLesson(makeupLesson);

        model.setStudent(studentToUpdate, updatedStudent);

        return new CommandResult(String.format(MESSAGE_SUCCESS, Messages.format(updatedStudent)));
    }

    @Override
    public boolean equals(Object other) {
        return other == this // short circuit if same object
                || (other instanceof MakeupLessonCommand // instanceof handles nulls
                        && makeupLesson.equals(((MakeupLessonCommand) other).makeupLesson));
    }

}
