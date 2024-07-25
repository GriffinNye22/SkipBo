/**
 * com.client.model.Game
 * CSC 421 Fall 2020
 * @author Griffin Nye
 * Implementation of the Game.
 */

package com.server.model;

import java.lang.Integer;
import java.util.ArrayList;

import com.client.SkipBoException;

public class Game{
	//Game Settings
	private int numPlayers;
	private int numCPUS;
	private int numStock;
	private int pointGoal;

	//Game Objects
	private BuildPile[] build;
	private Deck deck;
	private Player[] playerList; 

  //States
	private int currentPlayer;
	private int currentTurn;
	
	/**
	 * Constructs the Game object 
	 * @param players the number of players
	 * @param cpus the number of CPUs
	 * @param stock the beginning number of cards in each player's stockpile
	 * @param points the point goal
	 */
	public Game(int players, int cpus, int stock, int points) {
		//Initalize Game settings
		numPlayers = players;
		numCPUS = cpus;
		numStock = stock;
		pointGoal = points;
		
		//Set current player to first player
		currentPlayer = 0;
		
		//Create the buildpiles and the playerList
		build = new BuildPile[4];
		playerList = new Player[numPlayers];
		
		//Populate PlayerList with human players
		for (int i = 0; i < numPlayers - numCPUS; i++) {
			playerList[i] = new Player();
		}//end for
		
//		//Populate PlayerList with computer players
//		for (int i = numPlayers - numCPUS; i < numPlayers; i++) {
//			playerList[i] = new Computer("CPU" + (i - (numCPUS - 1) ) );
//		}//end for
	}//end constructor
	
	/**
	 * Checks if any BuildPiles are completed, clearing it if so.
	 * @return The index of the completed buildpile. -1 if none
	 */
	public int checkCompletedBuildPiles() {
		int ctr = 0;
		
		for (BuildPile b : build) {
			ctr++;
			
			//Checks if BuildPile is full
			if( b.getTop() != null && (b.getTop().getRank() == Card.Rank.TWELVE) ) {
				b.clear();
				return ctr;
			}//end if
			
		}//end for
		
		return -1;
	}//end clearCompleted BuildPiles
	
	/**
	 * Checks if the currentPlayer has won.
	 * @return int 1: if the player won the game. 0: if the player won the round -1: if the player did not win either
	 */
	public int checkPlayerWon() {
		
		//Check if a player has won the round
		if(playerList[currentPlayer].getStock().getCount() == 0) {
			
			//Check if player has won the game
			if (updateScore()) {
				return 1;
			} else {
				currentTurn++;
				return 0;
			}//end if-else
		
		}//end if
		
		return -1;
	}//end checkPlayerWon
	
	/**
	 * Discards card from Player's hand to the provided Discard Pile number
	 * @param handIdx index of desired Card from hand to discard
	 * @param pileNum index of DiscardPile to discard to
	 */
	public void discard(int handIdx, int pileNum) {
		
		playerList[currentPlayer].discard(handIdx, pileNum);
		
	}//end discard
	
	/**
	 * Clears objects at the end of the round
	 */
	public void endRound() {
		
		currentPlayer++;
		
		//Clear all players hands and stockpiles
		for (Player p : playerList) {
		
			p.getHand().clearHand();
			p.getStock().clear();
			
			for(int i = 0; i < 4; i++) {
				p.getDiscard(i).clear();
			}//end for
		
		}//end for
		
	}//end endRound

	/**
	 * Ends the Player's turn and refills the next Player's hand
	 */
	public void endTurn() {
		//Move to next player
		currentPlayer++;
		
		//Reset currentPlayer to beginning of player list if previous player was last index
		if(currentPlayer >= playerList.length) {
			currentPlayer = 0;
			
			//Increase the turn count only if the round is still underway
			//This is done because ending the round on the last player forces
			//turn to be incremented twice
			//if (contRound) {
			currentTurn++;
			//}//end if
		}//end if
		
		//Refill new CurrentPlayer's Hand to 5 cards
		if(playerList[currentPlayer].getHand().getSize() != 5) {
			playerList[currentPlayer].draw(deck.draw(5 - playerList[currentPlayer].getHand().getSize()));
		}//end if
		
	}//end endTurn	
	
	/**
	 * Returns the Game's BuildPiles for use with CPU players
	 * @return build the Game's BuildPiles
	 */
	public BuildPile[] getBuild() {
		return build;
	}//end getBuild
	
	/**
	 * Returns the BuildPile found at the provided index in build
	 * @param idx index of the desire BuildPile
	 * @return BuildPile the desired BuildPile
	 */
	public BuildPile getBuildPile(int idx) {
		 return build[idx];
	 }//end getBuildPile

	/**
	 * Returns the current player
	 * @return Player The current player
	 */
	public Player getCurrentPlayer() {
		return getPlayer(currentPlayer);
	}//end getCurrentPlayer
	
	/**
	 * Returns the index of the current player
	 * @return int index of the current player
	 */
	public int getCurrentPlayerIndex() {
		return currentPlayer;
	}//end getCurrentPlayer

	/**
	 * Returns the current turn number
	 * @return int the current turn number
	 */
	public int getCurrentTurn() {
		 return currentTurn;
	 }//end getCurrentTurn

	/**
	 * Returns the number of CPU players
	 * @return int number of CPUs
	 */
	public int getNumCPUS() {
		return numCPUS;
	}// end getNumCPUs

	/**
	 * Prompts the user for number of players
	 * @return int number of players
	 */
	public int getNumPlayers() {
		return numPlayers;
	}//end getNumPlayers
	
	/**
	 * Returns the number of cards in the stockpile
	 * @return int number of cards in Stockpile
	 */
	public int getNumStock() {
		return numStock;
	}//end getNumStock
	
	/**
	 * Returns Player from the PlayerList found at the provided index.
	 * @param playerIdx index of the desired Player
	 * @return Player the desired Player
	 */
	public Player getPlayer(int playerIdx) {
		return playerList[playerIdx];
	}//end getPlayer
	
	/**
	 * Returns array of the names of players in the PlayerList
	 * @return String[] list of player names
	 */
	public String[] getPlayerNames() {
		String[] playerNames = new String[numPlayers];
		
		for (int i = 0; i < numPlayers; i++) {
		 playerNames[i] = playerList[i].getName();
		}//end for
		
		return playerNames;
	}//end getPlayerNames
	
	/**
	 * Returns the point goal
	 * @return int the point goal
	 */
	public int getPointGoal() {
		return pointGoal;
	}//end getPointGoal
	
	/**
	 * Handles the execution of cheats
	 * @param str The command string
	 * @return Boolean Determines whether all Player's cards should be shown.
	 */
	public Boolean handleCheats(String str) throws SkipBoException {
		
		if (str != null) {
			Player editedPlayer = null;
			String[] cmd = str.split(" ");
			
			//Determine command 
			switch( cmd[0] ) {
			case "add":
				
				//Loop through all player names
				for (Player p: playerList) {
					
					//Store the matching player					
					if (cmd[1].equals(p.getName())) {
						editedPlayer = p;
						break;
					}//end if
					
				}//end for
				
				//Throw Exception for invalid player name
				if(editedPlayer == null) {
					throw new SkipBoException("No Player found matching name " + cmd[1] + ".");
				}//end if
				
				//Determine Destination
				switch( cmd[2] ) {
				case "hand":
					
					//Throw exception if empty index field
					if(cmd.length == 3) {
						throw new SkipBoException("No value found for index field.");
					//Throw exception if empty card rank field
					} else if(cmd.length == 4) {
						throw new SkipBoException("No value found for card rank field");
					//Throw Exception if invalid index
					} else if( Integer.valueOf(cmd[3]) < 0 || Integer.valueOf(cmd[3]) > 4) {
						throw new SkipBoException("Hand Index cannot be greater than 4");
					//Throw exception if invalid card rank
					} else if( Integer.valueOf(cmd[4]) < 1 || Integer.valueOf(cmd[4]) > 13) {
						throw new SkipBoException("Card Rank must be between 1 and 13");
					} else {
					
						//Attempt Insert to hand
						try {
							editedPlayer.getHand().get(Integer.parseInt(cmd[3])).setRank(Card.Rank.values()[Integer.parseInt(cmd[4])-1]);
						} catch (IndexOutOfBoundsException e) {
							//If index is in hand, but does not exist, add the card to the hand
							editedPlayer.getHand().addCard(new Card(Card.Rank.values()[Integer.parseInt(cmd[4])-1]));
						}//end try-catch
						
					}//end if-else
					break;
					
				case "discard":
					
					//Throw exception if empty index field
					if(cmd.length == 3) {
						throw new SkipBoException("No value found for index field.");
					//Throw exception if empty card rank field
					} else if(cmd.length == 4) {
						throw new SkipBoException("No value found for card rank field");
					//Throw Exception if invalid index
					} else if( Integer.valueOf(cmd[3]) < 0 || Integer.valueOf(cmd[3]) > 3) {
						throw new SkipBoException("Discard Pile number cannot be greater than 4");
					//Throw exception if invalid card rank
					} else if( Integer.valueOf(cmd[4]) < 1 || Integer.valueOf(cmd[4]) > 13) {
						throw new SkipBoException("Card Rank must be between 1 and 13");
					} else {
						//Insert card to discard
						editedPlayer.getDiscard(Integer.parseInt(cmd[3])).addCard(new Card(Card.Rank.values()[Integer.parseInt(cmd[4])-1]));
					}//end if
					
					break;
					
				case "stock":
					
					//Throw exception if empty card rank field
					if(cmd.length == 3) {
						throw new SkipBoException("No value found for card rank field.");
					//Throw exception if invalid card rank
					} else if (Integer.valueOf(cmd[3]) < 1 || Integer.valueOf(cmd[3]) > 13) {
						throw new SkipBoException("Card Rank must be between 1 and 13.");
					} else {
						//Insert card to stock
						editedPlayer.getStock().getTop().setRank(Card.Rank.values()[Integer.parseInt(cmd[3])-1]);
					}//end if
					
					break;
				
				default:
					throw new SkipBoException("Invalid destination: " + cmd[2] + "."
								                      + " Must be type: hand, discard, or stock.");
				}//end switch
				
				break;
				
			case "setPts":
				
			  //Loop through all player names
				for (Player p: playerList) {
					
					//Store the matching player					
					if (cmd[1].equals(p.getName())) {
						editedPlayer = p;
						break;
					}//end if
					
				}//end for
				
				//Throw Exception for invalid player name
				if(editedPlayer == null) {
					throw new SkipBoException("No Player found matching name " + cmd[1] + ".");
				}//end if
				
				if(cmd.length == 2) {
					throw new SkipBoException("No value found for points field.");
				} else if (Integer.valueOf(cmd[2]) > pointGoal) {
					throw new SkipBoException("Player's points cannot be set above the point goal of "
							                      + String.valueOf(pointGoal) + " points.");
				} else {
					editedPlayer.setPoints( Integer.valueOf( cmd[2] ) );
				}//end if

				break;
			
			case "showCards":
				
				if(cmd.length == 1) {
					return true;
				} else if (cmd[2] == "false") {
					return false;
				}//end if
				
			default:
				throw new SkipBoException("Invalid Command");
			}//end switch
		}//end if
		
		return false;
	}//end handleCheats
	
	/**
	 * Attempts to move card from player's discard pile to the provided buildpile
	 * @param pileNum DiscardPile number of the desired Discard Pile to play from
	 * @param buildIdx BuildPile number to play on
	 */
	public void makeDiscardMove(int pileNum, int buildIdx) throws SkipBoException{
		try {
			playerList[currentPlayer].playFromDiscard(pileNum, build[buildIdx]);
		} catch(SkipBoException e) {
			throw new SkipBoException( e.getMessage() );
		}//end try-catch
	}//end makeDiscardMove
	
	/**
	 * Attempts to move card from player's hand to the provided buildpile
	 * @param handIdx index of the desired Card in the Hand to play
	 * @param buildIdx BuildPile number to play on
	 */
	public void makeHandMove(int handIdx, int buildIdx) throws SkipBoException{
		try {
			playerList[currentPlayer].playFromHand(handIdx, build[buildIdx]);
		} catch(SkipBoException e) {
			throw new SkipBoException( e.getMessage() );
		}//end try-catch
	}//end makeHandMove
	
	/**
	 * Attempts to move card from player's stockpile to the provided buildpile
	 * @param buildIdx BuildPile number to play on
	 */
	public void makeStockMove(int buildIdx) throws SkipBoException {
		try {
			playerList[currentPlayer].playFromStock(build[buildIdx]);
		} catch(SkipBoException e) {
			throw new SkipBoException( e.getMessage() );
		}//end try-catch
	}//end makeStockMove

	/**
	 * Begins a new round of play. More specifically, creates new deck, creates the build piles, shuffles deck, and deals the cards.
	 */
	public void newRound() {
		
		//Create the deck
		deck = new Deck();	
		
		//Create the build piles
		for(int i = 0; i < 4; i++) {
			build[i] = new BuildPile();
		}//end for
		
		//Shuffle the deck
		deck.shuffle();
	
		//Deal the Cards
		for(int i = 0; i < numPlayers; i++) {
			playerList[i].getStock().fill( deck.draw(numStock) );
			playerList[i].getHand().addCards( deck.draw(5) );
		}//end for
		
	}//end newRound

	/**
	 * Handles calls for loading stats and beginning the Game
	 */
	public void play() {
		currentTurn = 1;
		newRound(); 
	}//end play
	
	/**
	 * Refills the current player's hand
	 */
	public void refillEmptyHand() {
		playerList[currentPlayer].draw(deck.draw(5));
	}//end refillHand
	
	/**
	 * Updates the score at the end of the round
	 * @return Boolean True if player won, False otherwise
	 */
	public Boolean updateScore() {
		int pts = 25;
		
		//Calculate Number of Cards in all Player's stockpiles
		for (Player p: playerList) {
			pts += (p.getStock().getCount() * 5);
		}//end for
		
		playerList[currentPlayer].addPoints(pts);
		
		//Check if player won game
		if (playerList[currentPlayer].getPoints() >= pointGoal) {
			return true;
		} else {
			return false;
		}//end if-else
	}//end updateScore

}//end Game class
	