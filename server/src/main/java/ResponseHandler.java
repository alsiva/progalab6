import commands.*;
import domain.StudyGroup;
import response.*;

import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

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
            Set<StudyGroup> groups = administration.getGroups();
            return new InfoResponse(groups.getClass(), groups.size());

        } else if (command instanceof ShowCommand) {
            Set<StudyGroup> groups = administration.getGroups();
            return new ShowResponse(sorted(groups));

        } else if (command instanceof AddCommand) {
            Optional<Long> id = administration.add(((AddCommand) command).getGroup());
            return new AddResponse(id);

        } else if (command instanceof UpdateIdCommand) {
            boolean wasUpdated = administration.updateId(((UpdateIdCommand) command).getGroup());
            return new UpdateIdResponse(wasUpdated);

        } else if (command instanceof RemoveByIdCommand) {
            boolean wasRemoved = administration.removeById(((RemoveByIdCommand) command).getId());
            return new RemoveByIdResponse(wasRemoved);

        } else if (command instanceof ClearCommand) {
            int elementsRemovedCount = administration.clear();
            return new ClearResponse(elementsRemovedCount);

        } else if (command instanceof AddIfMinCommand) {
            boolean wasAdded = administration.addIfMin(((AddIfMinCommand) command).getStudyGroup());
            return new AddIfMinResponse(wasAdded);

        } else if (command instanceof RemoveLowerCommand) {
            Set<StudyGroup> removedGroups = administration.removeLower(((RemoveLowerCommand) command).getStudyGroup());
            return new RemoveLowerResponse(sorted(removedGroups));

        } else if (command instanceof RemoveAllByStudentsCountCommand) {
            Set<StudyGroup> removedGroups = administration.removeAllByStudentsCount(((RemoveAllByStudentsCountCommand) command).getCount());
            return new RemoveAllByStudentsCountResponse(sorted(removedGroups));

        } else if (command instanceof CountByGroupAdminCommand) {
            long count = administration.countByGroupAdmin(((CountByGroupAdminCommand) command).getGroupAdmin());
            return new CountByGroupAdminResponse(count);

        } else if (command instanceof FilterLessThanSemesterEnumCommand) {
            Set<StudyGroup> studyGroupsWithLessEnum = administration.filterLessThanSemesterEnum(((FilterLessThanSemesterEnumCommand) command).getSemesterEnum());

            return new FilterLessThanSemesterEnumResponse(sorted(studyGroupsWithLessEnum));
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

    private static List<StudyGroup> sorted(Set<StudyGroup> groups) {
        return groups.stream()
                .sorted(studentCountComparator)
                .collect(Collectors.toList());
    }
    private static final Comparator<? super StudyGroup> studentCountComparator = Comparator.comparing(StudyGroup::getStudentsCount);
}
