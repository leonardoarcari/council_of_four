package core.connection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.gamemodel.bonus.Bonus;
import core.gamemodel.modelinterface.SellableItem;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 * Created by Leonardo Arcari on 20/05/2016.
 */
public class SocketConnection implements Connection, Runnable {
    private Socket socket;
    private InfoProcessor processor;
    private Communicator socketCommunicator;
    private BufferedReader in;
    private Gson gson;
    private Runnable onDisconnect;

    public SocketConnection(InfoProcessor processor, Socket socket) throws IOException {
        this.processor = processor;
        this.socket = socket;
        in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        gson = new GsonBuilder()
                .registerTypeAdapter(Bonus.class, new InterfaceAdapter<Bonus>())
                .registerTypeAdapter(SellableItem.class, new InterfaceAdapter<SellableItem>())
                .create();
        socketCommunicator = new SocketCommunicator(socket);
    }

    @Override
    public void setInfoProcessor(InfoProcessor processor) {
        this.processor = processor;
    }

    @Override
    public void sendInfo(Object info) {
        if (socketCommunicator != null) {
            socketCommunicator.sendInfo(info);
        }
    }

    @Override
    public void run() {
        try {
            while (true) {
                String info = in.readLine();
                if (info != null) {
                    if (!info.endsWith(SocketCommunicator.END_JSON)) {
                        in.reset();
                        System.out.println("SocketConnection: BufferedReader Reset");
                    } else {
                        StringTokenizer jsonTokenizer = new StringTokenizer(info, SocketCommunicator.END_JSON);
                        while (jsonTokenizer.hasMoreTokens()) {
                            String socketData = jsonTokenizer.nextToken();
                            //System.out.println(socketData);
                            StringTokenizer tokenizer = new StringTokenizer(socketData, SocketCommunicator.SEPARATOR);
                            String className = tokenizer.nextToken();
                            String json = tokenizer.nextToken();
                            //System.out.println(className + "\n" + json);
                            Object data = gson.fromJson(json, Class.forName(className));
                            processor.processInfo(data);
                        }
                    }
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("connection closed from the other side");
            if (onDisconnect != null) onDisconnect.run();
            try {
                socket.close();
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        } finally {
            try {
                socket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void setOnDisconnection(Runnable runnable) {
        onDisconnect = runnable;
    }
}
