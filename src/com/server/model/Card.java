/**
 * com.client.model.Card
 * CSC 421 Fall 2020
 * @author Griffin Nye
 * Implementation of the Card object.
 */

package com.server.model;


public class Card{
    public enum Rank { ONE, TWO, THREE, FOUR, FIVE, SIX, SEVEN, EIGHT, NINE, TEN, ELEVEN, TWELVE, WILD};
    
    private Rank rank;

	/**
	 * Constructs Card object with provided rank
	 * @param rank The rank of the card
	 */
    public Card(Rank rank) {
		this.rank = rank;
    }//end constructor

	/**
	 * Sets the Card object's rank
	 * @param rank The rank to be set
	 */
	public void setRank(Rank rank) {
		this.rank = rank;
	}//end setRank
	
	/**
	 * Returns the next rank in the enum list
	 * @return Rank the next rank in the enum list
	 */
	public Rank getNextRank() {
		if(this.rank.ordinal() < Rank.values().length - 1) {
			return Rank.values()[this.rank.ordinal() + 1];
		} else {
			return null;
		}//end if-else
	}//end getNext

	/**
	 * Gets the rank of the current Card
	 * @return Rank the rank of the current Card
	 */
    public Rank getRank() {
		return (this.rank);
    }//end getRank

	/**
	 * Returns string representation of the Card class
	 * @return String string representation of the Card class
	 */
    public String toString() {
		return this.rank.toString();
    }//end toString

}//end Card
