package server.serverconnection;

import core.Player;
import server.WaitingHall;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * A <code>SocketService</code> is a class that accept incoming TCP connections and sets a
 * {@link core.connection.Connection Connection} up representing the playing client on the server.
 */
public class SocketService implements Runnable {
    /**
     * Make a TCP passive open request and waits for incoming connections.
     * On an incoming connection request a socket is allocated and wrapped in a
     * {@link ServerSocketConnection ServerSocketConnection}, a newly allocated {@link Player Player} is set into it and
     * added to the waiting players' list in {@link WaitingHall WaitingHall}.
     *
     * In case of any network error accepting a socket thread is shutdown and the socket server closed.
     */
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
