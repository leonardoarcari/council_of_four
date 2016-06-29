package core.connection;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import core.gamemodel.bonus.Bonus;
import core.gamemodel.modelinterface.SellableItem;

import java.io.BufferedReader;
import java.io.Closeable;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.StringTokenizer;

/**
 * A <code>SocketConnection</code> is a {@link Connection Connection} implemented using a standard TCP socket connection
 * where objects are serialized/de-serialized as JSON strings.
 */
public class SocketConnection implements Connection, Runnable, Closeable {
    private Socket socket;
    private InfoProcessor processor;
    private Communicator socketCommunicator;
    private BufferedReader in;
    private Gson gson;
    private Runnable onDisconnect;

    /**
     * Initializes a <code>SocketConnection</code> and the JSON serializer/de-serializer in order to work fine with
     * game model's interfaces.
     * @param processor InfoProcessor to ask for messages processing after de-serializing them
     * @param socket TCP socket
     * @throws IOException in case of errors in the socket input stream
     */
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

    /**
     * Delegates the job to the <code>InfoProcessor</code> passed in the constructor
     * @param info Object to send
     */
    @Override
    public void sendInfo(Object info) {
        if (socketCommunicator != null) {
            socketCommunicator.sendInfo(info);
        }
    }

    /**
     * Listen to the socket's input stream for incoming messages. JSON strings are read line by line and deserialized
     * using the class whose name is contained at the beginning of the string. Upon success, the de-serialized object is
     * sent to the InfoProcessor passed in the constructor.
     */
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
                } else {
                    System.out.println("Connection closed from the other side");
                    if (onDisconnect != null) onDisconnect.run();
                    break;
                }
            }
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Connection closed from the other side");
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

    /**
     * Closes the socket connection
     * @throws IOException
     */
    @Override
    public void close() throws IOException {
        socket.close();
    }
}
