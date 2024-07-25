/**
 * com.client.model.Pile
 * CSC 421 Fall 2020
 * @author Griffin Nye
 * Abstract class for Pile objects
 */

package com.server.model;

abstract class Pile {
	/**
	 * Abstract method for retrieving the top card of the Pile.
	 * @return Card Card on top of the pile
	 */
	abstract Card getTop();
	
	/**
	 * Abstract method for getting the number of cards in the pile
	 * @return int number of cards in the pile
	 */
	abstract int getCount();

}//end Pile
