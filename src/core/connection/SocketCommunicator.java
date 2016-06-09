package core.connection;

import core.Player;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

/**
 * Created by Leonardo Arcari on 20/05/2016.
 */
public class SocketCommunicator implements Communicator {
    private ObjectOutputStream out;
    private Socket socket;


    public SocketCommunicator(Socket socket) throws IOException {
        this.socket = socket;
        out = new ObjectOutputStream(socket.getOutputStream());
    }

    @Override
    public void sendInfo(Object info) {
        try {
            if (info instanceof Player) {
                Player p = (Player) info;
                System.out.println(p.getUniqueID() + " " + p.getColor() + " " + p.getUsername() + " " + p.getNickname
                        ());
            }
            out.writeUnshared(info);
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
