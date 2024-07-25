/**
 * com.client.game.event.PlayFromHandEventHandler
 * CSC 421 Fall 2020
 * @author Griffin Nye
 * Interface for the PlayFromHandEventHandler
 */

package com.client.game.event;

import com.google.gwt.event.shared.EventHandler;

public interface PlayFromHandEventHandler extends EventHandler {
	
	/**
	 * Handles the onPlayFromHandEvent by Updating the Hand and BuildPiles
	 * @param event The PlayFromHandEvent
	 */
	void onPlayFromHand(PlayFromHandEvent event);
	
}//end PlayFromHandEventHandler
