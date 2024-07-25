/**
 * com.client.model.DiscardPile
 * CSC 421 Fall 2020
 * @author Griffin Nye
 * Implementation of the DiscardPile object.
 */

package com.server.model;

import java.util.ArrayList;
import java.util.Iterator;

public class DiscardPile extends Pile {
	private ArrayList<Card> discard;
	
	/**
	 * Creates the DiscardPile object
	 */
	public DiscardPile() {
		discard = new ArrayList<Card>();
	}//end constructor

	/**
	 * Adds Card to the DiscardPile
	 * @param c the Card to be added
	 */
	public void addCard(Card c) {
		discard.add(c);
	}//end addCard
	
	/**
	 * Returns String representation of the Rank of the Card found at idx in DiscardPile (For use with interface only)
	 * @param idx the index of the desired Card
	 * @return String String representation of the Rank of the Card found at idx
	 */
	public String get(int idx) {
		return( discard.get(idx).getRank().toString() );
	}//end get
	/**
	 * Returns Card on top of the DiscardPile
	 * @return Card Card on top of the DiscardPile
	 */
	public Card getTop() {
		
		if (discard.size() == 0) {
			return null;
		} else {
			return discard.get(discard.size() - 1);
		}//end if-else
	
	}//end getTop
	
	/**
	 * Removes Card on top of the DiscardPile
	 */
	public void removeTop() {
		discard.remove(discard.size() - 1);
	}//end removeTop
	
	/**
	 * Clears the DiscardPile
	 */
	public void clear() {
		discard.clear();
	}//end clear
	
	/**
	 * Returns number of Cards in the DiscardPile
	 * @return int number of Cards in the DiscardPile
	 */
	public int getCount() {
		return discard.size();
	}//end getCount

	/**
	 * Returns string representation of the DiscardPile
	 * @return String string representation of the DiscardPile
	 */
	public String toString() {
		Iterator<Card> itr = discard.iterator();
		String discardString;
		
		if (discard.size() == 0) {
			discardString = "X";
		} else {
			discardString = itr.next().toString();
		
			while(itr.hasNext()) {
				discardString += ">" + itr.next();
			}//end while
		}//end if-else
			
		return discardString;
	}//end toString
	
}//end DiscardPile