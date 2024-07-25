/**
 * com.client.game.event.PlayFromHandEvent
 * CSC 421 Fall 2020
 * @author Griffin Nye
 * Defines the PlayFromHandEvent
 */

package com.client.game.event;

import com.google.gwt.event.shared.GwtEvent;

public class PlayFromHandEvent extends GwtEvent<PlayFromHandEventHandler> {

	public static Type<PlayFromHandEventHandler> TYPE = new Type<PlayFromHandEventHandler> ();

	/**
	 * Returns the associated Type for PlayFromHandEvent.
	 */
	@Override
	public Type<PlayFromHandEventHandler> getAssociatedType() {
		return TYPE;
	}//end getAssociatedType

	/**
	 * Calls PlayFromHandEventHandler's onGameLoad method passing it this event.
	 */
	@Override
	protected void dispatch(PlayFromHandEventHandler handler) {
		handler.onPlayFromHand(this);
	}//end dispatch

}//end PlayFromHandEvent
