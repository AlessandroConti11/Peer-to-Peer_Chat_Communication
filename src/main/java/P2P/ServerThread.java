package P2P;


import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;


/**
 * The ServerThread class is responsible for managing the server in a peer-to-peer network.
 */
public class ServerThread extends Thread {
    /**
     * The server socket used to listen for incoming client connections.
     */
    private ServerSocket serverSocket;
    /**
     * A set of ServerThreadThread objects representing the connected client threads.
     */
    private Set<ServerThreadThread> serverThreadThreads = new HashSet<>();


    /**
     * Server thread constructor.
     *
     * @param portNumber the port number.
     */
    public ServerThread (String portNumber) {
        try {
            serverSocket = new ServerSocket(Integer.parseInt(portNumber));
        } catch (IOException e) {
            //constructor error
            System.exit(1);
        }
    }


    /**
     * Gets the serverThread threads.
     *
     * @return the ServerThreadThreads.
     */
    public Set<ServerThreadThread> getServerThreadThreads() {
        return serverThreadThreads;
    }


    /**
     * Overrides the run() method of the Thread class.
     * It continuously listens for incoming client connections, creates a new ServerThreadThread for each connection, and adds it to the set of connected threads.
     */
    @Override
    public void run() {
        try {
            while (true) {
                //creation of serverThread thread
                ServerThreadThread serverThreadThread = new ServerThreadThread(serverSocket.accept(), this);

                //add the new thread to serverThread
                serverThreadThreads.add(serverThreadThread);
                serverThreadThread.start();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    /**
     * Sends the specified message to all connected clients by iterating over the set of connected threads.
     *
     * @param message the message to send.
     */
    public void sendMessage(String message) {
        try {
            //send message to all peers connected with this one
            serverThreadThreads.forEach(t ->
                    t.getPrintWriter().println(message));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
