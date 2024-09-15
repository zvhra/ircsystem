package client;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Date;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        System.out.println("******************************");
        System.out.println("    WELCOME TO CHAT CLIENT");
        System.out.println("******************************");
        System.out.println("\n");
        
        
        Scanner in = new Scanner(System.in);
        System.out.print("Enter IP Address: ");
        String ip = in.nextLine();
        System.out.print("Enter Port Number: ");
        int port = in.nextInt();
        try (Socket socket = new Socket(ip, port)) {
            PrintWriter output = new PrintWriter(socket.getOutputStream(), true);
            Scanner scanner = new Scanner(System.in);
            String msg;
            String clientName = "unknown";
            ClientRunnable clientRun = new ClientRunnable(socket);
            new Thread(clientRun).start();
            Thread.sleep(500);
            do {
                if (clientName.equals("unknown")) {
                    System.out.print("Enter your name\n> ");
                    msg = scanner.nextLine();
                    clientName = msg;
                    output.println(msg);
                    if (msg.equals("exit")) {
                        break;
                    }
                } 
                else {
                    System.out.println("Type Your Message <exit to quit>");
                    System.out.print("> ");
                    msg = scanner.nextLine();
                    if (msg.equals("exit")) {
                        output.println("exit");
                        break;
                    }
                    System.out.println("1. Broadcast");
                    System.out.println("2. Private");
                    int type = scanner.nextInt();
                    scanner.nextLine();
                    if(type == 1){
                        output.println(clientName+": " + msg+" ["+new Date().toString()+"]"+"$b");
                    }
                    else if(type == 2){
                        System.out.print("Enter Recevier ID\n> ");
                        String ID = scanner.nextLine();
                        output.println(clientName+": " + msg+" ["+new Date().toString()+"]"+"$"+ID+"$p");
                    }
                }
                System.out.println("");
            } while (!msg.equals("exit"));

        } catch (Exception e) {
            System.out.println("Error [Client]: " + e.getMessage());
            e.printStackTrace();
        }
    }
}
