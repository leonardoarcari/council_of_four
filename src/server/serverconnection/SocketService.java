package server.serverconnection;

import core.Player;
import server.WaitingHall;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Leonardo Arcari on 21/05/2016.
 */
public class SocketService implements Runnable {
    @Override
    public void run() {
        ExecutorService executor = Executors.newCachedThreadPool();
        ServerSocket serverSocket;
        try {
            serverSocket = new ServerSocket(2828);
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        System.out.println("server ready");
        while (true) {
            try {
                Socket socket = serverSocket.accept();
                ServerSocketConnection connection = new ServerSocketConnection(
                        WaitingHall.getInstance().getInfoProcessor(),
                        socket);
                Player player = new Player(connection);
                connection.setPlayer(player);
                executor.submit(connection);
                WaitingHall.getInstance().addPlayer(player);
            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        executor.shutdown();
        try {
            serverSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
    }
}
