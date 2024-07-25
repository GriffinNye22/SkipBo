/**
 * com.client.model.Player
 * CSC 421 Fall 2020
 * @author Griffin Nye
 * Implementation of the Human Player.
 */
 
package com.server.model;

import java.util.ArrayList;

import com.client.SkipBoException;
import com.google.gwt.user.client.rpc.IsSerializable;

public class Player implements IsSerializable {
	//Data members
	private Hand hand;
	private DiscardPile[] discards;
	private StockPile stock;
	private String name;
	private int points;
	
	/**
	 * Creates the Player object
	 */
	public Player() {
		points = 0;
		
		hand = new Hand();
		stock = new StockPile();
		discards = new DiscardPile[4];
		
		for (int i = 0; i < 4; i++) {
			discards[i] = new DiscardPile();
		}//end for
	}//end constructor
	
	/**
	 * Returns name of the Player
	 * @return String name of the Player
	 */
	public String getName() {
		return name;
	}//end getName
	
	/**
	 * Sets the Player's points to a specified value
	 * @param num the number of points to be set
	 */
	public void setPoints(int num) {
		points = num;
	}//end setPoints
	
	/**
	 * Sets the Player's name
	 * @param name The player's name
	 */
	public void setName(String name) {
		this.name = name;
	}//end setName
	
	/**
	 * Returns the Player's points
	 * @return int number of Player points
	 */
	public int getPoints() {
		return points;
	}//end getPoints
	
	/**
	 * Adds a specified number of points to the Player's points
	 * @param num number of points to be added.
	 */
	public void addPoints(int num) {
		points += num;
	}//end addPoints
	
	/**
	 * Returns the Player's Hand 
	 * @return Hand the Player's Hand object
	 */
	public Hand getHand() {
		return hand;
	}//end getHand
	
	/**
	 * Returns the Player's StockPile 
	 * @return StockPile the Player's StockPile object
	 */
	public StockPile getStock() {
		return stock;
	}//end getStock
	
	/**
	 * Returns one of the Player's DiscardPiles
	 * @param pileNum the DiscardPile number
	 * @return DiscardPile the Player's DiscardPile at pileNum
	 */
	public DiscardPile getDiscard(int pileNum) {
		return discards[pileNum];
	}//end getDiscard
	
	/**
	 * Returns the Player's DiscardPiles
	 * @return DiscardPile[] The Player's DiscardPiles
	 */
	public DiscardPile[] getDiscardPiles() {
		return discards;
	}//end getDiscardPiles
	
	/**
	 * Draws the dealt Cards into the hand
	 * @param dealt ArrayList of Cards
	 */
	public void draw(ArrayList<Card> dealt) {
		hand.addCards(dealt);
	}//end draw
	
	/**
	 * Validates move and plays a card from the Player's Hand onto the BuildPile.
	 * @param handIdx the index of the Card in the Player's Hand
	 * @param build the BuildPile to be played on
	 */
	public void playFromHand(int handIdx, BuildPile build) throws SkipBoException {
		
		
		//Valid Empty Buildpile Case
		if ( (build.getTop() == null) && 
			   ( (hand.get(handIdx).getRank() == Card.Rank.ONE) ||
					 (hand.get(handIdx).getRank() == Card.Rank.WILD) ) )	{
									
			build.addCard(hand.get(handIdx));
			hand.remove(handIdx);	
			
			if (build.getTop().getRank() == Card.Rank.WILD) {
				build.getTop().setRank(Card.Rank.ONE);
			}//end if
		
		
		//Invalid Empty Buildpile Case
    } else if (build.getTop() == null) {
			
		  throw new SkipBoException("Buildpile empty. Card must be a ONE or a WILD.");
			
		//Wildcard Case		
		} else if (hand.get(handIdx).getRank() == Card.Rank.WILD) {
	
			Card.Rank temp = build.getTop().getNextRank();
			build.addCard(hand.get(handIdx));
			hand.remove(handIdx);
			build.getTop().setRank(temp);
		
		//Linear Build Case
		}	else if ( (build.getTop().getRank().ordinal() ) == 
								(hand.get(handIdx).getRank().ordinal() - 1) ) {
			
			build.addCard(hand.get(handIdx));
			hand.remove(handIdx);
		
		//Invalid Move Case
		} else {
			throw new SkipBoException("Card is not playable on the selected buildpile.");
		}//end if-else

	}//end playFromHand
	
	/**
	 * Validates move and plays a card from the Player's StockPile onto the BuildPile
	 * @param build the BuildPile to be played on
	 */
	public void playFromStock(BuildPile build) throws SkipBoException {
		
		//Valid Empty Buildpile Case
		if ( (build.getTop() == null) && 
			   ( (stock.getTop().getRank() == Card.Rank.ONE) ||
					 (stock.getTop().getRank() == Card.Rank.WILD) ) )	{
									
			build.addCard( stock.getTop() );
			stock.removeTop();	
			
			if (build.getTop().getRank() == Card.Rank.WILD) {
				build.getTop().setRank(Card.Rank.ONE);
			}//end if
		
		
		//Invalid Empty Buildpile Case
    } else if (build.getTop() == null) {
			
		  throw new SkipBoException("Buildpile empty. Card must be a ONE or a WILD.");
			
		//Wildcard Case		
		} else if (stock.getTop().getRank() == Card.Rank.WILD) {
	
			Card.Rank temp = build.getTop().getNextRank();
			build.addCard( stock.getTop() );
			stock.removeTop();
			build.getTop().setRank(temp);
		
		//Linear Build Case
		}	else if ( (build.getTop().getRank().ordinal() ) == 
								(stock.getTop().getRank().ordinal() - 1) ) {
			
			build.addCard( stock.getTop() );
			stock.removeTop();
		
		//Invalid Move Case
		} else {
			throw new SkipBoException("Card is not playable on the selected buildpile.");
		}//end if-else

	}//end playFromStock
	
	/**
	 * Validates move and plays a card from the Player's DiscardPile onto the BuildPile
	 * @param pileNum the DiscardPile number to be played from
	 * @param build the BuildPile to be played on
	 */
	public void playFromDiscard(int pileNum, BuildPile build) throws SkipBoException {
		
		//Check for empty discard pile
		if (discards[pileNum].getCount() != 0) {
			
			//Valid Empty Buildpile Case
			if ( (build.getTop() == null) && 
					 ( (discards[pileNum].getTop().getRank() == Card.Rank.ONE) ||
						 (discards[pileNum].getTop().getRank() == Card.Rank.WILD) ) )	{
										
				build.addCard( discards[pileNum].getTop() );
				discards[pileNum].removeTop();
				
				if (build.getTop().getRank() == Card.Rank.WILD) {
					build.getTop().setRank(Card.Rank.ONE);
				}//end if
			
			
			//Invalid Empty Buildpile Case
			} else if (build.getTop() == null) {
				
			  throw new SkipBoException("Buildpile empty. Card must be a ONE or a WILD.");
				
			//Wildcard Case		
			} else if (discards[pileNum].getTop().getRank() == Card.Rank.WILD) {
		
				Card.Rank temp = build.getTop().getNextRank();
				build.addCard( discards[pileNum].getTop() );
				discards[pileNum].removeTop();
				build.getTop().setRank(temp);
			
			//Linear Build Case
			}	else if ( (build.getTop().getRank().ordinal() ) == 
									(discards[pileNum].getTop().getRank().ordinal() - 1) ) {
				
				build.addCard( discards[pileNum].getTop() );
				discards[pileNum].removeTop();
			
			//Invalid Move Case
			} else {
				throw new SkipBoException("Card is not playable on the selected buildpile.");
			}//end if-else

		} else {
			throw new SkipBoException("No cards available on selected discard pile.");
		}//end if-else
	
	}//end playFromDiscard
	
	/**
	 * Discards a card from the Player's Hand onto a DiscardPile
	 * @param handIdx the index of the Card in the Player's Hand
	 * @param pileNum the DiscardPile num to discard onto
	 */
	public void discard(int handIdx, int pileNum) {
		discards[pileNum].addCard(hand.get(handIdx));
		hand.remove(handIdx);
	}//end discard
	 
	/**
	 * Returns a string representation of the Player object
	 * @return String string representation of the Player object
	 */ 
	public String toString() {
		String playerString = "";
		
		playerString += "Name: " + name + "\n";
		playerString += "Hand: " + hand.toString();
		playerString += " Stockpile: " + stock.getTop().toString();
		playerString += "\n" + "Discard Piles: ";
		
		for (DiscardPile p: discards) {
		  playerString += p.toString() + "\n" + "               ";
		}//end for
		
	return (playerString);
	}//end toString
}//end Player