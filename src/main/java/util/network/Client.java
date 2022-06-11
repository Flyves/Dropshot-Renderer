package util.network;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class Client {
    public static Socket socket;

    public static void start() throws IOException {
        socket = new Socket("127.0.0.1", 5000);
        System.out.println("Started a new connection");
    }

    public static void send(final String data) {
        try {
            PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
            out.print(data);
            out.flush();
        } catch (IOException e) {
            try {
                Client.start();
            } catch (IOException ex) {
                throw new RuntimeException("Client.send: couldn't reconnect");
            }
        }
    }

    public static String sendAndReceive(final String data) {
        send(data);

        try {
            Scanner in = new Scanner(socket.getInputStream());
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(in.next());
            return stringBuilder.toString();
        } catch (Exception e) {
            try {
                Client.start();
                return sendAndReceive(data);
            } catch (Exception ex) {
                throw new RuntimeException("Client.sendAndReceive: couldn't reconnect");
            }
        }
    }
}
