/**
 * com.client.model.BuildPile
 * CSC 421 Fall 2020
 * @author Griffin Nye
 * Implementation of the BuildPile object.
 */

package com.server.model;

import java.util.ArrayList;
import java.io.*;

public class BuildPile extends Pile {
	
	private ArrayList<Card> build;
	
	/**
	 * Creates the BuildPile object
	 */
	public BuildPile() {
		build = new ArrayList<Card>();
	}//end constructor
	
	/**
	 * Clears the BuildPile
	 */
	public void clear() {
		build.clear();
	}//end clear
	
	/**
	 * Returns Card on top of the BuildPile
	 * @return Card Card on top of the BuildPile
	 */
	public Card getTop() {
		if(build.isEmpty() ) {
			return null;
		} else {
			return build.get(0);
		}
	}//end getTop
	
	/**
	 * Adds Card to the BuildPile
	 * @param c the Card to be added
	 */
	public void addCard(Card c) {
		build.add(0,c);
	}//end addCard
	
	/**
	 * Returns number of Cards in the BuildPile
	 * @return int number of Cards in the BuildPile
	 */
	public int getCount() {
		return build.size();
	}//end getCount
	
	/**
	 * Returns string representation of the BuildPile
	 * @return String string representation of the BuildPile
	 */
	public String toString() {
		if (build.size() == 0) {
			return "X";
		} else {
			return build.get(0).toString();
		}//end if-else
	}//end toString
	
}//end BuildPile