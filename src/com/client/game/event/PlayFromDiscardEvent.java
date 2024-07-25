/**
 * com.client.game.event.PlayFromDiscardEvent
 * CSC 421 Fall 2020
 * @author Griffin Nye
 * Defines the PlayFromDiscardEvent
 */

package com.client.game.event;

import com.google.gwt.event.shared.GwtEvent;

public class PlayFromDiscardEvent extends GwtEvent<PlayFromDiscardEventHandler> {

	public static Type<PlayFromDiscardEventHandler> TYPE = new Type<PlayFromDiscardEventHandler>();
	
	/**
	 * Returns the associated Type for PlayFromDiscardEvent.
	 */
	@Override
	public Type<PlayFromDiscardEventHandler> getAssociatedType() {
		return TYPE;
	}//end getAssociatedType

	/**
	 * Calls PlayFromDiscardEventHandler's onGameLoad method passing it this event.
	 */
	@Override
	protected void dispatch(PlayFromDiscardEventHandler handler) {
		handler.onPlayFromDiscard(this);
	}//end dispatch

}//end PlayFromDiscardEvent