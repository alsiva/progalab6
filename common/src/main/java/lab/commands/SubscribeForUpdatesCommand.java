package lab.commands;

import java.io.Serializable;

public class SubscribeForUpdatesCommand implements Command, Serializable {

    public final boolean shouldSubscribe;

    public SubscribeForUpdatesCommand(boolean shouldSubscribe) {
        this.shouldSubscribe = shouldSubscribe;
    }

    @Override
    public String toPrint() {
        return shouldSubscribe ? "Subscribe for updates" : "Unsubscribe from updates";
    }
}
