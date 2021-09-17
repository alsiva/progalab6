package lab.ui;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import lab.commands.UpdateIdCommand;
import lab.domain.FailedToParseException;
import lab.domain.StudyGroup;
import lab.response.Response;
import lab.response.UpdateIdResponse;
import java.io.IOException;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class EditGroupController extends AbstractCommandController implements LocalizedController {

    @FXML
    public AnchorPane enterStudyGroup;
    @FXML
    public EnterStudyGroupController enterStudyGroupController;

    protected Window owner;

    public void setOwner(Window owner) { this.owner = owner; }

    private Long id;
    private String creator;
    private Consumer<StudyGroup> onSuccess;

    @FXML
    Button updateButton;

    @Override
    public void updateLanguage(ResourceBundle bundle) {
        enterStudyGroupController.updateLanguage(bundle);
        updateButton.setText(bundle.getString("UpdateStudyGroup.updateButton"));

    }

    public void setStudyGroup(StudyGroup studyGroup) {
        id = studyGroup.getId();
        creator = studyGroup.getCreator();
        enterStudyGroupController.setGroup(studyGroup);
    }

    public void update() throws IOException {
        StudyGroup group;
        try {
            group = enterStudyGroupController.getGroup();
        } catch (FailedToParseException e) {
            Pages.openInfoModal(owner, e.getMessage());
            return;
        }
        group.setId(id);
        group.setCreator(creator);
        Response response = getResponse(new UpdateIdCommand(group));

        if (!(response instanceof UpdateIdResponse)) {
            Pages.openInfoModal(owner, "Unknown command from server");
            return;
        }

        UpdateIdResponse updateIdResponse = (UpdateIdResponse) response;
        if (!updateIdResponse.getWasUpdated()) {
            Pages.openInfoModal(owner, "Group wasn't updated");
            return;
        }

        Stage stage = (Stage) enterStudyGroup.getScene().getWindow();
        stage.close();
        onSuccess.accept(group);
    }

    public void setOnSuccess(Consumer<StudyGroup> onSuccess) {
        this.onSuccess = onSuccess;
    }
}
