/**
 * com.client.settings.event.SettingsConfirmEvent
 * CSC 421 Fall 2020
 * @author Griffin Nye
 * Defines the SettingsConfirmEvent
 */

package com.client.settings.event;

import com.google.gwt.event.shared.GwtEvent;

public class SettingsConfirmEvent extends GwtEvent<SettingsConfirmEventHandler> {

	public static Type<SettingsConfirmEventHandler> TYPE = new Type<SettingsConfirmEventHandler> ();
	
	/**
	 * Returns the associated Type for SettingsConfirmEvent
	 */
	@Override
	public Type<SettingsConfirmEventHandler> getAssociatedType() {
		return TYPE;
	}//end getAssociatedType

	/**
	 * Calls SettingsConfirmEventHandler's onPlayClick method passing it this Event
	 */
	@Override
	protected void dispatch(SettingsConfirmEventHandler handler) {
		handler.onSettingsConfirm(this);
	}//end dispatch

}//end SettingsConfirmEvent