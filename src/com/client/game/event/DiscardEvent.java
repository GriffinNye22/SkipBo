/**
 * com.client.game.event.DiscardEvent
 * CSC 421 Fall 2020
 * @author Griffin Nye
 * Defines the DiscardEvent
 */

package com.client.game.event;

import com.google.gwt.event.shared.GwtEvent;

public class DiscardEvent extends GwtEvent<DiscardEventHandler> {

	public static Type<DiscardEventHandler> TYPE = new Type<DiscardEventHandler>();
	
	/**
	 * Returns the associated Type for DiscardEvent.
	 */
	@Override
	public Type<DiscardEventHandler> getAssociatedType() {
		return TYPE;
	}//end getAssociatedType

	/**
	 * Calls DiscardEventHandler's onGameLoad method passing it this event.
	 */
	@Override
	protected void dispatch(DiscardEventHandler handler) {
		handler.onDiscard(this);
	}//end dispatch

}//end DiscardEvent
