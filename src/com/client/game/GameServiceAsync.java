/**
 * com.client.game.GameServiceAsync
 * CSC421 Fall 2020
 * @author Griffin Nye
 * Asynchronous Client-side interface for the GameService
 */

package com.client.game;

import com.client.SkipBoException;
import com.client.game.event.EventType;
import com.google.gwt.user.client.rpc.AsyncCallback;

public interface GameServiceAsync {

	void checkCompletedBuildPiles(AsyncCallback<String[]> callback);
	void checkForEvent(AsyncCallback<Boolean> callback);
	void checkPlayerWon(AsyncCallback<Integer> callback);
	void checkHandRefill(AsyncCallback<String> callback);
	void executeCheat(String cmd, AsyncCallback<Void> callback);
	void discard(int handIdx, int pileNum, AsyncCallback<Void> callback);
	void getBuildPiles(AsyncCallback<String[]> callback);
	void getCurrentPlayer(AsyncCallback<Integer> callback);
	void getLastEvent(AsyncCallback<EventType> callback);
	void getOtherPlayer(int playerIdx, AsyncCallback<String[]> callback);
	void getPlayerDiscards(int playerIdx, AsyncCallback<String[]> callback);
	void getPlayerHand(int playerIdx, AsyncCallback<String> callback); 
	void getPlayerHandCount(int playerIdx, AsyncCallback<Integer> callback);
	void getPlayerHandDiscards(int playerIdx, AsyncCallback<String[]> callback);
	void getPlayerHandStock(int playerIdx, AsyncCallback<String[]> callback);
	void getPlayerNames(AsyncCallback<String[]> callback);
	void getPlayerStock(int playerIdx, AsyncCallback<String> callback);                
	void getPlayer(int playerIdx, AsyncCallback<String[]> callback);
	void getScoreboardData(AsyncCallback<String[]> callback);
	void getWinningPlayerMessage(AsyncCallback<String> callback);
	void initGame(AsyncCallback<Void> callback);
	void makeDiscardMove(int pileNum, int buildPile, AsyncCallback<Void> callback);
	void makeHandMove(int handIdx, int buildPile, AsyncCallback<Void> callback);
	void makeStockMove(int buildPile, AsyncCallback<Void> callback);
	
}//end GameServiceAsync
