import java.net.ServerSocket;
import java.net.Socket;
import java.net.InetAddress;
import java.io.IOException;
import java.io.InputStream;
import java.io.BufferedReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.InputStreamReader;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * TCPServer class is a simple TCP Server listening to clients and handling requests 
 * using a CommandHandler.
 */
public class TCPServer {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern
    ("yyyy-MM-dd HH:mm:ss.SSS");
    private static final Logger logger = Logger.getLogger(TCPServer.class.getName());

    /**
     * A Static initialization block to setup the logger for the TCP Server.
     */
    static {
        try {
            // Configure the logger with a file handler and a simple formatter
            FileHandler fileHandler = new FileHandler
            ("tcpserver.log", true); // Use append mode
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            System.err.println("Failed to set up logger: " + e.getMessage());
        }
    }

    /**
     * Main method for the TCP Server.
     * @param args Command Line Arguments to run the server: port number of the server.
     */
    public static void main(String[] args) {
        // Check for correct number of arguments to run the server
        if (args.length != 1) {
            printWithTimestamp("Sample Usage: java TCPServer <port number>");
            return;
        }

        // Extract the port number from the command-line arguments
        int port = Integer.parseInt(args[0]);
        CommandHandler commandHandler = new CommandHandler();

        try (ServerSocket serverSocket = new ServerSocket(port)) {
            printWithTimestamp("Server is listening on port " + port);

            while (true) {
                // Accepting a new client connection
                Socket socket = serverSocket.accept();
                // Starting a new thread to handle client requests
                new Thread(() -> handleClient(socket, commandHandler)).start();
            }

        } catch (IOException ex) {
            printWithTimestamp("Server exception: " + ex.getMessage());
            logger.severe("Server exception: " + ex.getMessage());
        }
    }

    /**
     * Handles client requests.
     * @param socket The socket used for communicating with the client.
     * @param commandHandler CommandHandler object to process the client commands.
     */

    private static void handleClient(Socket socket, CommandHandler commandHandler) {
        try (InputStream input = socket.getInputStream();
             BufferedReader reader = new BufferedReader(new InputStreamReader(input));
             OutputStream output = socket.getOutputStream();
             PrintWriter writer = new PrintWriter(output, true)) {

            InetAddress clientAddress = socket.getInetAddress();
            int clientPort = socket.getPort();

            String text;
            while ((text = reader.readLine()) != null) {
                // Log all the recived commands from the client onto console as well as log file
                printWithTimestamp("Received from " + clientAddress + ":" + clientPort + " - " + text);
                logger.info("Received from " + clientAddress + ":" + clientPort + " - " + text);

                // Split the received commands into commands and arguments
                String[] textParts = text.split(" ", 2);
                String command = textParts[0];
                String[] commandArgs = textParts.length > 1 ? textParts[1].split
                (" ", 2) : new String[]{};

                // Processing the command and getting the response
                String response = commandHandler.handleCommand(command, commandArgs);

                // Logging server response to the client
                printWithTimestamp("Response to " + clientAddress + ":" + clientPort + " - " + response);
                logger.info("Response to " + clientAddress + ":" + clientPort + " - " + response);

                writer.println(response);
            }

        } catch (IOException ex) {
            printWithTimestamp("Server exception: " + ex.getMessage());
            logger.severe("Server exception: " + ex.getMessage());
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                printWithTimestamp("Failed to close client socket: " + e.getMessage());
                logger.severe("Failed to close client socket: " + e.getMessage());
            }
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