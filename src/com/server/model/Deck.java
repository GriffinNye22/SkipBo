/**
 * com.client.model.Deck
 * CSC 421 Fall 2020
 * @author Griffin Nye
 * Implementation of the Deck object.
 */

package com.server.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Deck extends Pile {
	
	//private ArrayList<Card> deck;
	private ArrayList<Card> deck;
	
	/**
	 * Constructs the Deck object
	 */
	public Deck() {
		//Adds 12 of each Card to deck
		deck = new ArrayList<Card>();
		
		for (Card.Rank rank : Card.Rank.values() ) {
			for (int i = 0; i < 12; i++) {
				 deck.add(new Card(rank));
			}//end inner for
		}//end outer for
		
		for (int i = 0; i < 6; i++) {
			deck.add(new Card(Card.Rank.WILD));
		}//end for
	}//end Deck
	
	/**
	 * Draws the top Card from the Deck
	 * @return Card the Card on top of the Deck 
	 */
	public Card getTop() {
		return deck.get(0);
	}//end getTop
	
	/**
	 * Removes the top Card from the Deck
	 */
	public void removeTop() {
		deck.remove(0);
	}//end removeTop

	/**
	 * Draws the specified number of Cards from the top of the Deck
	 * @param num Number of Cards to be drawn
	 * @return ArrayList ArrayList containing the Cards drawn from the top of the Deck
	 */
	public ArrayList<Card> draw(int num) {
		ArrayList<Card> drawnCards = new ArrayList<Card>();
		
		//Adds num cards from top of deck to drawnCards
		for (int i = 0; i < num; i++) {
			drawnCards.add(deck.get(i));
		}//end for
		
		//Removes num cards from the top of the deck
		deck.subList(0,num).clear();
		
		return drawnCards;
	}//end draw
	
	/**
	 * Shuffles the deck 
	 */
	public void shuffle() {
		Random randomGen = new Random();
		
		for (int i = deck.size() - 1; i > 0; i--) {
			int num = randomGen.nextInt(i);
			Card rand = deck.get(num);
			Card last = deck.get(i);
			deck.set(i, rand);
			deck.set(num, last);
		}//end for
		
	}//end shuffle
	
	/**
	 * Returns the number of Cards in the Deck
	 * @return int number of Cards in the Deck 
	 */
	public int getCount() {
		return deck.size();
	}//end shuffle
	
	/**
	 * Returns string representation of the Deck object
	 * @return String string representation of the Deck object
	 */
	public String toString() {
		Iterator<Card> itr = deck.iterator();
		String deckString;
		
		deckString = itr.next().toString();
		
		while (itr.hasNext() ) {
			deckString+= " " + itr.next().toString();
		}//end while
		
		return deckString;
	}//end toString
	
}//end Deck
