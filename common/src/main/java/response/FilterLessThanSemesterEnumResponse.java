package response;

import domain.StudyGroup;

import java.io.Serializable;
import java.util.Set;

public class FilterLessThanSemesterEnumResponse implements Serializable, Response {
    private final Set<StudyGroup> studyGroupWithLessSemester;

    public FilterLessThanSemesterEnumResponse(Set<StudyGroup> studyGroupWithLessSemester) {
        this.studyGroupWithLessSemester = studyGroupWithLessSemester;
    }

    public Set<StudyGroup> getStudyGroupWithLessSemester() {
        return studyGroupWithLessSemester;
    }
}
