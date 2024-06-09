import java.net.Socket;
import java.net.UnknownHostException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * TCPClient class implements a TCP Client that connects to a TCP Server for communication of commands.
 */
public class TCPClient {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern
    ("yyyy-MM-dd HH:mm:ss.SSS");
    private static final Logger logger = Logger.getLogger(TCPClient.class.getName());

    /**
     * A Static initialization block to setup the logger for the TCP Client.
     */
    static {
        try {
            // Configure the logger with a file handler and a simple formatter
            FileHandler fileHandler = new FileHandler
            ("tcpclient.log", true); // Use append mode
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            System.err.println("Failed to set up logger: " + e.getMessage());
        }
    }

    /**
     * Main method for the TCP Client.
     * @param args Command Line Arguments to run the client: hostname and port number of the server.
     */
    public static void main(String[] args) {
        // Check for correct number of arguments to run the client
        if (args.length != 2) {
            printWithTimestamp("Sample Usage: java TCPClient <hostname> <port number>");
            return;
        }

        // Extract the hostname and port number from the command-line arguments
        String hostname = args[0];
        int port = Integer.parseInt(args[1]);
        int timeout = 5000; // Timeout for server (in milliseconds)

        try (Socket socket = new Socket(hostname, port)) {
            // Start the timeout 
            socket.setSoTimeout(timeout);

            // Setting up reading and writing streams
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            InputStream input = socket.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(input));

            // Setup input reader for reading commands from the console
            BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
            String text;

            // Prompt the user until 'exit' is entered
            while (true) {
                printWithTimestamp("Enter command: ");
                text = consoleReader.readLine();

                if (text.equalsIgnoreCase("exit")) {
                    break;
                }

                writer.println(text);

                try {
                    // Read the response sent by the server
                    String response = reader.readLine();
                    if (response == null) {
                        throw new IOException("No response from server");
                    }
                    printWithTimestamp("Server response: " + response);
                } catch (IOException e) {
                    // Handling the case when no response from the server is received
                    String errorMessage = "No response from server for command: " + text;
                    System.err.println("[" + LocalDateTime.now().format(formatter) + "] " + errorMessage);
                    logger.warning(errorMessage);
                }
            }

        } catch (UnknownHostException ex) {
            // Handling the case when the server is not found
            String errorMessage = "Server not found: " + ex.getMessage();
            System.err.println("[" + LocalDateTime.now().format(formatter) + "] " + errorMessage);
            logger.severe(errorMessage);
        } catch (IOException ex) {
            // Handling the case when the server is not found
            String errorMessage = "I/O error: " + ex.getMessage();
            System.err.println("[" + LocalDateTime.now().format(formatter) + "] " + errorMessage);
            logger.severe(errorMessage);
        }
    }

    /**
     * Helper method to print the message with a timestamp.
     * @param message The message to be printed along with the timestamp on the console.
     */
    private static void printWithTimestamp(String message) {
        System.out.println("[" + LocalDateTime.now().format(formatter) + "] " + message);
    }
}