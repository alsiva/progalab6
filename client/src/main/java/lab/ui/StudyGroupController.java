package lab.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lab.domain.*;

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
        coordinatesCol.setCellValueFactory(new PropertyValueFactory<>("coordinates"));

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

        TableColumn<StudyGroup, String> creatorCol = new TableColumn<>("creator");
        creatorCol.setCellValueFactory(new PropertyValueFactory<>("creator"));

        //noinspection unchecked
        studyGroupTable.getColumns().addAll(nameCol, coordinatesCol,creationDateCol, studentsCountCol, formOfEducationCol, semesterCol, groupAdminCol, creatorCol);
        studyGroupTable.setItems(data);
    }
}
