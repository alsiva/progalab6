import commands.Command;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

public class CommandManager {
    private final Queue<Command> lastCommands = new LinkedList<>();

    public void add(Command command) {
        lastCommands.add(command);
        if (lastCommands.size() > 10) {
            lastCommands.remove();
        }
    }

    public List<Command> getLastCommands() {
        return new ArrayList<>(lastCommands);
    }
}
