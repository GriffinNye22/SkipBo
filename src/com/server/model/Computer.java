/**
 * com.client.model.Computer
 * CSC 421 Fall 2020
 * @author Griffin Nye
 * Implementation of the Computer Player class.
 */
 
 package com.server.model;

 import java.util.Random;

import com.client.SkipBoException;
 
public class Computer extends Player {
	public enum MoveType {DISCARD, DISCARDMOVE, HANDMOVE, STOCKMOVE};
	
	private MoveType move;
	private String moveRecord;
	private int[] buildRank = new int[4];
	private int playedBuildIdx;
	
	/**
	 * Constructs the Computer object
	 */
	public Computer() {
		super();
	}//end constructor
	
	/**
	 * Returns the type of move the CPU made.
	 * @return MoveType The type of move the CPU made
	 */
	public MoveType getMove() {
		return move;
	}//end getMove
	
	/**
	 * Returns String message of the CPU's move
	 * @return String message of the CPU's move
	 */
	public String getMoveRecord() {
		return moveRecord;
	}//end getMoveRecord
	
	/**
	 * Returns the index of the BuildPile played on
	 * @return int the index of the BuildPile played on
	 */
	public int getPlayedBuildIdx() {
		return playedBuildIdx;
	}//end getPlayedBuildIdx
	
	/**
	 * Decides the Computer's next move
	 * @param build the Game's BuildPiles
	 * @return Boolean True: Still Computer's turn False: Computer has ended turn
	 * @throws SkipBoException 
	 */
	public Boolean decideMove(BuildPile[] build) throws SkipBoException {
		
		//Load Build Ranks
		loadBuildRanks(build);
		
		//Check for Stock move
		if ( checkStock(build) ) {
			move = MoveType.STOCKMOVE;
			return true;
		//Check for Hand move
		} else if ( checkHand(build) ) {
			move = MoveType.HANDMOVE;
			return true;
		//Check for Discard move
		} else if ( checkDiscard(build) ) {
			move = MoveType.DISCARDMOVE;
			return true;
		//Discard
		} else {
			move = MoveType.DISCARD;
			decideDiscard();
			return false;
		}//end if-else 
		
	}//end decideMove
	 
	/**
	 * Loads the ranks of the top card of each BuildPile into the buildRank data member to help Computer decide move
	 * @param build the Game's BuildPiles
	 */
	private void loadBuildRanks(BuildPile[] build) {
		
		for (int i = 0; i < build.length; i++) {
			
			//Store rank of top card in BuildPile (-1 if empty)
			if (build[i].getTop() == null) {
				buildRank[i] = -1;
			} else {
				buildRank[i] = build[i].getTop().getRank().ordinal();
			}//end if
			
		}//end for
		
	}//end loadBuildRanks
	
	/**
	 * Loads the ranks of the top card of each of the Computer's DiscardPiles into discardRank to help Computer decide discard
	 * @param discardRank the array for storing the discardRanks
	 */
	private void loadDiscardRanks(int[] discardRank) {
		
		for (int i = 0; i < 4; i++) {
			
			//Store rank of top card in DiscardPile (0 if empty)
			if (this.getDiscard(i).getTop() == null) {
				discardRank[i] = 0;
			} else {
				discardRank[i] = this.getDiscard(i).getTop().getRank().ordinal();
			}//end if
			
		}//end for
		
	}//end loadDiscardRanks
	
	/**
	 * Checks if a move from the Stockpile is possible
	 * @param build The Game's BuildPiles
	 * @return Boolean True: Stockpile move was made. False: No move made.
	 * @throws SkipBoException 
	 */
	private Boolean checkStock(BuildPile[] build)  {
		Boolean moveMade = false;
		
		//Loop through each Build Pile
		for (int i = 0; i < buildRank.length; i++) {

			//If card is playable, play the card and alert the user
			if ( (buildRank[i] == this.getStock().getTop().getRank().ordinal() - 1) ||
				 (this.getStock().getTop().getRank() == Card.Rank.WILD) ) {
				
				//Store description of move
				moveRecord = this.getName() + " played " + this.getStock().getTop().toString() +
				             " from stockpile onto Build Pile " + (i+1) + ".";
				
				//playFromStock(build[i]);
				
				playedBuildIdx = i;
				moveMade = true;
				break;
			}//end if
			
		}//end for
		
		return moveMade;
	}//end checkStock()

	/**
	 * Checks if a move from the Hand is possible
	 * @param build The Game's BuildPiles
	 * @return Boolean: True: Hand move was made. False: No move made.
	 */
	private Boolean checkHand(BuildPile[] build) {
		Boolean moveMade = false;
		
		//Loop through each Build Pile and hand combination
		for (int i = 0; i < buildRank.length; i++) {
			for (int j = 0; j < this.getHand().getSize(); j++) {
				
				//If card is playable, play the card and alert the user
				if ( (buildRank[i] == this.getHand().get(j).getRank().ordinal() - 1) ||
				     (this.getHand().get(j).getRank() == Card.Rank.WILD) ) {
					
					//Store description of move
					moveRecord = this.getName() + " played " + this.getHand().get(j).toString() +
					             " from hand onto Build Pile " + (i+1) + ".";   
					
					//playFromHand(j, build[i]);
					playedBuildIdx = i;
					moveMade = true;
				  break;
				}//end if								
			}//end inner for

			//End outer loop if move made
			if (moveMade) {
				break;
			}//end if
			
		}//end outer for
		
		return moveMade;
	}//end checkHand
	
	/**
	 * Checks if a move from one of the the Discard Piles is possible
	 * @param build The Game's BuildPiles
	 * @return Boolean True: Discard Pile move was made. False: No move made.
	 */
	private Boolean checkDiscard(BuildPile[] build) throws SkipBoException{
		Boolean moveMade = false;
		
		//Loop through each Build Pile and Discard Pile combination
		for (int i = 0; i < buildRank.length; i++) {
			for (int j = 0; j < 4; j++) {
				
				//Skip Discard pile if empty
				if (this.getDiscard(j).getTop() == null) {
					break;
					
					//If card is playable, play the card and alert the user
				} else if ( (buildRank[i] == this.getDiscard(j).getTop().getRank().ordinal() - 1) ||
						        (this.getDiscard(j).getTop().getRank() == Card.Rank.WILD) ) {
					
					//Store description of move
					moveRecord = this.getName() + " played " + this.getDiscard(j).getTop().toString() +
					             " from Discard Pile " + (j+1) + " onto Build Pile " + (i+1) + ".";
					
					playFromDiscard(j, build[i]);
					playedBuildIdx = i;
					moveMade = true;
				  break;
				}//end if							
			}//end inner for

			//end outer loop if move made
			if (moveMade) {
				break;
			}//end if
			
		}//end outer for
		
		return moveMade;
	}//end checkDiscard
	
	/**
	 * Decides which Card from the Hand and which DiscardPile to perform a discard.
	 * First tries to discard Card from Hand which is one rank lower than top card of DiscardPile.
	 * If this fails, discards the Card that is the greatest outlier from the average of the BuildPile's top cards onto a random DiscardPile.
	 * Finally, Computer will only discard a WILD, if no other discard is possible.
	 */
	private void decideDiscard(){
		Boolean moveMade = false;
		Random rand = new Random();
		int[] discardRank = new int[4];
		int randNum;
		
		
		//Load ordinals for each discard pile
		loadDiscardRanks(discardRank);
		
		//Loop through all hand and discard pile combinations
		for (int i = 0; i < discardRank.length; i++) {
			
			//If Discard Pile is empty, break out of loop for efficiency
			if (discardRank[i] == 0) {
				break;
			}//end if
			
			for (int j = 0; j < this.getHand().getSize(); j++) {
				
				//If card in hand is one rank lower than card in discard pile, discard
				if (discardRank[i] == this.getHand().get(j).getRank().ordinal() + 1) {
					
					//Store description of move
					moveRecord = this.getName() + " discarded " + this.getHand().get(j).toString() +
					             " from hand onto Discard Pile " + (i+1) + ".";
					    
					discard(j,i);
					moveMade = true;
					break;
				}//end if
			}//end inner for
			
			//end outer loop if move made
			if (moveMade) {
				break;
			}//end if
			
		}//end outer for
		
		//If no cards in hand are one rank lower than a card in discard pile,
		//Find the average of the cards on top of Build Piles and discard
		//the card furthest from the average onto a random discard pile.
		if (!moveMade) { 
			randNum = rand.nextInt(4);
			int card = findOutlier();
			
			moveRecord = this.getName() + " discarded " + this.getHand().get(card).toString() +
					         " from hand onto Discard Pile " + String.valueOf(randNum + 1) + ".";
			
			discard(card, randNum);
		}//end if
		
	}//end decideDiscard
	
	/**
	 * Calculates average rank of Build Piles and finds the outlier in the hand
	 * @return idx the index of the Card to discard.
	 */
	private int findOutlier() {
		int average = 0;
		int max = 0;
		int idx = -1;
		
		for (int i = 0; i < buildRank.length; i++) {
			
			//Total all non-empty Build Piles
			if (buildRank[i] != -1) {
				average += buildRank[i];
			}//end if
			
		}//end for
		
		average = average / buildRank.length;
		
		//Loop through cards in hand and find greatest outlier
		for (int i = 0; i < this.getHand().getSize(); i++) {
			
			//Ignore wilds	
			if (this.getHand().get(i).getRank() != Card.Rank.WILD) {
				
				//Find furthest card from the average
				if( Math.abs(this.getHand().get(i).getRank().ordinal() - average) > max) {
					max = Math.abs(this.getHand().get(i).getRank().ordinal() - average);
					idx = i;
				}//end if
				
			}//end if
		}//end for
		
		//If only possible discard option is WILD, discard it
		if (idx == -1) {
			idx = this.getHand().search(Card.Rank.WILD);
		}//end if
		
		return idx;
	}//end findOutlier
	
}//end Computer class