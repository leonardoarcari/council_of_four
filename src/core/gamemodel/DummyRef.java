package core.gamemodel;

import java.io.Serializable;

/**
 * Created by Matteo on 27/05/16.
 */
public class DummyRef implements Serializable {
    private int id = 0;
    public DummyRef() {
        id++;
    }

    public int getId() {
        return id;
    }
}
