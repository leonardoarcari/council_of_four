package client.View.gui;

import javafx.beans.property.BooleanProperty;

/**
 *  A <code>HasMainAction</code is a class that lets the user do a main action.
 */
public interface HasMainAction {
    /**
     * Registers whether the player can do a main action or not (because he has already done it/them in the current turn)
     * @param mainActionAvailable <code>True</code> if the player can send a main action. <code>False otherwise</code>.
     */
    void setDisableBindingMainAction(BooleanProperty mainActionAvailable);
}
