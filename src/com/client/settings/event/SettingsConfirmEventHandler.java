/**
 * com.client.settings.event.SettingsConfirmEventHandler
 * CSC 421 Fall 2020
 * @author Griffin Nye
 * Interface for the SettingsConfirmEventHandler
 */

package com.client.settings.event;

import com.google.gwt.event.shared.EventHandler;

public interface SettingsConfirmEventHandler extends EventHandler {

	/**
	 * Handles the SettingsConfirmEvent by storing input values, creating new instance of Game, and Displaying the Game page.
	 * @param event The SettingsConfirmEvent
	 */
	void onSettingsConfirm(SettingsConfirmEvent event);
	
}//end SettingsConfirmEventHandler