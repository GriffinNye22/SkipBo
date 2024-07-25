/**
 * com.client.game.event.CardDragHandler
 * CSC421 Fall 2020
 * @author Griffin Nye
 * Custom Implementation of the DragStartHandler for Draggable cards
 */

package com.client.game.event;

import com.google.gwt.event.dom.client.DragStartEvent;
import com.google.gwt.event.dom.client.DragStartHandler;
import com.google.gwt.user.client.ui.Image;

public class CardDragHandler implements DragStartHandler {
  
	/**
	 * Runs when card begins to be dragged
	 * @param event the event fired upon drag
	 */
	public void onDragStart(DragStartEvent event) {
		Image src = (Image) event.getSource();
      
		event.setData("draggedImage", src.getElement().getId() );
	}//end onDragStart
  
}//end CardDragHandler