package lab.ui;

import javafx.fxml.FXML;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import lab.commands.UpdateIdCommand;
import lab.domain.FailedToParseException;
import lab.domain.StudyGroup;
import lab.response.Response;
import lab.response.UpdateIdResponse;

import java.io.IOException;
import java.util.function.Consumer;

public class EditGroupController extends AbstractCommandController {

    @FXML
    public AnchorPane enterStudyGroup;
    @FXML
    public EnterStudyGroupController enterStudyGroupController;

    protected Window owner;

    public void setOwner(Window owner) { this.owner = owner; }

    private Long id;
    private Consumer<StudyGroup> onSuccess;

    public void setStudyGroup(StudyGroup studyGroup) {
        id = studyGroup.getId();
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
