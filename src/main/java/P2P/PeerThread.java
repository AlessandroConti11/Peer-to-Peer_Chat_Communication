package P2P;


import javax.json.Json;
import javax.json.JsonObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;


/**
 * The PeerThread class is a subclass of Thread that is responsible for handling communication with a peer in a peer-to-peer network.
 */
public class PeerThread extends Thread{
    /**
     * A BufferedReader object used to read JSON messages from the peer.
     */
    private BufferedReader bufferedReader;


    /**
     * PeerThread constructor.
     *
     * @param socket the socket.
     */
    public PeerThread(Socket socket) {
        try {
            bufferedReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * Overrides the run() method of the Thread class.
     * It reads JSON messages from the bufferedReader and prints them to the console if they contain a username and a message.
     */
    @Override
    public void run() {
        //the computation is running correctly or not
        boolean flag = true;

        while (flag) {
            try {
                //reads JSON that contains the message
                JsonObject jsonObject = Json.createReader(bufferedReader).readObject();

                if (jsonObject.containsKey("username")){
                    System.out.println("[" + jsonObject.getString("username") + "]: " + jsonObject.getString("message"));
                }
            }
            catch (Exception e) {
                flag = false;
                interrupt();
            }
        }
    }
}
