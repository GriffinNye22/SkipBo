/**
 * com.client.model.StockPile
 * CSC 421 Fall 2020
 * @author Griffin Nye
 * Implementation of the StockPile object
 */

package com.server.model;

import java.util.ArrayList;
import java.util.Iterator;

public class StockPile extends Pile {
	
	private ArrayList<Card> stock;
	
	/**
	 * Creates the StockPile object
	 */
	public StockPile() {
		stock = new ArrayList<Card>();
	}//end constructor
	
	/**
	 * Fills the StockPile from an ArrayList of Cards
	 * @param dealt ArrayList of cards
	 */
	public void fill(ArrayList<Card> dealt) {
		Iterator<Card> itr = dealt.iterator();
		
		while (itr.hasNext()) {
			stock.add(0, itr.next());
		}//end while
	}//end fill
	
	/**
	 * Clears the StockPile
	 */
	public void clear() {
		stock.clear();
	}//end clear
	
	/**
	 * Returns Card on top of the StockPile
	 * @return Card Card on top of the StockPile
	 */
	public Card getTop() {
		if (stock.isEmpty() ) {
			return null;
		} else {
			return stock.get(0);
		}//end getTop
	}
	
	/**
	 * Removes Card on top of the StockPile
	 */
	public void removeTop() {
		stock.remove(0);
	}//end removeTop
	
	/**
	 * Returns number of Cards in the StockPile
	 * @return int number of Cards in the StockPile
	 */
	public int getCount() {
		return stock.size();
	}//end getCount
	
	/**
	 * Prints the StockPile to the Screen (testing only);
	 */
	public void print() {
		System.out.println("Stockpile: " + stock);
	}//end print
}//end Stockpile