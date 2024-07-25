/**
 * com.client.StatRetrievalServiceClientInterface
 * CSC421 Fall 2020
 * @author Griffin Nye
 * Client Interface for the StatRetrievalService.
 */

package com.client;


public interface StatRetrievalServiceClientInterface {

	void retrieveAvgTurns();
	void retrieveGamesPlayed();
	void retrieveMaxTurns();
	void retrieveMinTurns();
	
	void recordGameStats(String turns);

}//end StatRetrievalServiceClientInterface

