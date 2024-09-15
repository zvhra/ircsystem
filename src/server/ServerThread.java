package server;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.ArrayList;

public class ServerThread extends Thread {
    
    private Socket socket;
    private String ID;
    private String name;
    private ArrayList<ServerThread> clients;
    private PrintWriter output;
    private ServerThread coordinator;
    private boolean coord = false;
    
    public ServerThread(Socket socket, ArrayList<ServerThread> clients, String ID) {
        this.socket = socket;
        this.clients = clients;
        this.ID = ID;
        this.name = "";
    }

    @Override
    public void run() {
        try {
            BufferedReader input = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            output = new PrintWriter(socket.getOutputStream(), true);
            broadcast(ID+" has joined the group!");
            broadcastUsersList(clients);
            while(true) {
                if(clients.size() == 1 && !coord){
                    coordinator = clients.get(0);
                    privateMessage(clients.get(0).ID, "You are now the coordinator.");
                    coord = true;
                }
                String msg = input.readLine();
                //System.out.println("Message: "+msg);
                
                if(msg.equals("exit")) {
                    broadcast(this.ID + " left!");
                    if(this.ID.equals(coordinator.ID)){
                        removeClient(this.ID);
                        coord = false;
                        if(clients.size() > 0){
                            coordinator = clients.get(0);   
                            privateMessage(clients.get(0).ID, "You are now the coordinator.");
                        }
                    }
                    break;
                }
                else if(msg.endsWith("b")){
                    msg = msg.replace("$b", "");
                    msg = msg.trim();
                    broadcast(msg);
                }
                else if(msg.endsWith("p")){
                    String[] s = msg.split("\\$");
                    msg = s[0];
                    String recID = s[1];
                    msg = msg.trim();
                    privateMessage(recID, msg);
                }
                else{
                    this.name = msg;
                }
            }
        } catch (Exception e) {
            
        }
    }

    private void broadcast(String msg) {
        for(ServerThread c: clients) {
            if(!c.ID.equals(this.ID)){
                c.output.println(msg);
            }
        }
    }

    private void removeClient(String ID) {
        for (int a = 0; a < clients.size(); a++) {
            if(clients.get(a).ID.equals(ID)){
                clients.remove(a);
                return;
            }
        }
    }
    
    private void privateMessage(String recID, String msg) {
        for(ServerThread c: clients) {
            if(c.ID.equals(recID)){
                c.output.println(msg);
                break;
            }
        }
    }

    private void broadcastUsersList(ArrayList<ServerThread> clients) {
        for(ServerThread c: clients) {
            c.output.println("Active Users: "+clients);
        }
    }
    
    @Override
    public String toString(){
        return ID;
    }
    
}
