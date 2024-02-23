package P2P;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.HashSet;
import java.util.Set;

public class ServerThread extends Thread{
    private ServerSocket serverSocket;
    private Set<ServerThreadThread> serverThreadThreads = new HashSet<ServerThreadThread>();


    public ServerThread (String portNumber) throws IOException {
        serverSocket = new ServerSocket(Integer.parseInt(portNumber));
    }


    public Set<ServerThreadThread> getServerThreadThreads() {
        return serverThreadThreads;
    }


    @Override
    public void run() {
        try {
            while (true) {
                ServerThreadThread serverThreadThread = new ServerThreadThread(serverSocket.accept(), this);

                serverThreadThreads.add(serverThreadThread);
                serverThreadThread.start();
            }
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void sendMessage(String message) {
        try {
            System.out.println("stt: " + serverThreadThreads);
            serverThreadThreads.forEach(t ->
                    t.getPrintWriter().println(message));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }
}
