package commands;

import domain.Semester;

import java.io.Serializable;

public class FilterLessThanSemesterEnumCommand implements Command, Serializable {
    private final Semester semesterEnum;

    public FilterLessThanSemesterEnumCommand(Semester semesterEnum) {
        this.semesterEnum = semesterEnum;
    }

    public Semester getSemesterEnum() {
        return semesterEnum;
    }
}
