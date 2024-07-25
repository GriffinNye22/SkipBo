/**
 * com.client.SkipBov2
 * CSC421 Fall 2020
 * @author Griffin Nye
 * EntryPoint for the SkipBov2 webapp
 */

package com.client;

import com.google.gwt.core.client.EntryPoint;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.RootPanel;

public class SkipBov3 implements EntryPoint {
	/**
	 * Executes on Load of SkipBov2. More specifically, creates the EventBus and 
	 * GameController and passes control to the Game Controller.
	 */
	@Override
	public void onModuleLoad() {
		HandlerManager eventBus = new HandlerManager(null);
		GameController game = new GameController(eventBus);
		game.launch( RootPanel.get("SkipBov2") );
	}//end onModuleLoad

}//end SkipBov2
