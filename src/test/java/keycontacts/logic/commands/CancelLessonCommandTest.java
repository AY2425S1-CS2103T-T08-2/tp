package keycontacts.logic.commands;

import static keycontacts.logic.commands.CommandTestUtil.VALID_DATE;
import static keycontacts.logic.commands.CommandTestUtil.VALID_START_TIME;
import static keycontacts.testutil.Assert.assertThrows;
import static keycontacts.testutil.TypicalIndexes.INDEX_FIRST_STUDENT;
import static keycontacts.testutil.TypicalStudents.ALICE;
import static keycontacts.testutil.TypicalStudents.getTypicalStudentDirectory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import keycontacts.commons.core.index.Index;
import keycontacts.logic.Messages;
import keycontacts.logic.commands.exceptions.CommandException;
import keycontacts.model.Model;
import keycontacts.model.ModelManager;
import keycontacts.model.UserPrefs;
import keycontacts.model.lesson.CancelledLesson;
import keycontacts.model.lesson.Date;
import keycontacts.model.lesson.Time;
import keycontacts.model.student.Student;

public class CancelLessonCommandTest {
    private Model model;

    @BeforeEach
    public void initialise() {
        model = new ModelManager(getTypicalStudentDirectory(), new UserPrefs());
    }

    @Test
    public void constructor_nullDate_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new CancelLessonCommand(INDEX_FIRST_STUDENT, null, new Time(VALID_START_TIME)));
    }


    @Test
    public void constructor_nullTime_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new CancelLessonCommand(INDEX_FIRST_STUDENT, new Date(VALID_DATE), null));
    }

    @Test
    public void constructor_nullIndex_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () ->
                new CancelLessonCommand(null, new Date(VALID_DATE), new Time(VALID_START_TIME)));
    }

    @Test
    public void execute_indexOutOfBounds_failure() {
        Index outOfBoundsIndex = Index.fromOneBased(model.getFilteredStudentList().size() + 1);
        CancelLessonCommand command = new CancelLessonCommand(outOfBoundsIndex, new Date(VALID_DATE),
                new Time(VALID_START_TIME));

        assertThrows(CommandException.class,
                Messages.MESSAGE_INVALID_STUDENT_DISPLAYED_INDEX, () -> command.execute(model));
    }

    @Test
    public void execute_lessonNotFound_throwsCommandException() {
        CancelLessonCommand command = new CancelLessonCommand(INDEX_FIRST_STUDENT, new Date(VALID_DATE),
                new Time(VALID_START_TIME));

        assertThrows(CommandException.class,
                String.format(CancelLessonCommand.MESSAGE_LESSON_NOT_FOUND, Messages.format(ALICE)), () ->
                        command.execute(model));
    }

    @Test
    public void execute_validInputs_success() throws CommandException {
        Date aliceDate = new Date("14-10-2024");
        Time aliceTime = new Time("12:00");
        CancelLessonCommand command = new CancelLessonCommand(INDEX_FIRST_STUDENT, aliceDate, aliceTime);
        Student studentToUpdate = model.getFilteredStudentList().get(INDEX_FIRST_STUDENT.getZeroBased());
        CancelledLesson cancelledLesson = new CancelledLesson(aliceDate);
        Student expectedUpdatedStudent = studentToUpdate.withAddedCancelledLesson(cancelledLesson);
        CommandResult result = command.execute(model);
        String expectedMessage = String.format(CancelLessonCommand.MESSAGE_SUCCESS,
                aliceDate, aliceTime, Messages.format(expectedUpdatedStudent));
        assertEquals(expectedMessage, result.getFeedbackToUser());
    }

    @Test
    public void equals() {
        String differentDate = "01-01-2023";
        String differentTime = "12:00";
        CancelLessonCommand command = new CancelLessonCommand(INDEX_FIRST_STUDENT, new Date(VALID_DATE),
                new Time(VALID_START_TIME));
        CancelLessonCommand commandDuplicate = new CancelLessonCommand(INDEX_FIRST_STUDENT, new Date(VALID_DATE),
                new Time(VALID_START_TIME));
        CancelLessonCommand differentDateAndTimeCommand =
                new CancelLessonCommand(INDEX_FIRST_STUDENT, new Date(differentDate), new Time(differentTime));
        CancelLessonCommand differentTimeCommand =
                new CancelLessonCommand(INDEX_FIRST_STUDENT, new Date(VALID_DATE), new Time(differentTime));
        CancelLessonCommand differentDateCommand =
                new CancelLessonCommand(INDEX_FIRST_STUDENT, new Date(differentDate), new Time(VALID_START_TIME));

        assertTrue(command.equals(command)); // same object
        assertTrue(command.equals(commandDuplicate)); // different object
        assertFalse(command.equals(differentDateAndTimeCommand)); // different values
        assertFalse(command.equals(differentTimeCommand)); // different time
        assertFalse(command.equals(differentDateCommand)); // different date
        assertFalse(command.equals(null)); // null
    }

    @Test
    public void toStringMethod() {
        CancelLessonCommand cancelLessonCommand = new CancelLessonCommand(INDEX_FIRST_STUDENT, new Date(VALID_DATE),
                new Time(VALID_START_TIME));
        String expected = CancelLessonCommand.class.getCanonicalName() + "{date=" + new Date(VALID_DATE) + ", "
                + "startTime=" + new Time(VALID_START_TIME) + ", index=" + INDEX_FIRST_STUDENT + "}";
        assertEquals(expected, cancelLessonCommand.toString());
    }
}