package lab.commands;

import lab.domain.Person;

import java.io.Serializable;

public class CountByGroupAdminCommand implements Serializable, Command {
    private final Person groupAdmin;

    public CountByGroupAdminCommand(Person groupAdmin) {
        this.groupAdmin = groupAdmin;
    }

    public Person getGroupAdmin() {
        return groupAdmin;
    }

    @Override
    public String toPrint() {
        return "Count number of groups with admin " + groupAdmin.getName();
    }
}
