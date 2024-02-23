package P2P;


import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;


/**
 * The ServerThreadThread class is a thread that handles the communication with a single client in a peer-to-peer network.
 */
public class ServerThreadThread extends Thread {
    /**
     * A reference to the ServerThread object that manages the server.
     */
    private final ServerThread serverThread;
    /**
     * The Socket object representing the connection with the client.
     */
    private final Socket socket;
    /**
     * The PrintWriter object used to send messages to the client.
     */
    private PrintWriter printWriter;


    /**
     * ServerThreadThread constructor.
     *
     * @param socket the socket.
     * @param serverThread the serverThread.
     */
    public ServerThreadThread (Socket socket, ServerThread serverThread) {
        this.serverThread = serverThread;
        this.socket = socket;
    }


    /**
     * Gets the printWriter.
     *
     * @return the printWriter.
     */
    public PrintWriter getPrintWriter() {
        return printWriter;
    }


    /**
     * Overrides the run() method of the Thread class.
     * It reads messages from the client using a BufferedReader and sends them to all other connected clients.
     */
    @Override
    public void run() {
        try {
            //reads message
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.printWriter = new PrintWriter(socket.getOutputStream(), true);

            //send message
            while (true) {
                serverThread.sendMessage(bufferedReader.readLine());
            }
        }
        catch (Exception e) {
            //if the peer is disconnected, remove it from the list of peers to send messages to
            serverThread.getServerThreadThreads().remove(this);
        }
    }
}
