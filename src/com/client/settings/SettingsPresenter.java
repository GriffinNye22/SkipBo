/**
 * com.client.settings.SettingsPresenter
 * CSC421 Fall 2020
 * @author Griffin Nye
 * Handles events from the SettingsView and Presents the View.
 */

package com.client.settings;

import java.util.ArrayList;

import com.client.settings.event.SettingsConfirmEvent;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.HasClickHandlers;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.ui.HasWidgets;
import com.google.gwt.user.client.ui.ListBox;
import com.google.gwt.user.client.ui.UIObject;
import com.google.gwt.user.client.ui.Widget;

public class SettingsPresenter {
	
	//Display Interface Linking Presenter to View
	public interface Display {
		
		HasClickHandlers getPlayButton();
		
		ListBox getCPUListBox();
		ListBox getPlayerListBox();
	
		SettingsView getViewInstance();
		
		String getCPUValue();
		String getPlayerName();
		String getPlayerValue();
		String getPointValue();
		String getStockValue();
	
		Widget asWidget();
	}//end Display
	
	final HandlerManager eventBus;
	final Display view;
	
	/**
	 * Constructs the Settings Presenter 
	 * @param settingsView Interface for the SettingsView
	 * @param eventBus The EventBus for the application
	 */
	public SettingsPresenter(Display settingsView, HandlerManager eventBus) {
		this.eventBus = eventBus;
		this.view = settingsView;
	}//end constructor
	
	/**
	 * Binds the EventHandlers 
	 */
	public void bindEvents() {
		
		//Add and Implement the Play Button's ClickHandler
		view.getPlayButton().addClickHandler(new ClickHandler() {
			
			/**
			 * Handles ClickEvent for Play Button. Stores selected settings for creating the Game object.
			 */
			public void onClick(ClickEvent event) {
        
				//Fire SettingsConfirmEvent to handle this event in GameController
        eventBus.fireEvent( new SettingsConfirmEvent() );
      
			}//end onClick
		});//end ClickHandler
		
		//Add and Implement the playerList ListBox's ChangeHandler
		view.getPlayerListBox().addChangeHandler(new ChangeHandler() {
			
			public void onChange(ChangeEvent event) {
				
				//Clear and update cpuList
        view.getCPUListBox().clear();
        
        //Add corresponding options for number of CPU Players (0 to # of Players - 1)
        for(int i = 0; i < Integer.parseInt( view.getPlayerValue() ); i++) {
          view.getCPUListBox().addItem(Integer.toString(i));
        }//end for
        
			}//end onChange
		});//end ChangeHandler
		
	}//end bindEvents
	
	/**
	 * Presents the SettingsView by placing it in the provided container.
	 * @param container The container for the view. (RootPanel in this case)
	 */
	public void display(HasWidgets container) {
		
		//Clear container
		container.clear();
		
		//Style container
		((UIObject) container).setStyleName("container");
		
		//Add view to container
		container.add( view.asWidget() );
		
		//Bind the view events to the Controller
		bindEvents();
		
	}//end display
	
	/**
	 * Returns the view from this presenter.
	 * @return Display The view
	 */
	public Display getView() {
		return view;
	}//end getView
	
}//end SettingsPresenter
