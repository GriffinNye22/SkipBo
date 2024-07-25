/**
 * com.client.game.event.CardDragEnterHandler
 * CSC421 Fall 2020
 * @author Griffin Nye
 * Custom Implementation of the DragEnterHandler for creating border highlight
 */

package com.client.game.event;

import com.google.gwt.event.dom.client.DragEnterEvent;
import com.google.gwt.event.dom.client.DragEnterHandler;
import com.google.gwt.user.client.ui.Image;


public class CardDragEnterHandler implements DragEnterHandler {

  /**
   * Adds border highlight when cursor enters source target during drag
   * @param event the event fired upon dragEnter 
   */
  public void onDragEnter(DragEnterEvent event) {
    event.preventDefault();

    Image src = (Image) event.getSource();
    src.addStyleName("dropTarget");
  }//end onDragEnter

}//end DragEnterHandler