package lab.ui;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import lab.domain.*;
import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.Predicate;

public class StudyGroupController extends AbstractCommandController implements LocalizedController {
    private Stage stage;

    public void setStage(Stage stage) { this.stage = stage; }

    @FXML
    public TableView<StudyGroup> studyGroupTable;

    @FXML
    TextField nameFilter, countFilter;
    @FXML
    DatePicker dateFilter, birthdayFilter;
    @FXML
    ComboBox<Semester> semesterFilter;
    @FXML
    ComboBox<FormOfEducation> educationFilter;
    @FXML
    TextField adminNameFilter, passportIdFilter;
    @FXML
    Button clearFiltersButton;

    @Override
    public void updateLanguage(ResourceBundle bundle) {
        nameFilter.setPromptText(bundle.getString("StudyGroupPage.nameFilter"));
        countFilter.setPromptText(bundle.getString("StudyGroupPage.countFilter"));
        dateFilter.setPromptText(bundle.getString("StudyGroupPage.dateFilter"));
        birthdayFilter.setPromptText(bundle.getString("StudyGroupPage.birthdayFilter"));
        semesterFilter.setPromptText(bundle.getString("StudyGroupPage.semesterFilter"));
        educationFilter.setPromptText(bundle.getString("StudyGroupPage.educationFilter"));
        adminNameFilter.setPromptText(bundle.getString("StudyGroupPage.adminNameFilter"));
        passportIdFilter.setPromptText(bundle.getString("StudyGroupPage.passportIdFilter"));
        clearFiltersButton.setText(bundle.getString("StudyGroupPage.clearFilterButton"));
    }

    @FXML
    void clearFilters() {
        nameFilter.setText("");
        countFilter.setText("");
        dateFilter.setValue(null);
        semesterFilter.setValue(null);
        educationFilter.setValue(null);
        adminNameFilter.setText("");
        birthdayFilter.setValue(null);
    }

    private final ObservableList<StudyGroup> list = FXCollections.observableArrayList();
    private final FilteredList<StudyGroup> filteredList = new FilteredList<>(list);


    private final Predicate<StudyGroup> namePredicate = studyGroup -> {
        String trimmed = nameFilter.getText().trim().toLowerCase();
        return trimmed.isEmpty() || studyGroup.getName().toLowerCase().trim().contains(trimmed);
    };
    private final Predicate<StudyGroup> countPredicate = studyGroup -> {
        String trimmed = countFilter.getText().trim();
        try {
            return studyGroup.getStudentsCount() == Integer.parseInt(trimmed);
        } catch (NumberFormatException e) {
            return true;
        }
    };
    private final Predicate<StudyGroup> datePredicate = studyGroup -> {
        LocalDate date = studyGroup.getCreationDate().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        return dateFilter.getValue() == null || date.equals(dateFilter.getValue());
    };
    private final Predicate<StudyGroup> semesterPredicate = studyGroup -> {
        Semester semester = semesterFilter.getValue();
        return semester == null || semester.equals(studyGroup.getSemesterEnum());
    };
    private final Predicate<StudyGroup> educationPredicate = studyGroup -> {
        FormOfEducation education = educationFilter.getValue();
        return education == null || education.equals(studyGroup.getFormOfEducation());
    };
    private final Predicate<StudyGroup> adminNamePredicate = studyGroup -> {
        String trimmed = adminNameFilter.getText().trim().toLowerCase();
        if (trimmed.isEmpty()) {
            return true;
        }

        String adminName = studyGroup.getAdminName();

        return adminName != null && adminName.toLowerCase().trim().contains(trimmed);
    };
    private final Predicate<StudyGroup> adminBirthdayPredicate = studyGroup -> {
        LocalDate birthday = birthdayFilter.getValue();
        if (birthday == null) {
            return true;
        }

        return birthday.equals(studyGroup.getAdminBirthday());
    };
    private final Predicate<StudyGroup> adminPassportPredicate = studyGroup -> {
        String trimmed = passportIdFilter.getText().trim().toLowerCase();
        if (trimmed.isEmpty()) {
            return true;
        }

        String adminPassportId = studyGroup.getAdminPassportId();
        return adminPassportId != null && adminPassportId.toLowerCase().trim().contains(trimmed);
    };

    private void initializeFilters() {
        semesterFilter.getItems().setAll(Semester.values());
        semesterFilter.getItems().add(null);
        educationFilter.getItems().setAll(FormOfEducation.values());
        educationFilter.getItems().add(null);

        nameFilter.textProperty().addListener(this::onFilterChange);
        countFilter.textProperty().addListener(this::onFilterChange);
        dateFilter.valueProperty().addListener(this::onFilterChange);
        semesterFilter.valueProperty().addListener(this::onFilterChange);
        educationFilter.valueProperty().addListener(this::onFilterChange);
        adminNameFilter.textProperty().addListener(this::onFilterChange);
        birthdayFilter.valueProperty().addListener(this::onFilterChange);
        passportIdFilter.textProperty().addListener(this::onFilterChange);
    }

    private <T> void onFilterChange(ObservableValue<? extends T> observable, T oldValue, T newValue) {
        filteredList.setPredicate(namePredicate
            .and(countPredicate)
            .and(datePredicate)
            .and(semesterPredicate)
            .and(educationPredicate)
            .and(adminNamePredicate)
            .and(adminBirthdayPredicate)
            .and(adminPassportPredicate)
        );
    }

    @FXML
    void initialize() {
        initializeColumns();
        initializeFilters();
        studyGroupTable.setItems(filteredList);
    }

    @SuppressWarnings("unchecked")
    private void initializeColumns() {
        TableColumn<StudyGroup, String> nameCol = new TableColumn<>("Group name");
        nameCol.setCellValueFactory(new PropertyValueFactory<>("name"));

        TableColumn<StudyGroup, Coordinates> coordinatesCol = new TableColumn<>("coordinates");

        TableColumn<StudyGroup, Float> coordinatesXCol = new TableColumn<>("x");
        TableColumn<StudyGroup, Integer> coordinatesYCol = new TableColumn<>("y");

        coordinatesXCol.setCellValueFactory(new PropertyValueFactory<>("coordinateX"));
        coordinatesYCol.setCellValueFactory(new PropertyValueFactory<>("coordinateY"));

        coordinatesCol.getColumns().addAll(coordinatesXCol, coordinatesYCol);

        TableColumn<StudyGroup, Date> creationDateCol = new TableColumn<>("Creation date");
        creationDateCol.setCellValueFactory(new PropertyValueFactory<>("creationDate"));

        TableColumn<StudyGroup, Integer> studentsCountCol = new TableColumn<>("headcount");
        studentsCountCol.setCellValueFactory(new PropertyValueFactory<>("studentsCount"));

        TableColumn<StudyGroup, FormOfEducation> formOfEducationCol = new TableColumn<>("Education form");
        formOfEducationCol.setCellValueFactory(new PropertyValueFactory<>("formOfEducation"));

        TableColumn<StudyGroup, Semester> semesterCol = new TableColumn<>("Semester");
        semesterCol.setCellValueFactory(new PropertyValueFactory<>("semesterEnum"));

        TableColumn<StudyGroup, Person> groupAdminCol = new TableColumn<>("Admin");

        TableColumn<StudyGroup, String> adminNameCol = new TableColumn<>("Name");
        TableColumn<StudyGroup, LocalDate> adminBirthdayCol = new TableColumn<>("Birthday");
        TableColumn<StudyGroup, String> adminPassportIdCol = new TableColumn<>("Passport");
        TableColumn<StudyGroup, Location> adminLocationCol = new TableColumn<>("Location");

        adminNameCol.setCellValueFactory(new PropertyValueFactory<>("AdminName"));
        adminBirthdayCol.setCellValueFactory(new PropertyValueFactory<>("AdminBirthday"));
        adminPassportIdCol.setCellValueFactory(new PropertyValueFactory<>("AdminPassportId"));

        groupAdminCol.getColumns().addAll(adminNameCol, adminBirthdayCol, adminPassportIdCol, adminLocationCol);

        TableColumn<StudyGroup, Integer> locationXCol = new TableColumn<>("x");
        TableColumn<StudyGroup, Integer> locationYCol = new TableColumn<>("y");
        TableColumn<StudyGroup, String> locationNameCol = new TableColumn<>("name");

        locationXCol.setCellValueFactory(new PropertyValueFactory<>("locationX"));
        locationYCol.setCellValueFactory(new PropertyValueFactory<>("locationY"));
        locationNameCol.setCellValueFactory(new PropertyValueFactory<>("locationName"));

        adminLocationCol.getColumns().addAll(locationXCol, locationYCol, locationNameCol);

        TableColumn<StudyGroup, String> creatorCol = new TableColumn<>("creator");
        creatorCol.setCellValueFactory(new PropertyValueFactory<>("creator"));

        studyGroupTable.getColumns().addAll(nameCol, coordinatesCol, creationDateCol, studentsCountCol, formOfEducationCol, semesterCol, groupAdminCol, creatorCol);
        studyGroupTable.setOnMousePressed(event -> {
            if (!event.isPrimaryButtonDown() || event.getClickCount() != 2) {
                return;
            }

            TableView.TableViewSelectionModel<StudyGroup> selectionModel = studyGroupTable.getSelectionModel();
            int index = selectionModel.getSelectedIndex();
            StudyGroup group = selectionModel.getSelectedItem();
            try {
                Pages.openEditGroupModal(stage, connectionManager, credentials, group, modifiedGroup -> {
                    list.set(index, modifiedGroup);
                });
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }

    public void initData(List<StudyGroup> items) {
        list.setAll(items);
    }
}
