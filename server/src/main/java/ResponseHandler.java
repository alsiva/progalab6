import commands.*;
import domain.StudyGroup;
import response.*;

import java.util.Set;

public class ResponseHandler {
    private final Administration administration;

    public ResponseHandler(Administration administration) {
        this.administration = administration;
    }

    public Response getResponse(Command command) throws CommandNotRecognizedException {
        if (command instanceof HelpCommand) {
            String help = administration.help();
            return new HelpResponse(help);

        } else if (command instanceof InfoCommand) {
            Set<StudyGroup> groups = administration.info();
            return new InfoResponse(groups);

        } else if (command instanceof ShowCommand) {
            Set<StudyGroup> groups = administration.show();
            return new ShowResponse(groups);

        } else if (command instanceof AddCommand) {
            StudyGroup group = administration.add(((AddCommand) command).getGroup());
            return new AddResponse(group);

        } else if (command instanceof UpdateIdCommand) {
            boolean wasUpdated = administration.updateId(((UpdateIdCommand) command).getGroup());
            return new UpdateIdResponse(wasUpdated);

        } else if (command instanceof RemoveByIdCommand) {
            boolean wasRemoved = administration.removeById(((RemoveByIdCommand) command).getId());
            return new RemoveByIdResponse(wasRemoved);

        } else if (command instanceof ClearCommand) {
            administration.clear();
            return new ClearResponse();

        } else if (command instanceof ExecuteScriptCommand) {
            return null; //todo command response for script execution
        } else if (command instanceof AddIfMinCommand) {
            boolean wasAdded = administration.addIfMin(((AddIfMinCommand) command).getStudyGroup());
            return new AddIfMinResponse(wasAdded);

        } else if (command instanceof RemoveLowerCommand) {
            Set<StudyGroup> removedGroups = administration.removeLower(((RemoveLowerCommand) command).getStudyGroup());
            return new RemoveLowerResponse(removedGroups);

        } else if (command instanceof RemoveAllByStudentsCountCommand) {
            Set<StudyGroup> removedGroups = administration.removeAllByStudentsCount(((RemoveAllByStudentsCountCommand) command).getCount());
            return new RemoveAllByStudentsCountResponse(removedGroups);

        } else if (command instanceof CountByGroupAdminCommand) {
            long count = administration.countByGroupAdmin(((CountByGroupAdminCommand) command).getGroupAdmin());
            return new CountByGroupAdminResponse(count);

        } else if (command instanceof FilterLessThanSemesterEnumCommand) {
            Set<StudyGroup> studyGroupsWithLessEnum = administration.filterLessThanSemesterEnum(((FilterLessThanSemesterEnumCommand) command).getSemesterEnum());
            return new FilterLessThanSemesterEnumResponse(studyGroupsWithLessEnum);
        }

        throw new CommandNotRecognizedException(command);
    }

    public static class CommandNotRecognizedException extends Exception {
        private final Command notRecognizedCommand;

        public CommandNotRecognizedException(Command notRecognizedCommand) {
            super();
            this.notRecognizedCommand = notRecognizedCommand;
        }

        public Command getNotRecognizedCommand() {
            return notRecognizedCommand;
        }
    }
}
