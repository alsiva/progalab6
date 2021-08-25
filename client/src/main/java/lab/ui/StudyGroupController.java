package lab.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lab.domain.*;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

public class StudyGroupController {
    @FXML
    public TableView<StudyGroup> studyGroupTable;

    public void setStudyGroups(List<StudyGroup> items) {
        final ObservableList<StudyGroup> data = FXCollections.observableArrayList(items);

        TableColumn<StudyGroup, String> nameCol = new TableColumn<>("name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<StudyGroup, Coordinates> coordinatesCol = new TableColumn<>("coordinates");

        TableColumn<StudyGroup, Float> coordinatesXCol = new TableColumn<>("coordinates.x");
        TableColumn<StudyGroup, Integer> coordinatesYCol = new TableColumn<>("coordinates.y");

        coordinatesXCol.setCellValueFactory(new PropertyValueFactory<>("coordinateX"));
        coordinatesYCol.setCellValueFactory(new PropertyValueFactory<>("coordinateY"));

        coordinatesCol.getColumns().addAll(coordinatesXCol, coordinatesYCol);

        TableColumn<StudyGroup, Date> creationDateCol = new TableColumn<>("creationDate");
        creationDateCol.setCellValueFactory(new PropertyValueFactory<>("creationDate"));

        TableColumn<StudyGroup, Integer> studentsCountCol = new TableColumn<>("studentsCount");
        studentsCountCol.setCellValueFactory(new PropertyValueFactory<>("studentsCount"));

        TableColumn<StudyGroup, FormOfEducation> formOfEducationCol = new TableColumn<>("formOfEducation");
        formOfEducationCol.setCellValueFactory(new PropertyValueFactory<>("formOfEducation"));

        TableColumn<StudyGroup, Semester> semesterCol = new TableColumn<>("semesterEnum");
        semesterCol.setCellValueFactory(new PropertyValueFactory<>("semesterEnum"));

        TableColumn<StudyGroup, Person> groupAdminCol = new TableColumn<>("groupAdmin");
        groupAdminCol.setCellValueFactory(new PropertyValueFactory<>("groupAdmin"));

        TableColumn<StudyGroup, String> adminNameCol = new TableColumn<>("admin name");
        TableColumn<StudyGroup, LocalDate> adminBirthdayCol = new TableColumn<>("admin birthday");
        TableColumn<StudyGroup, String> adminPassportId = new TableColumn<>("admin passport id");

        adminNameCol.setCellValueFactory(new PropertyValueFactory<>("AdminName"));
        adminNameCol.setCellValueFactory(new PropertyValueFactory<>("AdminBirthday"));
        adminNameCol.setCellValueFactory(new PropertyValueFactory<>("PassportId"));

        groupAdminCol.getColumns().addAll(adminNameCol, adminBirthdayCol, adminPassportId);

        TableColumn<StudyGroup, String> creatorCol = new TableColumn<>("creator");
        creatorCol.setCellValueFactory(new PropertyValueFactory<>("creator"));

        //noinspection unchecked
        studyGroupTable.getColumns().addAll(nameCol, coordinatesCol,creationDateCol, studentsCountCol, formOfEducationCol, semesterCol, groupAdminCol, creatorCol);
        studyGroupTable.setItems(data);
    }
}
