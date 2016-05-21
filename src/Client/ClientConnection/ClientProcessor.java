package Client.ClientConnection;


import Client.View;
import Core.Connection.InfoProcessor;
import Core.ModelInterface;

import java.util.Arrays;

/**
 * Created by Leonardo Arcari on 20/05/2016.
 */
public class ClientProcessor implements InfoProcessor {
    private View view;

    public ClientProcessor(View view) {
        this.view = view;
    }

    @Override
    public void processInfo(Object info) {
        Class<?> objClass = info.getClass();
        if (Arrays.asList(objClass.getInterfaces()).contains(ModelInterface.class)) {
            ModelInterface model = (ModelInterface) info;
            view.print(model);
        }
    }
}
