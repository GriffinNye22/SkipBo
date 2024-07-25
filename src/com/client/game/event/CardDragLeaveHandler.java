/**
 * com.client.game.event.CardDragLeaveHandler
 * CSC421 Fall 2020
 * @author Griffin Nye
 * Custom Implementation of the DragLeaveHandler for removing border highlight
 */

package com.client.game.event;

import com.google.gwt.event.dom.client.DragLeaveEvent;
import com.google.gwt.event.dom.client.DragLeaveHandler;
import com.google.gwt.user.client.ui.Image;

public class CardDragLeaveHandler implements DragLeaveHandler {

/**
 * Removes border highlight when cursor leaves source target during drag
 * @param event fired upon drag leave 
 */
  public void onDragLeave(DragLeaveEvent event) {
    event.preventDefault();
        
    Image src = (Image) event.getSource();          
    src.removeStyleName("dropTarget");
  }//end onDragLeave

}//end CardDragLeaveHandler
