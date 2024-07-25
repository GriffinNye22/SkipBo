/**
 * com.client.settings.WaitForHostView
 * CSC421 Fall 2020
 * @author Griffin Nye
 * Creates the User Interface for the WaitForHostPage and implements the 
 * WaitForHostPresenter.Display interface
 */

package com.client.settings;

import com.google.gwt.user.client.ui.HasHorizontalAlignment;
import com.google.gwt.user.client.ui.HasVerticalAlignment;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.VerticalPanel;
import com.google.gwt.user.client.ui.Widget;

public class WaitForHostView implements WaitForHostPresenter.Display {

	private Label msg;
	private VerticalPanel msgPanel;
	
	/**
	 * Constructs the WaitForHostView object
	 */
	public WaitForHostView() {
		
		//Initialize components
		msgPanel = new VerticalPanel();
		msg = new Label("Waiting for Host to configure Game settings.");
		
		//Add message to panel
		msgPanel.add(msg);
		
		//Center message in panel
		msgPanel.setCellHorizontalAlignment(msg, HasHorizontalAlignment.ALIGN_CENTER);
		msgPanel.setCellVerticalAlignment(msg, HasVerticalAlignment.ALIGN_MIDDLE);
		
		//Style Message and its panel
		msg.setStyleName("message");
		msgPanel.setStyleName("messagePanel");
	}//end constructor

	/**
	 * Returns this view as a widget
	 */
	@Override
	public Widget asWidget() {
		return msgPanel;
	}//end asWidget
	
	/**
	 * Returns an instance of this view
	 */
	@Override
	public WaitForHostView getViewInstance() {
		return this;
	}//end getViewInstance
	
}//end WaitView
