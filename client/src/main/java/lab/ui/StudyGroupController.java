package lab.ui;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import lab.domain.StudyGroup;

import java.util.List;

public class StudyGroupController {
    @FXML
    public TableView<StudyGroup> studyGroupTable;

    public void setStudyGroups(List<StudyGroup> items) {
        final ObservableList<StudyGroup> data = FXCollections.observableArrayList(items);

        TableColumn nameCol = new TableColumn<StudyGroup, String>("name");
        nameCol.setCellValueFactory(
             new PropertyValueFactory<StudyGroup,String>("name")
        );

        studyGroupTable.getColumns().addAll(nameCol);
        studyGroupTable.setItems(data);
    }
}
