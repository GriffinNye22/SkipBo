/**
 * com.client.StatRetrievalServiceAsync
 * CSC421 Fall 2020
 * @author Griffin Nye
 * Async Interface for the StatRetrievalService.
 */

package com.client;

import com.google.gwt.user.client.rpc.AsyncCallback;

public interface StatRetrievalServiceAsync {
	
	void retrieveAvgTurns(AsyncCallback callback);
	void retrieveGamesPlayed(AsyncCallback callback);
	void retrieveMaxTurns(AsyncCallback callback);
	void retrieveMinTurns(AsyncCallback callback);
	
	void recordGameStats(String turns, AsyncCallback callback);
	
}//end StatRetrievalServiceAsync
