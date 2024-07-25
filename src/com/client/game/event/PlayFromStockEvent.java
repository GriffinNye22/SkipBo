/**
 * com.client.game.event.PlayFromStockEvent
 * CSC 421 Fall 2020
 * @author Griffin Nye
 * Defines the PlayFromStockEvent
 */

package com.client.game.event;

import com.google.gwt.event.shared.GwtEvent;

public class PlayFromStockEvent extends GwtEvent<PlayFromStockEventHandler> {

	public static Type<PlayFromStockEventHandler> TYPE = new Type<PlayFromStockEventHandler>();
	
	/**
	 * Returns the associated Type for PlayFromStockEvent.
	 */
	@Override
	public Type<PlayFromStockEventHandler> getAssociatedType() {
		return TYPE;
	}//end getAssociatedType

	/**
	 * Calls PlayFromStockEventHandler's onGameLoad method passing it this event.
	 */
	@Override
	protected void dispatch(PlayFromStockEventHandler handler) {
		handler.onPlayFromStock(this);
	}//end dispatch

}//end PlayFromStockEvent

