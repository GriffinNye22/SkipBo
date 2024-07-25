/**
 * com.client.game.event.PlayFromDiscardEventHandler
 * CSC 421 Fall 2020
 * @author Griffin Nye
 * Interface for the PlayFromDiscardEventHandler
 */

package com.client.game.event;

import com.google.gwt.event.shared.EventHandler;

public interface PlayFromDiscardEventHandler extends EventHandler {

	/**
	 * Handles the PlayFromDiscardEvent by Updating the DiscardPiles and BuildPiles
	 * @param event The PlayFromDiscardEvent
	 */
	void onPlayFromDiscard(PlayFromDiscardEvent event);
	
}//end PlayFromDiscardEventHandler
