/**
 * com.server.GameServiceImpl
 * CSC421 Fall 2020
 * @author Griffin Nye
 * Server-side implementation of the GameService. 
 * Handles the transmission of game data.
 */

package com.server;

import java.util.ArrayList;
import java.util.Collections;

import com.client.SkipBoException;
import com.client.game.GameService;
import com.client.game.event.EventType;
import com.google.gwt.user.client.Timer;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.server.model.Game;
import com.server.model.Player;

public class GameServiceImpl extends RemoteServiceServlet implements GameService {	
	private Boolean eventFired = false;
	private EventType lastEvent;
	private int eventsReceivedCtr = 0;
	private int winningPlayer;
	private Game SBGame;
	
//	/**
//	 * Initializes Members of the GameService
//	 */
//	public GameServiceImpl() {
//		SBGame = ConnectionServiceImpl.getGameInstance();
//	}//end constructor
	
	/**
	 * Checks if any BuildPiles need to be cleared
	 * @return String[] The BuildPile data. Null if no buildpiles were cleared.
	 */
	@Override
	public String[] checkCompletedBuildPiles() {
		
		if(SBGame.checkCompletedBuildPiles() != -1) {
			return getBuildPiles();
		} else {
			return null;
		}//end if-else
			
	}//end checkCompletedBuildPiles
	
	/**
	 * Checks if an event has been fired by the Current Player
	 * @return Boolean Whether an event has been fired by the Current Player
	 */
	@Override
	public Boolean checkForEvent() {
		return eventFired;
	}//end checkForEvent
	
	/**
	 * Checks if the Hand needs to be refilled, returning Hand data if needed
	 * @return String The Player's new hand data, null if Hand is not empty
	 */
	@Override
	public String checkHandRefill() {
		
		if(SBGame.getCurrentPlayer().getHand().getSize() == 0) {
			
			//Refill Hand
			SBGame.refillEmptyHand();
			
			//Return Hand data
			return getPlayerHand( SBGame.getCurrentPlayerIndex() );
		} else {
			return null;
		}//end if-else
		
	}//end checkHandRefill
	
	/**
	 * Checks if the Player has won 
	 * @return Integer 1: Player won game 0: Player won round -1: Player has not yet won
	 */
	@Override
	public Integer checkPlayerWon() {
		int winStatus = SBGame.checkPlayerWon();
		
		switch(winStatus) {
		case 1:
			//eventFired = true;
			//lastEvent = EventType.GAMEWIN;
			winningPlayer = SBGame.getCurrentPlayerIndex();
			break;
		case 0:
			//eventFired = true;
			//lastEvent = EventType.ROUNDWIN;
			winningPlayer = SBGame.getCurrentPlayerIndex();
			break;
		case -1:
			break;
		}//end switch
		
		return SBGame.checkPlayerWon();
	}//end checkPlayerWon
	
	/**
	 * Discards the indicated card from the hand to the indicated DiscardPile
	 * @param handIdx Index of desired card to discard
	 * @param pileNum Index of the desired discard pile to discard to
	 * @return null Used solely for determining success of server call
	 */
	@Override
	public Void discard(int handIdx, int pileNum) throws SkipBoException {
		SBGame.discard(handIdx, pileNum);
  	SBGame.endTurn();
  	eventFired = true;
  	lastEvent = EventType.DISCARD;
		return null;
	}//end discard
	
	/**
	 * Executes cheat command
	 * @param cmd The cheat command
	 * @return null Used solely for determining successs of server call
	 */
	@Override
	public Void executeCheat(String cmd) throws SkipBoException {
		SBGame.handleCheats(cmd);
		return null;
	}//end executeCheat
	
	/**
	 * Returns the BuildPile data for the Game
	 * @return String[] Array of Build Pile data
	 */
	@Override
	public String[] getBuildPiles() {
		String[] build = new String[4];
		
		for(int i = 0; i < 4; i++) {
			build[i] = SBGame.getBuildPile(i).toString();
		}//end for
		
		return build;
 	}//end getBuildPiles
	
	/**
	 * Returns the Current Player index
	 * @return Integer the Current player index
	 */
	@Override
	public Integer getCurrentPlayer() {
		return SBGame.getCurrentPlayerIndex();
	}//end getCurrentPlayer
	
	/**
	 * Returns the last Event performed by the Current Player
	 * @return EventType The last event performed by the Current Player
	 */
	@Override
	public EventType getLastEvent() {
		
		//Increment counter of clients who have received the event
		eventsReceivedCtr++;
		
		//Set eventFired flag back to false if all clients have received the event
		if(eventsReceivedCtr == SBGame.getNumPlayers() - SBGame.getNumCPUS() - 1) {
			eventFired = false;
			eventsReceivedCtr = 0;
		}//end if
		
		return lastEvent;
	}//end getLastEvent
	
	/**
	 * Retrieves the Player with the associated player index
	 * @return String[] Array containing player data. Format: String[ (0: Hand 1: Stock 2-5: Discard Piles 1-4) ]
	 */
	@Override
	public String[] getOtherPlayer(int playerIdx) {
		String[] pData = new String[6];
		String[] discardData = new String[4];
		
		//Populate hand and stock
		pData[0] = Integer.toString( getPlayerHandCount(playerIdx) );
		pData[1] = getPlayerStock(playerIdx);
		
		//Store Discards array
		discardData = getPlayerDiscards(playerIdx);
		
		//Populate discards
		for(int i = 0; i < 4; i++) {
			pData[i+2] = discardData[i];
		}//end for
		
		return pData;
	}//end getOtherPlayer
	
	/**
	 * Retrieves data for other Player with the associated player index
	 * @return String[] Array containing player data. Format: String[ (0: Hand 1: Stock 2-5: Discard Piles 1-4) ]
	 */
	@Override
	public String[] getPlayer(int playerIdx) {
		String[] pData = new String[6];
		String[] discardData = new String[4];
		
		//Populate hand and stock
		pData[0] = getPlayerHand(playerIdx);
		pData[1] = getPlayerStock(playerIdx);
		
		//Store Discards array
		discardData = getPlayerDiscards(playerIdx);
		
		//Populate discards
		for(int i = 0; i < 4; i++) {
			pData[i+2] = discardData[i];
		}//end for
		
		return pData;
	}//end getPlayer
	
	/**
	 * Retrieves the Discard Piles of the Player with the associated player index
	 * @param playerIdx The player index
	 * @return String[] Array containing String representations of DiscardPile
	 */
	@Override
	public String[] getPlayerDiscards(int playerIdx){
		String[] pile = new String[4];
		
		//Store String of Each pile in array
		for(int i = 0; i < 4; i++) {
			pile[i] = SBGame.getPlayer(playerIdx).getDiscard(i).toString();
		}//end for
		
		return pile;
	}//end getPlayerDiscards
	
	/**
	 * Retrieves the Hand of the Player with the associated player index
	 * @param playerIdx The player index
	 * @return String String representation of the hand
	 */
	@Override
	public String getPlayerHand(int playerIdx) {
		return SBGame.getPlayer(playerIdx).getHand().toString();
	}//end getPlayerHand
	
	/**
	 * Retrieves the Size of the Hand of the Player with the associated player index
	 * @param playerIdx The player index
	 * @return Integer The size of the Player's hand
	 */
	@Override
	public Integer getPlayerHandCount(int playerIdx) {
		return SBGame.getPlayer(playerIdx).getHand().getSize();
	}//end getPlayerHand
	
	/**
	 * Retrieves the Hand and Discard Piles of the Player with the associated player index
	 * @param playerIdx The player index
	 * @return String[] Array containing String representation of the hand and stock
	 */
	@Override
	public String[] getPlayerHandDiscards(int playerIdx) {
		String[] handDiscards = new String[5];
		String[] discards;
		
		//Store Hand Data
		handDiscards[0] = getPlayerHand(playerIdx);
		
		//Retrieve Discard Data
		discards = getPlayerDiscards(playerIdx);
		
		//Store Discard Data
		for(int i = 1; i < 5; i++) {
			handDiscards[i] = discards[i-1];
		}//end for
		
		return handDiscards;
	}//end getPlayerHandDiscards
	
	/**
	 * Retrieves the Hand and Stock of the Player with the associated player index
	 * @param playerIdx The player index
	 * @return String[] Array containing String representation of the hand and stock
	 */
	@Override
	public String[] getPlayerHandStock(int playerIdx) {
		String[] handStock = new String[2];
		
		handStock[0] = getPlayerHand(playerIdx);
		handStock[1] = getPlayerStock(playerIdx);
		
		return handStock;
	}//end getPlayerHand
	
	/**
	 * Returns a list of the player names
	 * @return String[] The list of player names
	 */
	@Override
	public String[] getPlayerNames() {
		return SBGame.getPlayerNames();
	}//end getPlayerNames
	
	/**
	 * Retrieves the StockPile of the Player with the associated player index
	 * @param playerIdx The player index
	 * @return String The String representation of the top card of the StockPile.
	 */
	@Override
	public String getPlayerStock(int playerIdx) {
		return SBGame.getPlayer(playerIdx).getStock().getTop().toString();
	}//end getPlayerStock
	
	/**
	 * Retrieves an array containing the scoreboard data
	 * @return String[] Array containing the scoreboard data
	 */
	@Override
	public String[] getScoreboardData() {
		ArrayList<String> SBData = new ArrayList<String>();
		String[] stockCounts = new String [SBGame.getNumPlayers()];
		String[] points = new String[SBGame.getNumPlayers()];
		String[] SBDataArr;
		
		//Populate list of current Stock Counts
		for(int i = 0; i < SBGame.getNumPlayers(); i++) {
			stockCounts[i] = Integer.toString( SBGame.getPlayer(i).getStock().getCount() );
		}//end for
		
		//Populate list of Player Points
		for(int i = 0; i < SBGame.getNumPlayers(); i++) {
			points[i] = Integer.toString( SBGame.getPlayer(i).getPoints() );
		}//end for
		
		//Populate Arraylist with Scoreboard data
		Collections.addAll(SBData, SBGame.getPlayerNames() );
		Collections.addAll(SBData, points);
		Collections.addAll(SBData, stockCounts);
		SBData.add( Integer.toString( SBGame.getNumStock() ) );
		SBData.add( Integer.toString( SBGame.getCurrentTurn() ) );
	
		//Copy Arraylist into array
		SBDataArr = new String[SBData.size()];
		SBDataArr = SBData.toArray(SBDataArr);
		
		return SBDataArr;
	}//end getScoreboardData
	
	/**
	 * Returns the message to be broadcast for a won Round or Game
	 * @return The message to be broadcast
	 */
	@Override
	public String getWinningPlayerMessage() {
		String broadcast = "";
		
		//Increment counter of clients who have received the winningPlayer
		eventsReceivedCtr++;
			
		//Store message before possibly resetting vars
		switch(lastEvent) {
		case GAMEWIN:
			broadcast = SBGame.getPlayer(winningPlayer).getName() + " has won the game.";
			break;
		case ROUNDWIN:
			broadcast = SBGame.getPlayer(winningPlayer).getName() + " has won the round.";
			break;
		default:
			break;
		}//e	nd switch
			
			
		//Reset counter once all Players have received winningPlayer
		if(eventsReceivedCtr == SBGame.getNumPlayers() - SBGame.getNumCPUS() ) {
//			eventFired = false;
			eventsReceivedCtr = 0;
			winningPlayer = -1;
			SBGame.endRound();
			SBGame.newRound();
		}//end if
			
		return broadcast;
	}//end getWinningPlayer
	
	/**
	 * Receives Game Instance from ConnectionService
	 * @return null Solely used to indicate success of the server call
	 */
	@Override
	public Void initGame() {
		SBGame = ConnectionServiceImpl.getGameInstance();
		return null;
	}//end initGame
	
	/**
	 * Plays the top card from the Player's desired DiscardPile onto the desired BuildPile
	 * @param pileNum Index of the desired DiscardPile to play from
	 * @param buildPile Index of the desired BuildPile to be played on
	 * @return null Solely used to indicate the success of the server call
	 * @throws SkipBoException Thrown on invalid move
	 */
	public Void makeDiscardMove(int pileNum, int buildPile) throws SkipBoException {
		SBGame.makeDiscardMove(pileNum, buildPile);
		eventFired = true;
  	lastEvent = EventType.DISCARDMOVE;
		return null;
	}//end makeDiscardMove
	
	/**
	 * Plays the desired card from the Player's Hand onto the desired BuildPile
	 * @param handIdx Index of the desired Card to be played
	 * @param buildPile Index of the desired BuildPile to be played on
	 * @return null Solely used to indicate the success of the server call
	 * @throws SkipBoException Thrown on invalid move
	 */
	@Override
	public Void makeHandMove(int handIdx, int buildPile) throws SkipBoException {
		SBGame.makeHandMove(handIdx, buildPile);
		eventFired = true;
  	lastEvent = EventType.HANDMOVE;
		return null;
	}//end makeHandMove
	
	/**
	 * Plays the card on top of the Player's Stock onto the desired BuildPile;
	 * @param buildPile Index of the desired BuildPile to be played on
	 * @return null Solely used to indicate the success of the server call
	 * @throws SkipBoException Thrown on invalid move
	 */
	@Override
	public Void makeStockMove(int buildPile) throws SkipBoException {
		SBGame.makeStockMove(buildPile);
		eventFired = true;
  	lastEvent = EventType.STOCKMOVE;
		return null;
	}//end makeStockMove
	
}//end GameServiceImpl
