package P2P;


import javax.json.Json;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.Socket;


/**
 * The Peer class is responsible for creating and managing a peer in a peer-to-peer chat application.
 */
public class Peer {
    /**
     * Connects to other peers by prompting the user to enter their IP address and port number.
     *
     * @param bufferedReader the buffer reader used.
     * @param username the peer username.
     * @param serverThread the peer to send message.
     */
    public void updateListenToPeers(BufferedReader bufferedReader, String username, ServerThread serverThread)  {
        System.out.print("To connect to other peers insert their ip and port number" +
                "in the following format \"PeerIP:PortNumber ...\": ");

        //read all peer information
        String input = null;
        try {
            input = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }

        //peer division
        assert input != null;
        String[] inputValues = input.split(" ");

        //for each peer --> peer information division
        for (String inputValue : inputValues) {
            String[] address = inputValue.split(":");

            //creation socket per peer
            Socket socket = null;
            try {
                socket = new Socket(address[0], Integer.parseInt(address[1]));
                new PeerThread(socket).start();
            } catch (Exception e) {
                //close socket
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        e.printStackTrace();
                    }
                }
                else {
                    System.out.println("Invalid input");
                }
            }

        }

        //now it is possible communicate with the others peer
        communicate(bufferedReader, username, serverThread);
    }

    /**
     * Handles the communication with other peers.
     *
     * @param bufferedReader the buffer reader used.
     * @param username the peer username.
     * @param serverThread the peer to send message.
     */
    public void communicate(BufferedReader bufferedReader, String username, ServerThread serverThread) {
        try {
            System.out.println("""
                    Now you can communicate with other peers (e to exit, c to change)
                    - Insert "exit" to quit from the chat
                    - Insert "change" to change the group chat""");

            //peer wats to continue with chat
            boolean flag = true;

            //white true
            while (flag) {
                //reads peer input
                String message = bufferedReader.readLine();

                //peer wants to exit from chat
                if (message.equals("exit")) {
                    flag = false;
                }
                //peer wants to change chat
                else if (message.equals("change")) {
                    updateListenToPeers(bufferedReader, username, serverThread);
                }
                //peer wants to send message
                else {
                    StringWriter stringWriter = new StringWriter();
                    Json.createWriter(stringWriter).writeObject(Json.createObjectBuilder()
                                                                .add("username", username)
                                                                .add("message", message)
                                                                .build());
                    serverThread.sendMessage(stringWriter.toString());
                }
            }
            //peer correctly exited the chat
            System.exit(0);
        }
        catch (Exception e) {
            //error
            System.exit(1);
        }
    }

    /**
     * Peer main.
     *
     * @param args input args.
     */
    public static void main(String[] args) {
        //peer buffer reader
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Insert username and port number for this peer in the following format \"username port_number\": ");

        //username and port number inserted by the peer
        String[] setupValues = new String[0];
        try {
            setupValues = bufferedReader.readLine().split(" ");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //creation of server thread
        ServerThread serverThread;
        serverThread = new ServerThread(setupValues[1]);
        serverThread.start();

        //update peer list
        new Peer().updateListenToPeers(bufferedReader, setupValues[0], serverThread);
    }
}
