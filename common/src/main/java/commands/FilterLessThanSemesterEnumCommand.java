package commands;

import domain.Semester;

public class FilterLessThanSemesterEnumCommand {
    private final Semester semesterEnum;

    public FilterLessThanSemesterEnumCommand(Semester semesterEnum) {
        this.semesterEnum = semesterEnum;
    }

    public Semester getSemesterEnum() {
        return semesterEnum;
    }
}
