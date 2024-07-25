/**
 * com.client.StatRetrievalService
 * CSC421 Fall 2020
 * @author Griffin Nye
 * Server Interface for the StatRetrievalService.
 */

package com.client;

import com.google.gwt.user.client.rpc.RemoteService;
import com.google.gwt.user.client.rpc.RemoteServiceRelativePath;


@RemoteServiceRelativePath("StatRetrievalService")
public interface StatRetrievalService extends RemoteService {

	Float retrieveAvgTurns();
	Integer retrieveGamesPlayed();
	Integer retrieveMaxTurns();
	Integer retrieveMinTurns();
	
	void recordGameStats(String turns);
	
}//end StatRetriealService
