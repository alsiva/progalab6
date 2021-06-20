package lab.response;

import lab.domain.StudyGroup;

import java.io.Serializable;
import java.util.List;

public class FilterLessThanSemesterEnumResponse implements Serializable, Response {
    private final List<StudyGroup> studyGroupWithLessSemester;

    public FilterLessThanSemesterEnumResponse(List<StudyGroup> studyGroupWithLessSemester) {
        this.studyGroupWithLessSemester = studyGroupWithLessSemester;
    }

    public List<StudyGroup> getStudyGroupWithLessSemester() {
        return studyGroupWithLessSemester;
    }
}
