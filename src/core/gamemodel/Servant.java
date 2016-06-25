package core.gamemodel;

import core.gamemodel.modelinterface.SellableItem;

import java.io.Serializable;

/**
 * This class acts as a marker class. Servant aren't really needed as objects
 * in the game: both the model and the player just need to know the number of owned
 * servants. But when entering the market phase a number is not enough and a object
 * representing the servant concept is needed, thus the creation of the Servant class.
 */
public class Servant implements SellableItem, Serializable{
}
