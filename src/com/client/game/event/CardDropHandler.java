/**
 * com.client.game.event.CardDropHandler
 * CSC421 Fall 2020
 * @author Griffin Nye
 * Custom Implementation of the DropHandler for handling the transfer of cards
 */

package com.client.game.event;

import java.util.ArrayList;

import com.client.SkipBoException;
import com.client.game.GamePresenter;
import com.client.game.GamePresenter.Display;
import com.client.game.GameServiceAsync;
import com.google.gwt.event.dom.client.DropEvent;
import com.google.gwt.event.dom.client.DropHandler;
import com.google.gwt.event.shared.HandlerManager;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Image;
import com.server.model.Computer;
import com.server.model.Game;



public class CardDropHandler implements DropHandler {
  private ArrayList<Image> buildPiles;
  private Display view;
  private GameServiceAsync gameService;
  final private HandlerManager eventBus;
  private int thisPlayer;
  
  /**
   * Constructs the CardDropHandler
   * @param view Display Interface for the view
   * @param eventBus The eventBus for the handler
   * @param thisPlayer index of this client's associated player index
   * @param gameService instance of the GameServiceAsync interface
   */
  public CardDropHandler(Display view, HandlerManager eventBus, int thisPlayer, GameServiceAsync gameService) {
  	this.view = view;
  	this.eventBus = eventBus;
  	this.gameService = gameService;
  	this.thisPlayer = thisPlayer;
  	
  	this.buildPiles = view.getBuildPanel().getBuildPiles();
  }//end constructor
	
	
	/**
	 * Removes border highlight from source target, identifies source and dest targets and makes appropriate move
	 * @param event the event fired on drop
	 */
	public void onDrop(DropEvent event) {
		event.preventDefault();
		
    //Remove DragOverStyle
		Image src =  (Image) event.getSource();
		src.removeStyleName("dropTarget");
		
		//Get ID of draggedCard and dropTarget
		String draggedCard = event.getData("draggedImage");    
		String dropTarget = src.getElement().getId();
		
		int handIdx;
		int pileNum;
		
		switch( draggedCard.substring(0,4) ) {
		case "hand":
			handIdx = Integer.valueOf( draggedCard.substring(4) );
			
			//Discard Case
			if( dropTarget.substring(0,7).equals("discard") ) {
				
				pileNum = Integer.valueOf( dropTarget.substring(7) );
				
					//Callback for discard
					AsyncCallback<Void> discardCallback = new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							view.displayMessage("Error performing discard");
						}//end onFailure

						@Override
						public void onSuccess(Void result) {
							
							//Fire DiscardEvent
							eventBus.fireEvent(new DiscardEvent() );
						}//end onSuccess
					};//end discardCallback
					
					//Attempt discard
					gameService.discard(handIdx, pileNum, discardCallback); 
					
					//If new current player is a CPU, handle the Computer's turn.
					//if (SBGame.getPlayer(currPlayer) instanceof Computer) 
//					{
//						eventBus.fireEvent(new CPUTurnEvent() );
//					}//end if
				
				//Play From Hand Case
			} else if( dropTarget.substring(0,5).equals("build") ) {
				
					//Callback for makeHandMove
					AsyncCallback<Void> handMoveCallback = new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							view.displayMessage(caught.getMessage() );
						}//end onFailure

						@Override
						public void onSuccess(Void result) {
							
							//Fire PlayFromHandEvent
							eventBus.fireEvent(new PlayFromHandEvent() );
						}//end onSuccess
					};//end discardCallback
					
					//Attempt Hand move
					gameService.makeHandMove(handIdx, buildPiles.indexOf(src), handMoveCallback);     	
			}//end if
			
			break;
		case "stoc":
			
			//Play From Stock Case
			if( dropTarget.substring(0,5).equals("build") ) {  
				
					//Callback for makeStockMove
					AsyncCallback<Void> stockMoveCallback = new AsyncCallback<Void>() {

						@Override
						public void onFailure(Throwable caught) {
							view.displayMessage(caught.getMessage() );
						}//end onFailure

						@Override
						public void onSuccess(Void result) {
							
							//Fire PlayFromStockEvent
							eventBus.fireEvent(new PlayFromStockEvent() );
						}//end onSuccess
					};//end discardCallback
					
					//Attempt Stock move
					gameService.makeStockMove( buildPiles.indexOf(src), stockMoveCallback ); 
			}//end if
			
			break;
		case "disc":
			
			//Play from Discard case
			pileNum = Integer.valueOf( draggedCard.substring(7) );
			
			if( dropTarget.substring(0,5).equals("build") ) {
				
				//Callback for makeDiscardMove
				AsyncCallback<Void> discardMoveCallback = new AsyncCallback<Void>() {

					@Override
					public void onFailure(Throwable caught) {
						view.displayMessage( caught.getMessage() );
					}//end onFailure

					@Override
					public void onSuccess(Void result) {
						
						//Fire PlayFromDiscardEvent
						eventBus.fireEvent(new PlayFromDiscardEvent() );
					}//end onSuccess
				};//end discardMoveCallback
					
				//Attempt DiscardMove
				gameService.makeDiscardMove(pileNum, buildPiles.indexOf(src), discardMoveCallback);
			}//end if	
			
			break;	
		}//end switch
	}//end onDrop	
}//end CardDropHandler