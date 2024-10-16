package keycontacts.ui;

import java.time.format.DateTimeFormatter;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import keycontacts.model.lesson.MakeupLesson;
import keycontacts.model.student.Student;

/**
 * An UI component that displays information of a {@code Student}.
 */
public class StudentCard extends UiPart<Region> {

    private static final String FXML = "StudentListCard.fxml";
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMM dd, yyyy");

    /**
     * Note: Certain keywords such as "location" and "resources" are reserved
     * keywords in JavaFX.
     * As a consequence, UI elements' variable names cannot be set to such keywords
     * or an exception will be thrown by JavaFX during runtime.
     *
     * @see <a href="https://github.com/se-edu/addressbook-level4/issues/336">The
     *      issue on AddressBook level 4</a>
     */

    public final Student student;

    @FXML
    private HBox cardPane;
    @FXML
    private Label name;
    @FXML
    private Label id;
    @FXML
    private Label phone;
    @FXML
    private Label address;
    @FXML
    private Label gradeLevel;
    @FXML
    private Label regularLesson;
    @FXML
    private Label pianoPieces;
    @FXML
    private Label makeupLessons;

    /**
     * Creates a {@code StudentCode} with the given {@code Student} and index to
     * display.
     */
    public StudentCard(Student student, int displayedIndex) {
        super(FXML);
        this.student = student;
        id.setText(displayedIndex + ". ");
        name.setText(student.getName().fullName);
        phone.setText(student.getPhone().value);
        address.setText(student.getAddress().value);
        gradeLevel.setText(student.getGradeLevel().value);
        regularLesson.setText(student.getRegularLessonDisplay());
        pianoPieces.setText(student.getPianoPieces().stream()
                .sorted(Comparator.comparing(pianoPiece -> pianoPiece.pianoPieceName))
                .map(pianoPiece -> pianoPiece.pianoPieceName)
                .collect(Collectors.joining(", ")));
        makeupLessons.setText(formatMakeupLessons(student));

    }

    /**
     * Formats the makeup lessons with index, date, and time.
     */
    private String formatMakeupLessons(Student student) {
        List<MakeupLesson> sortedLessons = student.getMakeupLessons().stream()
                .sorted(Comparator.comparing(MakeupLesson::getLessonDate))
                .collect(Collectors.toList());

        if (sortedLessons.isEmpty()) {
            return "No Makeup Lessons scheduled";
        }

        return IntStream.range(0, sortedLessons.size())
                .mapToObj(i -> String.format(
                        "%d. Date: %s, Time: %s - %s",
                        i + 1,
                        sortedLessons.get(i).getLessonDate().format(formatter),
                        sortedLessons.get(i).getStartTime(),
                        sortedLessons.get(i).getEndTime()))
                .collect(Collectors.joining("\n", "Makeup Lessons:\n", ""));
    }
}
