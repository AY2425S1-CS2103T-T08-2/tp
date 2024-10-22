package keycontacts.model.student;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Represents a comparator that compares two students based on multiple fields.
 * Wraps around a {@code List<Comparator<Student>>} to compare students by each
 * field
 */
public class StudentComparator implements Comparator<Student> {

    private final List<Comparator<Student>> comparators;
    private final List<String> comparatorMessages;

    public StudentComparator() {
        comparators = new ArrayList<>();
        comparatorMessages = new ArrayList<>();
    }

    public void addComparator(StudentComparatorByField comparator) {
        comparators.add(comparator.comparator);
        comparatorMessages.add(comparator.message);
    }

    public String getSortDescription() {
        return String.join(", ", comparatorMessages);
    }

    @Override
    public int compare(Student o1, Student o2) {
        for (Comparator<Student> comparator : comparators) {
            int result = comparator.compare(o1, o2);
            if (result != 0) {
                return result;
            }
        }
        return 0;
    }

    public static StudentComparatorByField getComparatorForName(SortOrder sortOrder) {
        if (sortOrder.isAscending()) {
            return new StudentComparatorByField(Comparator.comparing(Student::getName), "Name (ascending)");
        }
        return new StudentComparatorByField(Comparator.comparing(Student::getName).reversed(), "Name (descending)");
    }

    public static StudentComparatorByField getComparatorForPhone(SortOrder sortOrder) {
        if (sortOrder.isAscending()) {
            return new StudentComparatorByField(Comparator.comparing(Student::getPhone), "Phone (ascending)");
        }
        return new StudentComparatorByField(Comparator.comparing(Student::getPhone).reversed(), "Phone (descending)");
    }

    public static StudentComparatorByField getComparatorForAddress(SortOrder sortOrder) {
        if (sortOrder.isAscending()) {
            return new StudentComparatorByField(Comparator.comparing(Student::getAddress), "Address (ascending)");
        }
        return new StudentComparatorByField(Comparator.comparing(Student::getAddress).reversed(),
                "Address (descending)");
    }

    public static StudentComparatorByField getComparatorForGradeLevel(SortOrder sortOrder) {
        if (sortOrder.isAscending()) {
            return new StudentComparatorByField(Comparator.comparing(Student::getGradeLevel),
                    "Grade Level (ascending)");
        }
        return new StudentComparatorByField(Comparator.comparing(Student::getGradeLevel).reversed(),
                "Grade Level (descending)");
    }

    public static class SortOrder {
        private final String sortOrder;

        private final String ASCENDING = "ASC";
        private final String DESCENDING = "DESC";

        public SortOrder(String sortOrder) {
            if (!sortOrder.equals(ASCENDING) && !sortOrder.equals(DESCENDING)) {
                throw new IllegalArgumentException("Invalid sort order");
            }
            this.sortOrder = sortOrder;
        }

        public boolean isAscending() {
            return sortOrder.equals("ASC");
        }

        public boolean isDescending() {
            return sortOrder.equals("DESC");
        }
    }
}
