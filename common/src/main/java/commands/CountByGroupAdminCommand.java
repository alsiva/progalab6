package commands;

import domain.Person;

public class CountByGroupAdminCommand {
    private Person groupAdmin;

    public CountByGroupAdminCommand(Person groupAdmin) {
        this.groupAdmin = groupAdmin;
    }

    public Person getGroupAdmin() {
        return groupAdmin;
    }
}
