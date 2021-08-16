package lab;

import java.util.HashMap;
import java.util.Map;

public class Configuration {
    public final String username;
    public final String password;
    public final String host;
    public final String port;
    public final String database;

    public Configuration(String username, String password, String host, String port, String database) {
        this.username = username;
        this.password = password;
        this.host = host;
        this.port = port;
        this.database = database;
    }

    public static Configuration readConfiguration(String[] args) throws ConfigurationReadException {
        Map<String, String> keyToValue = new HashMap<>();
        for (String arg : args) {
            String[] keyAndValue = arg.split("=");
            if (keyAndValue.length < 2) {
                System.err.println("Argument '" + arg + "' is not in key=value form");
            }
            keyToValue.put(keyAndValue[0], keyAndValue[1]);
        }

        String username = keyToValue.get("username");
        if (username == null) {
            throw new ConfigurationReadException("username");
        }
        String password = keyToValue.get("password");
        if (password == null) {
            throw new ConfigurationReadException("password");
        }

        String host = keyToValue.get("host");
        if (host == null) {
            host = "localhost";
        }

        String port = keyToValue.get("port");
        if (port == null) {
            port = "5432";
        }

        String database = keyToValue.get("database");
        if (database == null) {
            database = "studs";
        }

        return new Configuration(username, password, host, port, database);
    }

    public static class ConfigurationReadException extends Exception {
        public ConfigurationReadException(String key) {
            super("Missing field " + key);
        }
    }
}
