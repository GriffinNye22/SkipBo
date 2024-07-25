/**
 * com.server.ConnectionServiceImpl
 * CSC421 Fall 2020
 * @author Griffin Nye
 * Server-side implementation of the Connection Service. 
 * Handles creation of the game and the connection of clients.
 */

package com.server;

import java.util.Timer;
import java.util.TimerTask;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

import com.client.settings.ConnectionService;
import com.google.gwt.core.client.GWT;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;
import com.server.model.Game;

public class ConnectionServiceImpl extends RemoteServiceServlet implements ConnectionService {
	private int numClients = 0;
	private static Game SBGame;
	
	private Timer resetClientCount;
	private ResetTask reset;
	
	private class ResetTask extends TimerTask {
		
		@Override
		public void run() {
			numClients = 0;
		}//end run
		
	}//end Timer
	
	/**
	 * Indicates whether the Game has reached its capacity
	 * @return Boolean True: If game is full False: If Game is not full
	 */
	public Boolean checkGameFull() {
		
		if(numClients == SBGame.getNumPlayers() - SBGame.getNumCPUS() ) {
			return true;
		} else {
			return false;
		}//end if
		
	}//end checkGameFull
	
	
	/**
	 * Checks if the game has been created. Used by non-host clients to determine when to connect to the game.
	 * @return Integer The number of players in the game. (0 means there is no game yet)
	 */
	@Override
	public Integer checkGameStart() {
		
		if(SBGame == null) {
			return 0;
		} else {
			return SBGame.getNumPlayers();
		}//end if-else
		
	}//end checkGameStart
	
	/**
	 * Connects a client to the game, setting their name and providing them with their player index.
	 * @param name The player's name
	 * @return Integer The client's player index, -1 if game is already full
	 */
	@Override
	public Integer connectClient(String name) {
		int clientNum;
		
		
		if(numClients < SBGame.getNumPlayers() - SBGame.getNumCPUS() ) {
			
			//Increment number of clients
			numClients++;
			
			//Store clientNum
			clientNum = numClients - 1;
			
			//Set Player name
			SBGame.getPlayer(clientNum).setName(name);
			
			//Reset Client count if game full **ADDED TO ALLOW GRADING WITHOUT RECOMPILE
			if ( checkGameFull() ) {
				resetClientCount = new Timer();
				reset = new ResetTask();
				resetClientCount.schedule(reset, 5000);
			}//end if
			
			return clientNum;
		} else {
			return -1;
		}//end if
		
	}//end connectClient
	
	/**
	 * Specifies the first connected client as the host client
	 * @return Boolean True: The client is the host client False: The client is a non-host client
	 */
	@Override
	public Boolean connectHostClient() {
		
		//Specify first connected client as the host, block all other connections
		if (numClients == 0) {
			
			//Increment number of clients
			numClients++;
			
			return true;
			
		} else {
			
			return false;
		}//end if
		
	}//end connectHostClient
	
	/**
	 * Creates a Session for a client by inserting player index into session
	 * @param playerIdx The client's associated player index
	 */
	public void createSession(int playerIdx) {
		HttpServletRequest request = this.getThreadLocalRequest();
		HttpSession session = request.getSession();
		session.setAttribute("playerIdx", playerIdx);
	}//end createSession
	
	/**
	 * Retrieves the created Game Instance
	 * @return Game The created Game Instance
	 */
	public static Game getGameInstance() {
		return SBGame;
	}//end getGameInstance
	
	/**
	 * Creates the game with the specified settings and sets the Host client's player name
	 * @param players Number of players in the game
	 * @param CPUs Number of CPU players in the game
	 * @param stock Number of cards in the stockpile
	 * @param points The point goal for the game
	 * @param hostName The name of the host client's player
	 * @return null Used solely for determining success of server call
	 */
	@Override
	public Void startGame(int players, int CPUs, int stock, int points, String hostName) {
		SBGame = new Game(players, CPUs, stock, points);
		SBGame.play();
		SBGame.getPlayer(0).setName(hostName);
		return null;
	}//end startGame

}//end ConnectionServiceImpl
