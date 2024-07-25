/**
 * com.client.game.event.CPUTurnEventHandler
 * CSC 421 Fall 2020
 * @author Griffin Nye
 * Interface for the CPUTurnEventHandler
 */

package com.client.game.event;

import com.google.gwt.event.shared.EventHandler;

public interface CPUTurnEventHandler extends EventHandler {
	
	/**
	 * Handles the Computer's turn and Updates to the UI
	 * @param event The CPUTurnEvent
	 */
	void onCPUTurn(CPUTurnEvent event);
	
}//end PlayFromDiscardEventHandler
