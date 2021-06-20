package lab;

import lab.auth.AuthorizationManager;
import lab.auth.Credentials;
import lab.domain.PrintRepresentation;
import lab.domain.StudyGroup;
import lab.response.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.List;
import java.util.Optional;

public class ResponseHandlerClient {
    private static final PrintRepresentation printRepresentation = new PrintRepresentation();
    private final AuthorizationManager authorizationManager;

    public ResponseHandlerClient(AuthorizationManager authorizationManager) {
        this.authorizationManager = authorizationManager;
    }

    public void handleResponse(Response response) {
        if (response instanceof AuthorizationFailedResponse) {
            AuthorizationFailedResponse authorizationFailedResponse = (AuthorizationFailedResponse) response;
            System.out.println("Problem with authorization invoking this command: " + authorizationFailedResponse.getCommand().toPrint());

            Optional<Credentials> currentCredentials = authorizationManager.getCredentials();
            if (currentCredentials.isPresent()) {
                Credentials failedResponseCredentials = authorizationFailedResponse.getCredentials();
                if (failedResponseCredentials.equals(currentCredentials.get())) {
                    authorizationManager.logout();
                    System.out.println("User " + failedResponseCredentials.username + " was logged out");
                }
            }
        } else if (response instanceof CheckCredentialsResponse) {
            CheckCredentialsResponse checkCredentialsResponse = (CheckCredentialsResponse) response;
            Credentials credentials = checkCredentialsResponse.getCredentials();

            if (checkCredentialsResponse.isAuthorized()) {
                System.out.println("Logged in as " + credentials.username);
                authorizationManager.authorize(credentials);
            } else {
                System.out.println("Failed to authorize " + credentials.username + " with password " + credentials.password);
            }
        } else if (response instanceof CantModifyResponse) {
            long studyGroupId = ((CantModifyResponse) response).getStudyGroupId();
            System.out.println("You're not allowed to modify group #" + studyGroupId + ". You are not the group creator.");

        } else if (response instanceof RegistrationResultResponse) {
            RegistrationResultResponse registrationResultResponse = (RegistrationResultResponse) response;
            Credentials credentials = registrationResultResponse.getCredentials();

            if (registrationResultResponse.isSuccessful()) {
                System.out.println("User " + credentials.username + " was created, do you want to log in (yes/no)?:");
                BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
                String answer;
                try {
                    answer = in.readLine();
                } catch (IOException e) {
                    System.err.println("Failed to read answer: " + e.getMessage());
                    return;
                }

                if (!answer.equals("no")) {
                    authorizationManager.authorize(credentials);
                }

            } else {
                System.out.println("Failed to create new user " + credentials.username + " with password" + credentials.password);
            }
        } else if (response instanceof InfoResponse) {
            InfoResponse infoResponse = (InfoResponse) response;
            String responseString = "Collection type: " + infoResponse.getCollectionType().toString() + "\n" +
                    "Collection creation time: " + printRepresentation.toPrint(infoResponse.getCreationDate()) + "\n" +
                    "Elements in collection: " + infoResponse.getCollectionSize();
            System.out.println(responseString);
        } else if (response instanceof ShowResponse) {
            List<StudyGroup> groups = ((ShowResponse) response).getGroups();
            for (StudyGroup studyGroup: groups) {
                System.out.println(printRepresentation.toPrint(studyGroup));
            }
        } else if (response instanceof AddResponse) {
            Optional<Long> studyGroupId = ((AddResponse) response).getStudyGroupId();
            System.out.println(studyGroupId
                    .map(id -> "Added group; generated id " + id)
                    .orElse("Failed to add group")
            );

        } else if (response instanceof UpdateIdResponse) {
            if (((UpdateIdResponse) response).getWasUpdated()) {
                System.out.println("Group was updated");
            } else  {
                System.out.println("No element with this id");
            }
        } else if (response instanceof RemoveByIdResponse) {
            boolean wasRemoved = ((RemoveByIdResponse) response).getWasRemoved();
            if (wasRemoved) {
                System.out.println("Group was removed");
            } else {
                System.out.println("Nothing was removed");
            }
        } else if (response instanceof ClearResponse) {
            int removedCount = ((ClearResponse) response).getElementsRemovedCount();
            System.out.println("Collection was cleared; " + removedCount + " elements were removed");
        } else if (response instanceof AddIfMinResponse) {
            Boolean wasAdded = ((AddIfMinResponse) response).getWasAdded();
            if (wasAdded) {
                System.out.println("Group was added");
            } else {
                System.out.println("Group wasn't added");
            }
        } else if (response instanceof RemoveLowerResponse) {
            List<StudyGroup> groups = ((RemoveLowerResponse) response).getGroups();
            System.out.println("Removed groups:");
            for (StudyGroup studyGroup: groups) {
                System.out.println(printRepresentation.toPrint(studyGroup));
            }
        } else if (response instanceof RemoveAllByStudentsCountResponse) {
            List<StudyGroup> groups = ((RemoveAllByStudentsCountResponse) response).getRemovedGroups();
            System.out.println("Removed groups:");
            for (StudyGroup studyGroup: groups) {
                System.out.println(printRepresentation.toPrint(studyGroup));
            }
        } else if (response instanceof CountByGroupAdminResponse) {
            long count = ((CountByGroupAdminResponse) response).getCount();
            System.out.println("Number of groups with this admin: " + count);
        } else if (response instanceof FilterLessThanSemesterEnumResponse) {
            List<StudyGroup> groups = ((FilterLessThanSemesterEnumResponse) response).getStudyGroupWithLessSemester();
            System.out.println("Groups with lessEnum");
            for (StudyGroup studyGroup: groups) {
                System.out.println(printRepresentation.toPrint(studyGroup));
            }
        }
    }
}
