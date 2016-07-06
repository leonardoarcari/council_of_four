package client.View.gui;

import javafx.collections.ListChangeListener;

/**
 * A <code>PathViewInterface</code> exposes a method common to all game paths (victory, wealth and nobility paths)
 */
public interface PathViewInterface {
    /**
     * Registers <code>listener</code> to be executed on a path model's object update
     * @param listener ListChangeListener to register
     */
    void addListener(ListChangeListener<? super ObjectImageView> listener);
}
