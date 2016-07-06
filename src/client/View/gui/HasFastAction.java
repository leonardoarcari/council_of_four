package client.View.gui;

import javafx.beans.property.BooleanProperty;

/**
 * A <code>HasFastAction</code is a class that lets the user do a fast action.
 */
public interface HasFastAction {
    /**
     * Registers whether the player can do a fast action or not (because he has already done it in the current turn)
     * @param fastActionAvailable <code>True</code> if the player can send a fast action. <code>False otherwise</code>.
     */
    void setDisableBindingFastAction(BooleanProperty fastActionAvailable);
}
