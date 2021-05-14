import domain.PrintRepresentation;
import domain.StudyGroup;
import response.*;

import java.util.List;
import java.util.Set;

public class ResponseHandlerClient {
    private static final PrintRepresentation printRepresentation = new PrintRepresentation();

    public void handleResponse(Response response) {
        if (response instanceof HelpResponse) {
            System.out.println(((HelpResponse) response).getResponse());
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
            System.out.println("Added group; generated id " + ((AddResponse) response).getStudyGroupId());

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
