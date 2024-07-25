/**
 * com.client.game.event.PlayFromStockEventHandler
 * CSC 421 Fall 2020
 * @author Griffin Nye
 * Interface for the PlayFromStockEventHandler
 */

package com.client.game.event;

import com.google.gwt.event.shared.EventHandler;

public interface PlayFromStockEventHandler extends EventHandler {

	/**
	 * Handles the PlayFromStockEvent by updating the Stock, Scoreboard, and BuildPiles.
	 * @param event The PlayFromStockEvent
	 */
	void onPlayFromStock(PlayFromStockEvent event);
	
}//end PlayFromStockEventHandler

