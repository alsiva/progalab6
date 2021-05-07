package commands;

import domain.Person;

import java.io.Serializable;

public class CountByGroupAdminCommand implements Serializable, Command {
    private Person groupAdmin;

    public CountByGroupAdminCommand(Person groupAdmin) {
        this.groupAdmin = groupAdmin;
    }

    public Person getGroupAdmin() {
        return groupAdmin;
    }
}
