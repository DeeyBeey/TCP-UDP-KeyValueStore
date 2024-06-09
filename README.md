# Distributed Systems Project

This project implements a single key-value store using both TCP and UDP communication protocols. The project includes the following components:

1. `CommandHandler` - Handles the key-value store operations.
2. `TCPServer` - TCP server implementation.
3. `TCPClient` - TCP client implementation.
4. `UDPServer` - UDP server implementation.
5. `UDPClient` - UDP client implementation.

## Prerequisites

Ensure that you have the Java Development Kit (JDK) installed on your system. You can download it from [here](https://www.oracle.com/java/technologies/javase-downloads.html).

## Compilation
>Note: Compiling again may cause loss of prepopulated data from the servers.

To compile the Java files, open a terminal or command prompt and navigate to the directory containing the source files. Use the following commands to compile each file:

```
javac CommandHandler.java TCPServer.java TCPClient.java UDPServer.java UDPClient.java
```

## Running the Servers
To start the TCP and UDP servers, use the following commands. Replace `<port>` with the port number you wish to use (e.g., 8080).

### TCP Server

```
java TCPServer <port>
```

### UDP Server

```
java UDPServer <port>
```

## Running the Clients
To start the TCP and UDP clients, use the following commands. Replace `<hostname>` with the server's hostname or IP address (e.g., localhost), and `<port>` with the same port number used for the server.

### TCP Client
``` 
java TCPClient <hostname> <port>
```

### UDP Client
```
java UDPClient <hostname> <port>
```

## Example Usage
>Note: As per the Assignment Description, the key value stores for both UDP and TCP have been pre-populated (5 PUTs, GETs and DELETEs) have been performed on them.

Here is an example of how to run the servers and clients using port `32000` and localhost as the hostname.

### Starting TCP Server
```
java TCPServer 32000
```

### Starting UDP Server
```
java UDPServer 32000
```

### Running TCP Client
```
java TCPClient localhost 32000
```

### Running UDP Client
```
java UDPClient localhost 32000
```

>Note: Ensure that the server is running first and use separate terminals or consoles for each client/server 

