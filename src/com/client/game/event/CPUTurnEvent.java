/**
 * com.client.game.event.CPUTurnEvent
 * CSC 421 Fall 2020
 * @author Griffin Nye
 * Defines the CPUTurnEvent
 */

package com.client.game.event;

import com.google.gwt.event.shared.GwtEvent;

public class CPUTurnEvent extends GwtEvent<CPUTurnEventHandler> {

	public static Type<CPUTurnEventHandler> TYPE = new Type<CPUTurnEventHandler>();
	
	/**
	 * Returns the associated Type for PlayFromDiscardEvent.
	 */
	@Override
	public Type<CPUTurnEventHandler> getAssociatedType() {
		return TYPE;
	}//end getAssociatedType

	/**
	 * Calls PlayFromDiscardEventHandler's onGameLoad method passing it this event.
	 */
	@Override
	protected void dispatch(CPUTurnEventHandler handler) {
		handler.onCPUTurn(this);
	}//end dispatch

}//end PlayFromDiscardEvent
