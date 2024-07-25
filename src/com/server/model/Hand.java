/**
 * com.client.model.Hand
 * CSC 421 Fall 2020
 * @author Griffin Nye
 * Implementation of the Hand object.
 */

package com.server.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Hand {
	private ArrayList<Card> hand;

	/**
	 * Constructs Hand object
	 */
	public Hand() {
		hand = new ArrayList<Card>();
	} //end constructor
	
	/**
	 * Adds Card object to the Hand
	 * @param c the Card object to be added
	 */
	public void addCard(Card c) {
		hand.add(c);
	}//end addCard
	
	/**
	 * Adds multiple Card objects to the Hand
	 * @param cards ArrayList of card objects
	 */
	public void addCards(ArrayList<Card> cards) {
		Iterator<Card> itr = cards.iterator();
		
		while(itr.hasNext()) {
			hand.add(itr.next());
		}//end while
	}//end addCards

	/**
	 * Removes Card from the Hand
	 * @param idx index of the Card to be removed
	 */
	public void remove(int idx) {
		hand.remove(idx);
	} //end clearHand

	/**
	 * Clears the Hand
	 */
	public void clearHand() {
		hand.clear();
	} //end clearHand

	/**
	 * Returns size of the Hand
	 * @return int the size of the Hand
	 */
	public int getSize() {
		String handString = "";
		
		for (int i = 0; i < hand.size(); i++) {
			handString = handString + " " + hand.get(i).toString();
		}//end for
		
		return hand.size();
	} //end getSize

	/**
	 * Searches the Hand for a specific Card Rank
	 * @param rank the rank of the card to be searched for
	 * @return int index of the found Card, -1 if not found.
	 */
	public int search(Card.Rank rank) {
		for (int i = 0; i < hand.size(); i++) {
			if (hand.get(i).getRank() == rank) {
				return i;
			}//end if
		}//end for

		return -1;
	}//end search

	/**
	 * Retrieves Card from the Hand by index
	 * @param idx the index of the desired Card
	 * @return Card the Card located at the provided index
	 */
	public Card get(int idx) {
		return hand.get(idx);
	}//end getCard

	/**
	 * Retrieves Card from the Hand by index
	 * @param rank The rank of the desired card
	 * @return Card the Card with the provided rank; -1 if not found.
	 */
	public Card getCard(Card.Rank rank) {
		int idx = search(rank);

		if (idx != -1) {
			return hand.get(idx);
		} else {
			return null;
		}//end if-else
	}//end getCard

	/**
	 * Returns string representation of the Hand object
	 * @return String string representation of the Hand object
	 */
	public String toString() {
		Iterator<Card> itr = hand.iterator();
		String handString;
		
		if(this.getSize() != 0) {
			handString = itr.next().toString();
		
			while (itr.hasNext() ) {
				handString+= " " + itr.next().toString();
			}//end while
			
		} else {
			handString = "X";
		}//end if-else
		
		return handString;
	}//end toString
}//end Hand
