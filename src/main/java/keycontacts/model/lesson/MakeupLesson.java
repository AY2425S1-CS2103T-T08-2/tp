package keycontacts.model.lesson;

import static keycontacts.commons.util.CollectionUtil.requireAllNonNull;

import java.util.Objects;

import keycontacts.commons.util.ToStringBuilder;

/**
 * Represents a makeup lesson which happens if a student misses a lesson.
 */
public class MakeupLesson extends Lesson {

    private final Date lessonDate;

    /**
     * Every field must be present and not null.
     */
    public MakeupLesson(Date lessonDate, Time startTime, Time endTime) {
        super(startTime, endTime);
        requireAllNonNull(lessonDate);
        this.lessonDate = lessonDate;
    }

    public Date getLessonDate() {
        return lessonDate;
    }

    @Override
    public int hashCode() {
        return Objects.hash(lessonDate, getStartTime(), getEndTime());
    }

    @Override
    public boolean equals(Object other) {
        if (other == this) {
            return true;
        }

        // instanceof handles nulls
        if (!(other instanceof MakeupLesson)) {
            return false;
        }

        MakeupLesson otherLesson = (MakeupLesson) other;
        return lessonDate.equals(otherLesson.lessonDate)
                && getStartTime().equals(otherLesson.getStartTime())
                && getEndTime().equals(otherLesson.getEndTime());
    }

    @Override
    public String toString() {
        return new ToStringBuilder(this)
                .add("lessonDate", lessonDate)
                .add("startTime", getStartTime())
                .add("endTime", getEndTime())
                .toString();
    }

    @Override
    public String toDisplay() {
        return lessonDate.toString() + " " + super.toDisplay();
    }
}
