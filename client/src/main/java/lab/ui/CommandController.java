package lab.ui;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import lab.CommandReader;
import lab.ConnectionManagerClient;
import lab.auth.Credentials;
import lab.commands.*;
import lab.domain.FailedToParseException;
import lab.domain.PrintRepresentation;
import lab.domain.Semester;
import lab.domain.StudyGroup;
import lab.response.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class CommandController extends AbstractCommandController {

    @FXML
    public void initialize() {
        filterLessThenSemesterChoiceBox.getItems().setAll(Semester.values());
        filterLessThenSemesterChoiceBox.setValue(Semester.SECOND);
    }

    public void setPrimaryStage(Stage primaryStage) { this.primaryStage = primaryStage; }

    public void setConnectionManager(ConnectionManagerClient connectionManager) {
        this.connectionManager = connectionManager;
    }

    public void setCredentials(Credentials credentials) {
        this.credentials = credentials;
    }

    public void help() throws IOException {
        String message = CommandReader.HELP_CONTENTS;
        Pages.openInfoModal(primaryStage, message);
        //todo fix help command
    }

    public void info() throws IOException {
        InfoCommand command = new InfoCommand();
        Response response = getResponse(command);

        if (!(response instanceof InfoResponse)) {
            Pages.openInfoModal(primaryStage.getOwner(), "Unknown command from server");
            return;
        }

        String message = "Collection type: " + ((InfoResponse) response).getCollectionType().toString() + "\n" +
                "Collection creation time: " + PrintRepresentation.Factory.singleton.toPrint(((InfoResponse) response).getCreationDate()) + "\n" +
                "Elements in collection: " + ((InfoResponse) response).getCollectionSize();


        Pages.openInfoModal(primaryStage, message);
    }

    public void show() throws IOException {
        Response response = getResponse(new ShowCommand());
        if (!(response instanceof ShowResponse)) {
            return;
        }

        List<StudyGroup> studyGroups = ((ShowResponse) response).getGroups();
        Pages.openStudyGroupsModal(primaryStage, studyGroups);
    }

    @FXML
    public Node enterStudyGroup;
    @FXML
    public EnterStudyGroupController enterStudyGroupController;

    public void add() throws IOException {
        StudyGroup group;
        try {
            group = enterStudyGroupController.getGroup();
        } catch (FailedToParseException e) {
            Pages.openInfoModal(primaryStage, e.getMessage());
            return;
        }

        Pages.openStudyGroupsModal(primaryStage, Collections.singletonList(group));
    }

    public void updateId() {

    }

    @FXML
    TextField removeByIdIdField;

    public void removeById() throws IOException {
        String idAsStr = removeByIdIdField.getText().trim();
        long id;
        try {
            id = Long.parseLong(idAsStr);
        } catch (NumberFormatException e) {
            // display error
            Pages.openInfoModal(primaryStage, idAsStr + " is not long");
            return;
        }
        RemoveByIdCommand removeByIdCommand = new RemoveByIdCommand(id);

        Response response = getResponse(removeByIdCommand);

        if(!(response instanceof RemoveByIdResponse)) {
            Pages.openInfoModal(primaryStage, "Unknown command from server");
            return;
        }

        if (((RemoveByIdResponse)response).getWasRemoved()) {
            Pages.openInfoModal(primaryStage, "Element was removed");
        } else {
            Pages.openInfoModal(primaryStage, "Element wasn't removed");
        }
    }

    public void clear() throws IOException {
        Command command = new ClearCommand();

        Response response = getResponse(command);

        if (!(response instanceof ClearResponse)) {
            Pages.openInfoModal(primaryStage, "Unknown command from server");
            return;
        }

        int removedCount = ((ClearResponse) response).getElementsRemovedCount();
        String message = "Collection was cleared; " + removedCount + " elements were removed";

        Pages.openInfoModal(primaryStage, message);
    }

    public void addIfMin() {

    }

    public void removeLower() {

    }

    @FXML
    TextField removeAllByStudentsCountField;

    public void removeAllByStudentsCount() throws IOException {
        String countAsString = removeAllByStudentsCountField.getText().trim();
        long count;
        try {
            count = Long.parseLong(countAsString);
        } catch (NumberFormatException e) {
            // display error
            Pages.openInfoModal(primaryStage, countAsString + " is not long");
            return;
        }

        RemoveAllByStudentsCountCommand removeAllByStudentsCountCommand = new RemoveAllByStudentsCountCommand(count);

        Response response = getResponse(removeAllByStudentsCountCommand);

        if (!(response instanceof RemoveAllByStudentsCountResponse)) {
            Pages.openInfoModal(primaryStage, "Unknown command from server");
            return;
        }

        List<StudyGroup> removedGroups = ((RemoveAllByStudentsCountResponse) response).getRemovedGroups();
        Pages.openStudyGroupsModal(primaryStage, removedGroups);
    }

    public void countByGroupAdmin() {

    }

    @FXML
    private ChoiceBox<Semester> filterLessThenSemesterChoiceBox;

    public void filterLessThanSemesterEnum() throws IOException {
        Semester semester = filterLessThenSemesterChoiceBox.getValue();

        Response response = getResponse(new FilterLessThanSemesterEnumCommand(semester));
        if (!(response instanceof FilterLessThanSemesterEnumResponse)) {
            // todo: proper error message
            return;
        }

        List<StudyGroup> studyGroupWithLessSemester = ((FilterLessThanSemesterEnumResponse) response).getStudyGroupWithLessSemester();
        Pages.openStudyGroupsModal(primaryStage, studyGroupWithLessSemester);
    }

    public void logOut() throws IOException {
        Pages.openLoginPage(primaryStage, connectionManager);
    }
}
