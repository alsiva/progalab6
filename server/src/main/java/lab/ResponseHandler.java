package lab;

import lab.auth.AuthorizationControlManager;
import lab.auth.Credentials;
import lab.auth.NotAuthorizedException;
import lab.commands.*;
import lab.domain.StudyGroup;
import lab.response.*;

import java.net.SocketAddress;
import java.util.*;
import java.util.stream.Collectors;

public class ResponseHandler {
    private final Administration administration;
    private final AuthorizationControlManager authorizationManager;
    private final Notifier notifier;

    public ResponseHandler(Administration administration, AuthorizationControlManager authorizationManager, Notifier notifier) {
        this.administration = administration;
        this.authorizationManager = authorizationManager;
        this.notifier = notifier;
    }

    public Response getResponse(Command command, Credentials credentials, SocketAddress senderAddress) throws CommandNotRecognizedException {
        if (command instanceof RegisterCommand) {
            boolean isSuccessful = authorizationManager.createNewUser(credentials);
            return new RegistrationResultResponse(isSuccessful, credentials);
        }

        boolean isAuthorized = authorizationManager.checkCredentials(credentials);
        if (command instanceof CheckCredentialsCommand) {
            return new CheckCredentialsResponse(isAuthorized, credentials);
        }

        if (!isAuthorized) {
            return new AuthorizationFailedResponse(command, credentials);
        }

        if (command instanceof LogoutCommand) {
            return new LogoutResponse();
        }

        if (command instanceof InfoCommand) {
            Set<StudyGroup> groups = administration.getGroups();
            return new InfoResponse(groups.getClass(), groups.size());

        }
        if (command instanceof ShowCommand) {
            Set<StudyGroup> groups = administration.getGroups();
            return new ShowResponse(sorted(groups));

        }
        if (command instanceof AddCommand) {
            Optional<Long> optionalId = administration.add(((AddCommand) command).getGroup(), credentials.username);
            if (optionalId.isPresent()) {
                notifier.notifyAboutAddingGroup(((AddCommand) command).getGroup(), senderAddress);
            }
            return new AddResponse(optionalId.orElse(null));

        }
        if (command instanceof UpdateIdCommand) {
            StudyGroup group = ((UpdateIdCommand) command).getGroup();
            try {
                boolean wasUpdated = administration.updateId(group, credentials.username);
                if (wasUpdated) {
                    notifier.notifyAboutGroupChange(group, senderAddress);
                }
                return new UpdateIdResponse(wasUpdated);
            } catch (NotAuthorizedException e) {
                return new CantModifyResponse(group.getId());
            }

        }
        if (command instanceof RemoveByIdCommand) {
            long id = ((RemoveByIdCommand) command).getId();
            try {
                boolean wasRemoved = administration.removeById(id, credentials.username);
                if (wasRemoved) {
                    Set<Long> ids = new HashSet<>();
                    ids.add(id);
                    notifier.notifyAboutRemovingGroup(ids, senderAddress);
                }
                return new RemoveByIdResponse(wasRemoved);
            } catch (NotAuthorizedException e) {
                return new CantModifyResponse(id);
            }

        }
        if (command instanceof ClearCommand) {
            Set<StudyGroup> removedGroups = administration.getGroups();
            Set<Long> removedIds = removedGroups.stream().map(StudyGroup::getId).collect(Collectors.toSet());
            notifier.notifyAboutRemovingGroup(removedIds, senderAddress);
            int elementsRemovedCount = administration.clear(credentials.username);
            return new ClearResponse(elementsRemovedCount);

        }
        if (command instanceof AddIfMinCommand) {
            boolean wasAdded = administration.addIfMin(((AddIfMinCommand) command).getStudyGroup(), credentials.username);
            if (wasAdded) {
                notifier.notifyAboutAddingGroup(((AddIfMinCommand)command).getStudyGroup(), senderAddress);
            }
            return new AddIfMinResponse(wasAdded);

        }
        if (command instanceof RemoveLowerCommand) {
            Set<StudyGroup> removedGroups = administration.removeLower(((RemoveLowerCommand) command).getStudyGroup(), credentials.username);
            Set<Long> removedIds = removedGroups.stream().map(StudyGroup::getId).collect(Collectors.toSet());
            if (!(removedGroups.isEmpty())) {
                notifier.notifyAboutRemovingGroup(removedIds, senderAddress);
            }
            return new RemoveLowerResponse(sorted(removedGroups));
        }
        if (command instanceof RemoveAllByStudentsCountCommand) {
            long count = ((RemoveAllByStudentsCountCommand) command).getCount();
            Set<StudyGroup> removedGroups = administration.removeAllByStudentsCount(count, credentials.username);
            Set<Long> removedIds = removedGroups.stream().map(StudyGroup::getId).collect(Collectors.toSet());
            if (!(removedGroups.isEmpty())) {
                notifier.notifyAboutRemovingGroup(removedIds, senderAddress);
            }
            return new RemoveAllByStudentsCountResponse(sorted(removedGroups));
        }
        if (command instanceof CountByGroupAdminCommand) {
            long count = administration.countByGroupAdmin(((CountByGroupAdminCommand) command).getGroupAdmin());
            return new CountByGroupAdminResponse(count);

        }
        if (command instanceof FilterLessThanSemesterEnumCommand) {
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
