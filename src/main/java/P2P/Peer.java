package P2P;

import javax.json.Json;
import javax.sound.midi.Soundbank;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringWriter;
import java.net.Socket;

public class Peer {
    public static void main(String[] args) {
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(System.in));

        System.out.print("Insert username and port number for this peer in the following format \"username port_number\": ");
        String[] setupValues = new String[0];
        try {
            setupValues = bufferedReader.readLine().split(" ");
        } catch (IOException e) {
            e.printStackTrace();
        }

        ServerThread serverThread = null;
        try {
            serverThread = new ServerThread(setupValues[1]);
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert serverThread != null;
        serverThread.start();

        new Peer().updateListenToPeers(bufferedReader, setupValues[0], serverThread);
    }

    public void updateListenToPeers(BufferedReader bufferedReader, String username, ServerThread serverThread)  {
        System.out.print("To connect to other peers insert their ip and port number" +
                "in the following format \"PeerIP:PortNumber ...\": ");
        String input = null;
        try {
            input = bufferedReader.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        assert input != null;
        String[] inputValues = input.split(" ");

        for (String inputValue : inputValues) {
            String[] address = inputValue.split(":");

            Socket socket = null;
            try {
                socket = new Socket(address[0], Integer.parseInt(address[1]));
                new PeerThread(socket).start();
            } catch (Exception e) {
                e.printStackTrace();
                if (socket != null) {
                    try {
                        socket.close();
                    } catch (IOException ex) {
                        e.printStackTrace();
                    }
                } else {
                    System.out.println("invalid input");
                }
            }

        }

        communicate(bufferedReader, username, serverThread);
    }

    public void communicate(BufferedReader bufferedReader, String username, ServerThread serverThread) {
        try {
            System.out.println("Now you can communicate with other peers (e to exit, c to change)\n" +
                    "- Insert \"exit\" to quit from the chat\n" +
                    "- Insert \"change\" to change the group chat");

            boolean flag = true;

            while (flag) {
                String message = bufferedReader.readLine();

                if (message.equals("exit")) {
                    flag = false;
                }
                else if (message.equals("change")) {
                    updateListenToPeers(bufferedReader, username, serverThread);
                }
                else {
                    StringWriter stringWriter = new StringWriter();
                    Json.createWriter(stringWriter).writeObject(Json.createObjectBuilder()
                                                                .add("username", username)
                                                                .add("message", message)
                                                                .build());
                    serverThread.sendMessage(stringWriter.toString());
                }
            }
            System.exit(0);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
