package client.View;

import javafx.collections.ListChangeListener;

/**
 * Created by Matteo on 06/06/16.
 */
public interface PathViewInterface {
    void addListener(ListChangeListener<? super ObjectImageView> listener);
}
