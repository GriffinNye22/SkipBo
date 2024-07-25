/**
 * com.client.settings.WaitForHostPresenter
 * CSC 421 Fall 2020
 * @author Griffin Nye
 * Handles events from the WaitForHostView and Presents the view
 */

package com.client.settings;

import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class WaitForHostPresenter {

	//Display Interface Linking Presenter to View
	public interface Display {
		WaitForHostView getViewInstance();
		Widget asWidget();
	}//end Display
	
	final Display view;
	
	/**
	 * Constructs the WaitForHostView
	 * @param waitForHostView Interface for the WaitForHostView
	 */
	public WaitForHostPresenter(Display waitForHostView) {
		this.view = waitForHostView;
	}//end WaitForHostPresenter
	
	/**
	 * Presents the WaitForHostView by placing it in the provided container.
	 * @param container The container for the view. (RootPanel in this case)
	 */
	public void display(HasWidgets container) {
		
		//Clear container
		container.clear();
		
		//Style container
	  ((UIObject) container).setStyleName("container");
	  
	  //Add view to container
	  container.add( view.asWidget() );
	}//end display
	
}//end WaitForHostPresenter
