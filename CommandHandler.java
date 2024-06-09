import java.util.HashMap;

/**
 * CommandHandler class is an easy to use Command-Line Interface for managing 
 * the key-value pairs for both TCP and UDP Clients.
 */
public class CommandHandler {

    /**
     * keyValueStore is the HashMap that stores the key-value pairs.
     */
    private HashMap<String, String> keyValueStore;

    /**
     * A new CommandHandler is constructed with an empty keyValueStore.
     */
    public CommandHandler() {
        this.keyValueStore = new HashMap<>();
    }

    /**
     * 
     * @param command The command to be execute (PUT, GET, DELETE).
     * @param args Arguments for the command.
     * @return A message depicting the result of the operation.
     */
    public String handleCommand(String command, String[] args) {
        switch (command) {
            case "PUT":
                return put(args);
            case "GET":
                return get(args);
            case "DELETE":
                return delete(args);
            default:
                return "Invalid Command.";
        }
    }

    /**
     * Puts a key-value pair into the keyValueStore.
     * @param args Arguments containing the key and value to be inserted.
     * @return A message depicting success or failure of the operation.
     */
    private String put(String[] args) {
        if (args.length < 2) return "Sample Usage: PUT <key> <value>";
        String key = args[0];
        String value = args[1];
        keyValueStore.put(key, value);
        return "Operation successful.";
    }

    /**
     * Fetches a value for a given key from the keyValueStore.
     * @param args Argument containing the key for which the value must be retrieved.
     * @return Value for the associated key or an error message if key does not exist.
     */
    private String get(String[] args) {
        if (args.length < 1) return "Sample Usage: GET <key>";
        String key = args[0];
        String value = keyValueStore.get(key);
        return value != null ? value : "No record found.";
    }

    /**
     * Deletes a key-value pair record from the keyValueStore.
     * @param args Argument containing the key which must be deleted. 
     * @return A message depicting success or failure of the operation.
     */
    private String delete(String[] args) {
        if (args.length < 1) return "Sample Usage: DELETE <key>";
        String key = args[0];
        keyValueStore.remove(key);
        return "Operation successful.";
    }
}