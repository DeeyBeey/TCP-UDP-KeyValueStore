import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.logging.FileHandler;
import java.util.logging.Logger;
import java.util.logging.SimpleFormatter;

/**
 * UDPServer class is a simple UDP Server listening to clients and handling requests 
 * using a CommandHandler.
 */
public class UDPServer {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern
    ("yyyy-MM-dd HH:mm:ss.SSS");
    private static final Logger logger = Logger.getLogger(UDPServer.class.getName());

    /**
     * A Static initialization block to setup the logger for the UDP Server.
     */
    static {
        try {
            // Configure the logger with a file handler and a simple formatter
            FileHandler fileHandler = new FileHandler
            ("udpserver.log", true); // Use append mode
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setUseParentHandlers(false);
        } catch (IOException e) {
            System.err.println("Failed to set up logger: " + e.getMessage());
        }
    }

    /**
     * Main method for the UDP Server.
     * @param args Command Line Arguments to run the server: port number of the server.
     */
    public static void main(String[] args) {
        // Check for correct number of arguments to run the server
        if (args.length != 1) {
            printWithTimestamp("Sample Usage: java UDPServer <port number>");
            return;
        }

        // Extract the port number from the command-line arguments
        int port = Integer.parseInt(args[0]);
        CommandHandler commandHandler = new CommandHandler();

        try (DatagramSocket socket = new DatagramSocket(port)) {
            byte[] buffer = new byte[1024];

            printWithTimestamp("Server is listening on port " + port);

            while (true) {
                DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
                try {
                    // Accept packets from the client
                    socket.receive(packet);

                    // Convert packet data into string
                    String received = new String(packet.getData(), 0, packet.getLength());

                    // Log the request received by the client into log file
                    printWithTimestamp("Received from " + packet.getAddress() + ":" 
                    + packet.getPort() + " - " + received);
                    logger.info("Received from " + packet.getAddress() + ":" 
                    + packet.getPort() + " - " + received);

                    // Split the recived request into commands and arguments
                    String[] textParts = received.split(" ", 2);
                    if (textParts.length < 1) {
                        throw new IllegalArgumentException("Malformed request");
                    }

                    // Extract the commands and arguments
                    String command = textParts[0];
                    String[] commandArgs = textParts.length > 1 ? 
                    textParts[1].split(" ", 2) : new String[]{};

                    // Process commands and get the response
                    String response = commandHandler.handleCommand(command, commandArgs);

                    // Log the response on the console and log file
                    printWithTimestamp("Response to " + packet.getAddress() + ":" 
                    + packet.getPort() + " - " + response);
                    logger.info("Response to " + packet.getAddress() + ":" 
                    + packet.getPort() + " - " + response);

                    // Sending the response back to the client
                    byte[] responseBytes = response.getBytes();
                    DatagramPacket responsePacket = new DatagramPacket
                    (responseBytes, responseBytes.length, packet.getAddress(), packet.getPort());
                    socket.send(responsePacket);

                } catch (IllegalArgumentException e) {
                    // Handling malformed datagram requests
                    String errorMsg = "Received malformed request of length " 
                    + packet.getLength() + " from " + packet.getAddress() + ":" + packet.getPort();
                    printWithTimestamp(errorMsg);
                    logger.warning(errorMsg);
                }
            }

        } catch (IOException ex) {
            // Handling I/O errors
            printWithTimestamp("Server exception: " + ex.getMessage());
            logger.severe("Server exception: " + ex.getMessage());
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