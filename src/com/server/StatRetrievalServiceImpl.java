/**
 * com.server.StatRetrievalServiceImpl
 * CSC421 Fall 2020
 * @author Griffin Nye
 * Server Side-Implementation of the StatRetrievalService
 */

package com.server;

import java.io.IOException;

import com.client.StatRetrievalService;
import com.google.gwt.user.server.rpc.RemoteServiceServlet;

public class StatRetrievalServiceImpl extends RemoteServiceServlet implements StatRetrievalService {
	private StatTracker stats = new StatTracker();

	@Override
	public Float retrieveAvgTurns() {
		return (Float) stats.getAvgTurns();
	}//end retrieveAvgTurns

	@Override
	public Integer retrieveGamesPlayed() {
		return 1;
	}//end retrieveGamesPlayed

	@Override
	public Integer retrieveMaxTurns() {
		return (Integer) stats.getMaxTurns();
	}//end retrieveMaxTurns

	@Override
	public Integer retrieveMinTurns() {
		return (Integer) stats.getMinTurns();
	}//end retrieveMinTurns

	@Override
	public void recordGameStats(String turns) {

		try {
			stats.recordStats(turns);
		} catch (IOException e) {
			e.printStackTrace();
		}//end try-catch
		
	}//end recordGameStats
	
}//end StatRetrievalServiceImpl
