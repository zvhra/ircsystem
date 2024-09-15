
package server;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.net.Socket;

public class Server {

    private static int nextID = 100;
    public static void main(String[] args) {
        ArrayList<ServerThread> clients = new ArrayList<>();
        try (ServerSocket ss = new ServerSocket(1000)){
            while(true) {
                Socket socket = ss.accept();
                String ID = "C"+nextID;
                nextID++;
                ServerThread serverThread = new ServerThread(socket, clients, ID);
                clients.add(serverThread); 
                serverThread.start();
                System.out.println("Server Started!");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}