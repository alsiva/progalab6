package lab.ui;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.stage.Stage;
import lab.commands.*;
import lab.domain.*;
import lab.response.*;
import java.io.IOException;
import java.util.*;

public class CommandController extends AbstractCommandController implements LocalizedController {

    @FXML
    public void initialize() {
        filterLessThenSemesterChoiceBox.getItems().setAll(Semester.values());
        filterLessThenSemesterChoiceBox.setValue(Semester.SECOND);
        changeLanguageController.setChangeLanguageCallback(this::updateLanguage);
    }

    protected Stage primaryStage;

    public void setPrimaryStage(Stage primaryStage) { this.primaryStage = primaryStage; }

    @FXML
    Button addButton, removeLowerButton, addIfMinButton, updateIdButton;

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
        Pages.openStudyGroupsModal(primaryStage, connectionManager, credentials, studyGroups);
    }

    @FXML
    public Node enterStudyGroup;
    @FXML
    public EnterStudyGroupController enterStudyGroupController;

    @FXML
    public Node enterPerson;
    @FXML
    public EnterPersonController enterPersonController;

    public void visualize() throws IOException {
        Pages.openVisualizePage(primaryStage, connectionManager, credentials);
    }

    public void add() throws IOException {
        StudyGroup group;
        try {
            group = enterStudyGroupController.getGroup();
        } catch (FailedToParseException e) {
            Pages.openInfoModal(primaryStage, e.getMessage());
            return;
        }

        getResponse(new AddCommand(group));

        Pages.openStudyGroupsModal(primaryStage, connectionManager, credentials,Collections.singletonList(group));
    }

    @FXML
    TextField EnterIdField;
    public void updateId() throws IOException {
        StudyGroup group;
        try {
            group = enterStudyGroupController.getGroup();
        } catch (FailedToParseException e) {
            Pages.openInfoModal(primaryStage, e.getMessage());
            return;
        }

        String idAsStr = EnterIdField.getText().trim();
        long id;
        try {
            id = Long.parseLong(idAsStr);
        } catch (NumberFormatException e) {
            // display error
            Pages.openInfoModal(primaryStage, idAsStr + " is not long");
            return;
        }

        group.setId(id);

        Response response = getResponse(new UpdateIdCommand(group));

        if (!(response instanceof UpdateIdResponse)) {
            Pages.openInfoModal(primaryStage, "Unknown command from server");
            return;
        }

        if (((UpdateIdResponse) response).getWasUpdated()) {
            Pages.openStudyGroupsModal(primaryStage, connectionManager, credentials,Collections.singletonList(group));
        } else {
            Pages.openInfoModal(primaryStage, "Group wasn't updated");
        }

    }


    @FXML
    Tab simpleCommands, removeStudyGroup, filter, studyGroupCommands, countByGroupAdmin, settings;

    @FXML
    Button clear, info, show, visualize;

    @FXML
    Label byId, byStudentsCount;

    @FXML
    TextField removeAllByStudentsCountField ;

    @FXML
    Button removeByIdButton, remove2, logOutButton, countButton;

    @FXML
    Label showGroupsHavingLessSem;

    @FXML
    Button filterShowGroupsHavingLessSem;

    @Override
    public void updateLanguage(ResourceBundle bundle) {
        simpleCommands.setText(bundle.getString("simpleCommands"));
        removeStudyGroup.setText(bundle.getString("removeStudyGroup"));
        filter.setText(bundle.getString("filter"));
        studyGroupCommands.setText(bundle.getString("studyGroupCommands"));
        countByGroupAdmin.setText(bundle.getString("countByGroupAdmin"));
        settings.setText(bundle.getString("settings"));

        enterStudyGroupController.updateLanguage(bundle);
        addButton.setText(bundle.getString("addButton"));
        removeLowerButton.setText(bundle.getString("removeLowerButton"));
        addIfMinButton.setText(bundle.getString("addIfMinButton"));
        updateIdButton.setText(bundle.getString("updateIdButton"));

        enterPersonController.updateLanguage(bundle);

        removeByIdIdField.setPromptText(bundle.getString("removeByIdPrompt"));
        removeAllByStudentsCountField.setPromptText(bundle.getString("removeAllByStudentsCount"));
        filterLessThenSemesterChoiceBox.setAccessibleText(bundle.getString("filterLessThanSemesterEnum"));

        clear.setText(bundle.getString("clear"));
        info.setText(bundle.getString("info"));
        show.setText(bundle.getString("show"));
        visualize.setText(bundle.getString("visualize"));

        byId.setText(bundle.getString("byId"));
        byStudentsCount.setText(bundle.getString("byStudentsCount"));

        removeByIdButton.setText(bundle.getString("removeByIdButtonLabel"));
        remove2.setText(bundle.getString("remove2"));

        showGroupsHavingLessSem.setText(bundle.getString("showGroupsHavingLessSem"));
        filterLessThenSemesterChoiceBox.setAccessibleText(bundle.getString("filterLessThenSemesterChoiceBox"));
        filterShowGroupsHavingLessSem.setText(bundle.getString("filterShowGroupsHavingLessSem"));

        logOutButton.setText(bundle.getString("logOutButton"));
        countButton.setText(bundle.getString("countButton"));

        changeLanguageController.updateLanguage(bundle);
    }

    @FXML
    public Node changeLanguage;
    @FXML
    public ChangeLanguageController changeLanguageController;

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

    public void addIfMin() throws IOException {

        StudyGroup group;
        try {
            group = enterStudyGroupController.getGroup();
        } catch (FailedToParseException e) {
            Pages.openInfoModal(primaryStage, e.getMessage());
            return;
        }

        Response response = getResponse(new AddIfMinCommand(group));

        if (!(response instanceof AddIfMinResponse)) {
            Pages.openInfoModal(primaryStage, "Unknown command from server");
            return;
        }

        if (((AddIfMinResponse) response).getWasAdded()) {
            Pages.openStudyGroupsModal(primaryStage, connectionManager, credentials,Collections.singletonList(group));
        } else {
            Pages.openInfoModal(primaryStage, "Group wasn't added");
        }

    }

    public void removeLower() throws IOException {
        StudyGroup group;
        try {
            group = enterStudyGroupController.getGroup();
        } catch (FailedToParseException e) {
            Pages.openInfoModal(primaryStage, e.getMessage());
            return;
        }

        Response response = getResponse(new RemoveLowerCommand(group));

        if (!(response instanceof RemoveLowerResponse)) {
            Pages.openInfoModal(primaryStage, "Unknown command from server");
            return;
        }

        List<StudyGroup> removedGroups = ((RemoveLowerResponse) response).getGroups();
        Pages.openStudyGroupsModal(primaryStage, connectionManager, credentials,removedGroups);
    }


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
        Pages.openStudyGroupsModal(primaryStage, connectionManager, credentials,removedGroups);
    }

    public void countByGroupAdmin() throws IOException {
        Person person;
        try {
            person = enterPersonController.getPerson();
        } catch (FailedToParseException e) {
            Pages.openInfoModal(primaryStage, e.getMessage());
            return;
        }

        Response response = getResponse(new CountByGroupAdminCommand(person));

        if (!(response instanceof CountByGroupAdminResponse)) {
            Pages.openInfoModal(primaryStage, "Unknown command from server");
            return;
        }

        long count = ((CountByGroupAdminResponse) response).getCount();
        Pages.openInfoModal(primaryStage, count + " groups with this group admin");

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
        Pages.openStudyGroupsModal(primaryStage, connectionManager, credentials,studyGroupWithLessSemester);
    }

    public void logOut() throws IOException {
        Pages.openLoginPage(primaryStage, connectionManager);
    }
}
