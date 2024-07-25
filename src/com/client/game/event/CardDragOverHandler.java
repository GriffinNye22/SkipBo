/**
 * com.client.game.event.CardDragOverHandler
 * CSC421 Fall 2020
 * @author Griffin Nye
 * Custom Implementation of the DragOverHandler (must be implemented to allow DropHandler to function properly)
 */

package com.client.game.event;

import com.google.gwt.event.dom.client.DragOverEvent;
import com.google.gwt.event.dom.client.DragOverHandler;

public class CardDragOverHandler implements DragOverHandler {

	/**
	 * Simply prevents the default action 
	 * @param event fired upon drag over
	 */
  public void onDragOver(DragOverEvent event) {
    event.preventDefault();
  }//end onDragOver

}//end DragOverHandler