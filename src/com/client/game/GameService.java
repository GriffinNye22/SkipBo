/**
 * com.client.game.GameService
 * CSC421 Fall 2020
 * @author Griffin Nye
 * Client-side interface for the GameService
 */

package com.client.game;

import com.client.SkipBoException;
import com.client.game.event.EventType;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;
import com.server.model.Player;

@RemoteServiceRelativePath("game")
public interface GameService extends RemoteService {

	Boolean checkForEvent();
	
	EventType getLastEvent();
	
	Integer checkPlayerWon();
	Integer getCurrentPlayer();
	Integer getPlayerHandCount(int playerIdx);
	String getWinningPlayerMessage();
	
	String checkHandRefill();
	String getPlayerHand(int playerIdx);
	String getPlayerStock(int playerIdx);
	
	String[] checkCompletedBuildPiles();
	String[] getBuildPiles();
	String[] getOtherPlayer(int playerIdx);
	String[] getPlayerDiscards(int playerIdx);
	String[] getPlayerHandStock(int playerIdx);
	String[] getPlayerHandDiscards(int playerIdx);
	String[] getPlayerNames();
	String[] getPlayer(int playerIdx); 
	String[] getScoreboardData();
	
	Void discard(int handIdx, int pileNum) throws SkipBoException;
	Void executeCheat(String cmd) throws SkipBoException;
	Void initGame();
	Void makeDiscardMove(int pileNum, int buildPile) throws SkipBoException;
	Void makeHandMove(int handIdx, int buildPile) throws SkipBoException;
	Void makeStockMove(int buildPile) throws SkipBoException;
}//end GameService
