/**
 * com.client.game.event.DiscardEventHandler
 * CSC 421 Fall 2020
 * @author Griffin Nye
 * Interface for the DiscardEventHandler
 */

package com.client.game.event;

import com.google.gwt.event.shared.EventHandler;

public interface DiscardEventHandler extends EventHandler {

	/**
	 * Handles the DiscardEvent by ending the turn and rotating the players
	 * @param event The DiscardEvent
	 */
	void onDiscard(DiscardEvent event);
	
}//end DiscardEventHandler
